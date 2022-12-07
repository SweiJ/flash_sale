package cn.swei.common.exception;

import cn.swei.common.web.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by swei
 */
public class CommonControllerAdvice {
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result handleBusinessException(BusinessException ex){
        return Result.error(ex.getCodeMsg());
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleDefaultException(Exception ex){
        ex.printStackTrace();//在控制台打印错误消息.
        return Result.defaultError();
    }
}
