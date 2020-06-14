package com.lyl.yukon.gateway.interceptor;

import com.lyl.yukon.common.constant.AopSortConstant;
import com.lyl.yukon.common.redis.RedisRepository;
import com.lyl.yukon.gateway.annotation.RequestLimit;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.constant.RedisConstant;
import com.lyl.yukon.common.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 请求限制拦截
 *
 * @author liaoyl
 */
@Order(AopSortConstant.REQUEST_LIMIT_SORT)
public class RequestLimitInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLimitInterceptor.class);

    @Autowired
    private RedisRepository redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws CCWException {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 如果有访问限制
        if (method.isAnnotationPresent(RequestLimit.class)) {
            RequestLimit limit = method.getAnnotation(RequestLimit.class);
            String ip = WebUtils.getRemoteAddr(request);
            String path = request.getServletPath();
            String key = RedisConstant.REQUEST_LIMIT_PREFIX.concat(path).concat(":").concat(ip);

            long count = redis.incr(key);
            if (count == 1) {
                // 第一次被限制访问时开始计时
                redis.set(key, limit.time(), "");
            }
            if (count > limit.count()) {
                logger.error("用户:[" + ip + "]访问地址[" + path + "]超过了限定的次数[" + limit.count() + "]");
                // 返回限制异常
                throw new CCWException(EnumMsgCode.MSG_SYSTEM_1003);
            }
        }
        return true;
    }
}
