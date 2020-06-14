package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDO extends BaseDO implements Serializable {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色类型：0-系统角色，1-自定义角色
     */
    private String roleType;

    /**
     * 自定义角色关联的机构 id
     */
    private String orgId;

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