package cn.swei.web.controller;

import cn.swei.common.constants.CommonConstants;
import cn.swei.domain.UserResponse;
import cn.swei.common.web.Result;
import cn.swei.domain.UserLogin;
import cn.swei.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by swei
 * 登录操作
 */
@RestController
@RequestMapping("/token")
public class LoginController {
    @Autowired
    private IUserService userService;
    @PostMapping
    public Result<UserResponse> login(@RequestBody UserLogin userLogin, HttpServletRequest request){
        /**
         * 获取用户IP,因为微服务的请求是网关转发过来的.
         * 所以request.getRemoteAddr()获取到的是网关的IP
         * 我们需要在网关中获取到真实IP,然后放入到请求头中。
         * 在微服务中通过获取请求头从而获取到真实的客户端IP
         */
        String ip = request.getHeader(CommonConstants.REAL_IP);
        //进行登录，并将这个token返回给前台
        UserResponse userResponse = userService.login(userLogin.getPhone(),userLogin.getPassword(),ip);
        return Result.success(userResponse);
    }
}
