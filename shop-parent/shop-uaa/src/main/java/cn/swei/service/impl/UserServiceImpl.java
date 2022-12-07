package cn.swei.service.impl;

import cn.swei.common.domain.UserInfo;
import cn.swei.domain.UserResponse;
import cn.swei.common.exception.BusinessException;
import cn.swei.domain.LoginLog;
import cn.swei.domain.UserLogin;
import cn.swei.mapper.UserMapper;
import cn.swei.mq.MQConstant;
import cn.swei.redis.CommonRedisKey;
import cn.swei.redis.UaaRedisKey;
import cn.swei.service.IUserService;
import cn.swei.util.MD5Util;
import cn.swei.web.msg.UAACodeMsg;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by swei
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    private UserLogin getUser(Long phone){
        UserLogin userLogin;
        String userLoginHashKey = UaaRedisKey.USERLOGIN_HASH.getRealKey("");
        String userInfoHashKey = UaaRedisKey.USERINFO_HASH.getRealKey("");
        String zSetKey = UaaRedisKey.USER_ZSET.getRealKey("");
        String userKey = String.valueOf(phone);
        String objStr = (String) redisTemplate.opsForHash().get(userLoginHashKey, String.valueOf(phone));
        if(StringUtils.isEmpty(objStr)){
            //缓存中并没有，从数据库中查询
            userLogin = userMapper.selectUserLoginByPhone(phone);
            //把用户的登录信息存储到Hash结构中.
            if(userLogin!=null){
                UserInfo userInfo = userMapper.selectUserInfoByPhone(phone);
                redisTemplate.opsForHash().put(userInfoHashKey,userKey,JSON.toJSONString(userInfo));
                redisTemplate.opsForHash().put(userLoginHashKey,userKey,JSON.toJSONString(userLogin));
            }
            //使用zSet结构,value存用户手机号码，分数为登录时间，在定时器中找出7天前登录的用户，然后再缓存中删除.
            //我们缓存中的只存储7天的用户登录信息(热点用户)
        }else{
            //缓存中有这个key
            userLogin = JSON.parseObject(objStr,UserLogin.class);
        }
        redisTemplate.opsForZSet().add(zSetKey,userKey,System.currentTimeMillis());
        return userLogin;
    }
    @Override
    public UserResponse login(Long phone, String password, String ip) {
        //无论登录成功还是登录失败,都需要进行日志记录
        LoginLog loginLog = new LoginLog(phone,ip,new Date());
        //根据用户手机号码查询用户对象
        UserLogin userLogin = this.getUser(phone);
        //进行密码加盐比对
        if(userLogin==null || !userLogin.getPassword().equals(MD5Util.encode(password,userLogin.getSalt()))){
            //进入这里说明登录失败
            loginLog.setState(LoginLog.LOGIN_FAIL);
            //往MQ中发送消息,登录失败
            rocketMQTemplate.sendOneWay(MQConstant.LOGIN_TOPIC+":"+LoginLog.LOGIN_FAIL,loginLog);
            //同事抛出异常，提示前台账号密码有误
            throw new BusinessException(UAACodeMsg.LOGIN_ERROR);
        }
        //查询
        //UserInfo userInfo = userMapper.selectUserInfoByPhone(phone);
        UserInfo userInfo = this.getUserInfo(phone);
        String token = createToken(String.valueOf(phone));
        rocketMQTemplate.sendOneWay(MQConstant.LOGIN_TOPIC,loginLog);
        return new UserResponse(token,userInfo);
    }

    @Override
    public UserInfo getUserInfo(Long phone) {
        String userInfoHashKey = UaaRedisKey.USERINFO_HASH.getRealKey("");
        String objStr = (String) redisTemplate.opsForHash().get(userInfoHashKey, String.valueOf(phone));
        return JSON.parseObject(objStr,UserInfo.class);
    }

    private String createToken(String phone) {
        //token创建
        String token = UUID.randomUUID().toString().replace("-","");
        //把user对象存储到redis中
        CommonRedisKey user_token_key = CommonRedisKey.USER_TOKEN;
        redisTemplate.opsForValue().set(user_token_key.getRealKey(token), phone, user_token_key.getExpireTime(),user_token_key.getUnit());
        return token;
    }
}
