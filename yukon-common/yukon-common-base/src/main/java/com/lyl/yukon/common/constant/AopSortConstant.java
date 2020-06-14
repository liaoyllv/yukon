package com.lyl.yukon.common.constant;

/**
 * <p>AOP的顺序排序（数字越低越靠前）</p>
 *
 * @author liaoyl
 * @version 1.0 2019/10/09 16:30
 **/
public class AopSortConstant {

    /**
     * 身份认证
     */
    public static final int AUTHENTICATION_SORT = 10;
    /**
     * 请求拦截
     */
    public static final int REQUEST_LIMIT_SORT = 20;
    /**
     * 写操作锁
     */
    public static final int WRITELOCK_SORT = 30;
    /**
     * 请求参数加解密
     */
    public static final int ENCRYPT_SORT = 40;
    /**
     * 请求日志
     */
    public static final int LOG_SORT = 50;
    /**
     * 接口权限
     */
    public static final int PERMISSION_SORT = 60;

}
