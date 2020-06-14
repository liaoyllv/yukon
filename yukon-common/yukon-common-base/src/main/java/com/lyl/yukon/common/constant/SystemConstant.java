package com.lyl.yukon.common.constant;

/**
 * <p>系统常量</p>
 *
 * @author liaoyl
 * @version 1.0 2018/11/06 15:31
 **/
public class SystemConstant {

    private SystemConstant() {
    }

    /**
     * 环境：dev-开发，test-测试，pre-预发，prod-生产
     */
    public static final String ENV_DEV = "dev";
    public static final String ENV_TEST = "test";
    public static final String ENV_PRE = "pre";
    public static final String ENV_PROD = "prod";

    /**
     * 终端类型：iOS、安卓、网页
     */
    public static final String TERMINAL_IOS = "ios";
    public static final String TERMINAL_ANDROID = "android";
    public static final String TERMINAL_WEB = "web";

    /**
     * 移动方向：0-向上，1-向下
     */
    public static final String DIRECTION_UP = "0";
    public static final String DIRECTION_DOWN = "1";

    /**
     * 是否置顶：0-未置顶，1-已置顶
     */
    public static final String STICKY_FLAG_NO = "0";
    public static final String STICKY_FLAG_YES = "1";

    /**
     * 是否删除：0-未删除，1-已删除
     */
    public static final String DEL_FLAG_NO = "0";
    public static final String DEL_FLAG_YES = "1";

    /**
     * 是否生效：0-不是，1-是
     */
    public static final String VALID_FLAG_NO = "0";
    public static final String VALID_FLAG_YES = "1";

    /**
     * 是否显示失效：0-不显示，1-显示
     */
    public static final String DISPLAY_FLAG_NO = "0";
    public static final String DISPLAY_FLAG_YES = "1";

    /**
     * 是否是 CC 内部使用：0-不是，1-是
     */
    public static final String CC_FLAG_NO = "0";
    public static final String CC_FLAG_YES = "1";

    /**
     * code类型
     */
    public static final String ENCODE_UTF = "utf-8";

    /**
     * 区域类型：0-省，1-市，2-区，3-街道
     */
    public static final String REGION_TYPE_PROVINCE = "0";
    public static final String REGION_TYPE_CITY = "1";
    public static final String REGION_TYPE_AREA = "2";
    public static final String REGION_TYPE_STREET = "3";
}
