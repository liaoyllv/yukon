package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleSchoolRelationDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 校区id
     */
    private String schoolId;
}