package cn.swei.common.web.anno;

import java.lang.annotation.*;

/**
 * Created by swei
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLogin {
}
