package cn.swei.common.exception;

import cn.swei.common.web.CodeMsg;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by swei
 */
@Setter
@Getter
public class BusinessException extends RuntimeException {
    private CodeMsg codeMsg;
    public BusinessException(CodeMsg codeMsg){
        this.codeMsg = codeMsg;
    }
}
