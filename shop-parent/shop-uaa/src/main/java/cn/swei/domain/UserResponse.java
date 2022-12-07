package cn.swei.domain;

import cn.swei.common.domain.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by swei
 * 用户在登录阶段返回给前端的对象
 */
@Setter@Getter@AllArgsConstructor@NoArgsConstructor
public class UserResponse {
    private String token;
    private UserInfo userInfo;
}
