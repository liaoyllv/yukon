package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDO extends BaseDO implements Serializable {

    /**
     * 手机
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别：0-男，1-女
     */
    private String gender;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 描述
     */
    private String description;

    private static final long serialVersionUID = 1L;

}