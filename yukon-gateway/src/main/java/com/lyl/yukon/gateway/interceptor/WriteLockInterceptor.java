package com.lyl.yukon.gateway.interceptor;

import com.lyl.yukon.common.constant.AopSortConstant;
import com.lyl.yukon.gateway.annotation.WriteLock;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.constant.RedisConstant;
import com.lyl.yukon.common.util.JwtUtils;
import com.lyl.yukon.common.util.StringUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 读写锁拦截
 *
 * @author liaoyl
 */
@Order(AopSortConstant.WRITELOCK_SORT)
public class WriteLockInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WriteLockInterceptor.class);

    /**
     * 锁尝试获取等待时间s
     */
    private final static int LOCK_WAIT_TIME = 2;
    /**
     * 锁自动释放时间s
     */
    private final static int LOCK_LEASE_TIME = 5;

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws CCWException {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 如果有写操作锁
        if (method.isAnnotationPresent(WriteLock.class)) {
            String token = request.getHeader("Authorization");
            if (StringUtils.isBlank(token)) {
                throw new CCWException(EnumMsgCode.MSG_UPMS_102);
            }
            String userId = JwtUtils.getUserId(token.substring("Bearer ".length()));
            String path = request.getServletPath();
            String key = RedisConstant.REQUEST_WRITE_LOCK_PREFIX.concat(path).concat(":").concat(userId);
            try {
                if (!redissonClient.getLock(key).tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
                    logger.error("用户:[" + userId + "]访问地址[" + path + "]重复提交");
                    throw new CCWException(EnumMsgCode.MSG_SYSTEM_1004);
                }
            } catch (InterruptedException e) {
                throw new CCWException(EnumMsgCode.MSG_SYSTEM_1004);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 如果有写操作锁则释放
        if (method.isAnnotationPresent(WriteLock.class)) {
            String token = request.getHeader("Authorization");
            String userId = JwtUtils.getUserId(token.substring("Bearer ".length()));
            String path = request.getServletPath();
            String key = RedisConstant.REQUEST_WRITE_LOCK_PREFIX.concat(path).concat(":").concat(userId);
            redissonClient.getLock(key).unlock();
        }
    }


}
