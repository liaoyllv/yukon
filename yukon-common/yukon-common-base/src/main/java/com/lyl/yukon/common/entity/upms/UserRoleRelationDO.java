package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRoleRelationDO extends BaseDO implements Serializable {

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 角色编号
     */
    private String roleId;

    private static final long serialVersionUID = 1L;

}