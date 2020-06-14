package com.lyl.yukon.common.constant;

/**
 * <p>redis常量</p>
 *
 * @author liaoyl
 * @version 1.0 2018/10/22 09:24
 **/
public class RedisConstant {

    private RedisConstant() {
    }

    //----------------------请求限制----------------------
    /**
     * 请求限制
     */
    public static final String REQUEST_LIMIT_PREFIX = "requestLimit:";
    /**
     * 写操作
     */
    public static final String REQUEST_WRITE_LOCK_PREFIX = "writeLock:";

    //----------------------用户相关----------------------
    /**
     * token，用户登出的token，防止继续访问，过期为两个小时
     */
    public static final String USER_LOGOUT_TOKEN_PREFIX = "user:logoutToken:";
    /**
     * userId-user，用户信息
     */
    public static final String USER_LIST_PREFIX = "user:list:";
    /**
     * userId-roleId列表
     */
    public static final String USER_ROLES_PREFIX = "user:roles:";

    //----------------------角色相关----------------------


    //----------------------菜单相关----------------------
    /**
     * 所有菜单，树结构
     */
    public static final String MENU_ALL = "menu:all";

    //----------------------组织架构相关----------------------
    /**
     * 所有组织架构，树结构
     */
    public static final String ORG_ALL = "org:all";


}
