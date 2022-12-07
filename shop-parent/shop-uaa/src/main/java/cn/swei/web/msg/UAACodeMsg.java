package cn.swei.web.msg;
import cn.swei.common.web.CodeMsg;

/**
 * Created by swei
 */
public class UAACodeMsg extends CodeMsg {
    private UAACodeMsg(Integer code, String msg){
        super(code,msg);
    }
    public static final UAACodeMsg LOGIN_ERROR = new UAACodeMsg(500101,"账号密码有误");
}
