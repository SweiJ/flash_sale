package cn.swei.mq;

import cn.swei.domain.LoginLog;
import cn.swei.mapper.UserMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by swei
 */
@Component
@RocketMQMessageListener(consumerGroup = "LoginLogGroup",topic = MQConstant.LOGIN_TOPIC)
public class MQLoginLogListener implements RocketMQListener<LoginLog> {
    @Autowired
    private UserMapper userMapper;
    @Override
    public void onMessage(LoginLog message) {
        //通过MQ进行异步登录日志记录
        userMapper.insertLoginLong(message);
    }
}
