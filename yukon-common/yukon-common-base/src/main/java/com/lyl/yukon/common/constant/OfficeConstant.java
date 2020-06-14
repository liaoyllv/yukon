package com.lyl.yukon.common.constant;

/**
 * <p>组织架构常量</p>
 *
 * @author liaoyl
 * @version 1.0 2019/8/5 11:24
 **/
public class OfficeConstant {

    private OfficeConstant() {
    }

    /**
     * CC的机构id
     */
    public final static String CC_ID = "1";
    /**
     * 最外层的深度
     */
    public final static int TOP_DEPTH = 1;
    /**
     * 最大深度
     */
    public final static int MAX_DEPTH = 6;
    /**
     * 类型：0-其他，1-机构，2-校区，3-部门
     */
    public final static String TYPE_OTHER = "0";
    public final static String TYPE_ORG = "1";
    public final static String TYPE_SCHOOL = "2";
    public final static String TYPE_DEPARTMENT = "3";

}
