package cn.swei.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by swei
 */
@Setter@Getter
public class UserInfo implements Serializable {
    private Long  phone;
    private String nickName;
    private String head;
    private String birthDay;
    private String info;
}
