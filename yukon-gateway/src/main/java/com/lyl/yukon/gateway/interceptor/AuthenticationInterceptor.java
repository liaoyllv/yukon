package com.lyl.yukon.gateway.interceptor;

import com.lyl.yukon.common.constant.AopSortConstant;
import com.lyl.yukon.common.redis.RedisRepository;
import com.lyl.yukon.common.constant.RedisConstant;
import com.lyl.yukon.gateway.annotation.Anon;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
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
 * <p>认证拦截器</p>
 *
 * @author liaoyl
 * @version 1.0 2019/3/15 16:48
 **/
@Order(AopSortConstant.AUTHENTICATION_SORT)
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    private RedisRepository redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws CCWException {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 如果不是匿名访问
        if (!method.isAnnotationPresent(Anon.class)) {
            // 从http请求头中取出签名
            String token = request.getHeader("Authorization");
            if (StringUtils.isBlank(token)) {
                throw new CCWException(EnumMsgCode.MSG_UPMS_102);
            }
            token = token.substring("Bearer ".length());

            // 该 token 是否已经退出不能用
            if (redis.exists(RedisConstant.USER_LOGOUT_TOKEN_PREFIX + token)) {
                throw new CCWException(EnumMsgCode.MSG_UPMS_103);
            }

            // token合法性校验
            if (!JwtUtils.verify(token)) {
                // 校验不通过的原因是否为需要重新登录
                if (JwtUtils.isExpire(JwtUtils.getTokenInfo(token).getCreateDate())) {
                    throw new CCWException(EnumMsgCode.MSG_UPMS_103);
                } else {
                    // 伪造的token
                    throw new CCWException(EnumMsgCode.MSG_UPMS_102);
                }
            } else {
                // 是否需要刷新token
                if (JwtUtils.needRefresh(JwtUtils.getTokenInfo(token).getCreateDate())) {
                    // 刷新token
                    token = JwtUtils.refresh(token);
                    // 设置新的token
                    response.setHeader("Authorization", token);
                }
            }
        }
        return true;
    }

}
