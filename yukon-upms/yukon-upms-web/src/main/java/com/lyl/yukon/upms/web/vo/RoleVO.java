package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.common.entity.upms.RoleDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * <p>角色信息</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/18 15:50
 **/

@Data
public class RoleVO {

    /**
     * 编号
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 描述
     */
    private String description;

    /**
     * 角色VO
     *
     * @param role 角色信息
     */
    public RoleVO(RoleDO role) {
        this.roleId = role.getId();
        BeanUtils.copyProperties(role, this);
    }


}
