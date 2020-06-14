package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleMenuRelationDO extends BaseDO implements Serializable {

    /**
     * 角色编号
     */
    private String roleId;

    /**
     * 菜单编号
     */
    private String menuId;

    private static final long serialVersionUID = 1L;

}