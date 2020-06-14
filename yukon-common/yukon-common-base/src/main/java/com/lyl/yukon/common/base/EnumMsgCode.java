package com.lyl.yukon.common.base; /**
 *
 */

import org.apache.commons.lang3.StringUtils;

/**
 * app返回码定义
 *
 * @author louxinhua
 */
public enum EnumMsgCode {

    // 只有这个是操作成功的标志，其余的都是错误的
    MSG_SUCCESS("0000", "oh yeah,通过了"),

    // 系统通用错误
    MSG_SYSTEM_1001("1001", "参数不全"),
    MSG_SYSTEM_1002("1002", "参数校验不通过"),
    MSG_SYSTEM_1003("1003", "访问次数过多"),
    MSG_SYSTEM_1004("1004", "请勿重复提交"),
    MSG_SYSTEM_1005("1005", "操作失败"),
    MSG_SYSTEM_1006("1006", "数据不存在"),
    MSG_SYSTEM_1007("1007", "名称已存在"),
    MSG_SYSTEM_1008("1008", "key已存在"),
    MSG_SYSTEM_1009("1009", "请先删除子元素"),

    // 用户相关
    MSG_UPMS_101("101", "权限不足"),
    MSG_UPMS_102("102", "请先登录"),
    MSG_UPMS_103("103", "身份已过期，请重新登录"),
    MSG_UPMS_104("104", "您的账号在其他设备登录"),
    MSG_UPMS_105("105", "用户不存在"),
    MSG_UPMS_106("106", "手机号不存在"),
    MSG_UPMS_107("107", "密码错误"),
    MSG_UPMS_108("108", "url已存在"),
    MSG_UPMS_109("109", "功能菜单无法添加下级菜单"),
    MSG_UPMS_110("110", "手机号已存在"),
    MSG_UPMS_111("111", "无法继续添加组织架构"),
    MSG_UPMS_112("112", "用户已失效"),

    //教师班级模块
    MSG_SMS_2010("2010", "1个老师最多加入20个开班中的班级"),
    MSG_SMS_2011("2011", "手机号已存在"),
    MSG_SMS_2012("2012", "身份证已存在"),
    MSG_SMS_2013("2013", "部分班级已有在职老师达上限，不能再加入"),
    MSG_SMS_2014("2014", "1个班级最多加入5位老师"),
    MSG_SMS_2015("2015", "该校区此班级名称已存在"),
    MSG_SMS_2016("2016", "部分学生已有在班数量达上限，不能再加入"),
    MSG_SMS_2017("2017", "部分老师已有在班数量达上限，不能再加入"),
    //课程表模块
    MSG_SMS_3010("3010", "已有日程关联，请勿删除"),

    MSG_ERROR("9999", "系统异常，请稍后再试~");

    private String code;

    private String description;

    EnumMsgCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static EnumMsgCode getByKey(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (EnumMsgCode type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static boolean isSuccess(EnumMsgCode code) {
        if (code == MSG_SUCCESS) {
            return true;
        }
        return false;
    }
}
