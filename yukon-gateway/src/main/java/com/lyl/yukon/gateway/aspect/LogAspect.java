package com.lyl.yukon.gateway.aspect;

import com.lyl.yukon.common.constant.AopSortConstant;
import com.lyl.yukon.common.entity.RequestLogDO;
import com.lyl.yukon.common.util.JsonUtils;
import com.lyl.yukon.common.util.JwtUtils;
import com.lyl.yukon.common.util.StringUtils;
import com.lyl.yukon.common.util.WebUtils;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * <p>请求日志记录AOP实现</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/15 17:06
 **/
@Order(AopSortConstant.LOG_SORT)
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 开始时间
     */
    private ThreadLocal<Long> startTime = new ThreadLocal();

//    @Autowired
//    private MongoRepository mongoRepository;


    @Before("execution(* *..controller..*.*(..))")
    public void doBeforeInServiceLayer() {
        startTime.set(System.currentTimeMillis());
    }


    @Around("execution(* *..controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        RequestLogDO log = new RequestLogDO();
        log.setId(UUID.randomUUID().toString());
        log.setPath(request.getServletPath());
        log.setIp(WebUtils.getRemoteAddr(request));
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            log.setParameter(request.getQueryString());
        } else {
            log.setParameter("");
            for (Object arg : pjp.getArgs()) {
                if (arg.getClass() != RequestFacade.class && arg.getClass() != ResponseFacade.class) {
                    log.setParameter(JsonUtils.toJson(arg));
                }
            }
        }
        // 执行操作，获取响应结果
        Object result = pjp.proceed();

        long spendTime = System.currentTimeMillis() - startTime.get();
        log.setResult(JsonUtils.toJson(result));
        log.setSpendTime((spendTime));
        if (StringUtils.isNotBlank(request.getHeader("Authorization"))) {
            log.setCreateId(JwtUtils.getUserId(request.getHeader("Authorization").substring("Bearer ".length())));
        } else {
            log.setCreateId("");
        }
        log.setCreateDate(new Date());
        startTime.remove();
//        mongoRepository.insert(log, MongoConstant.COLLECTION_REQUEST_RECORD);
        return result;
    }


}
