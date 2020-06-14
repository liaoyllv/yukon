package com.lyl.yukon.upms.web.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lyl.yukon.common.entity.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * <p>用户请求参数接收</p>
 *
 * @author liaoyl
 * @version 1.0 2019/03/15 15:02
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class UserParam extends BaseParam {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户姓名
     */
    @Length(max = 10)
    private String userName;

    /**
     * 真实姓名
     */
    @Length(max = 10)
    private String realName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别：0-男，1-女
     */
    private String gender;

    /**
     * 描述
     */
    private String description;

    /**
     * 所属部门列表
     */
    private List<String> officeIds;

    /**
     * 拥有的角色列表
     */
    private List<String> roleIds;

    /**
     * 查看的校区列表
     */
    private List<String> schoolIds;

    /**
     * 可查看的菜单列表
     */
    private List<String> menuIds;

    /**
     * 老密码
     */
    private String oriPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 终端类型
     */
    private String terminalType;

}
