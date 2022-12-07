package cn.swei.common.web.interceptor;

import cn.swei.common.constants.CommonConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Created by swei
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header(CommonConstants.FEIGN_REQUEST_KEY,CommonConstants.FEIGN_REQUEST_TRUE);
    }
}
