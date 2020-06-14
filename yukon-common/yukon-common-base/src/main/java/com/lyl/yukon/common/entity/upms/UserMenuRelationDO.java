package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserMenuRelationDO extends BaseDO implements Serializable {

    /**
     * 角色id
     */
    private String userId;
    /**
     * 菜单id
     */
    private String menuId;
    /**
     * 机构id
     */
    private String orgId;

    private static final long serialVersionUID = 1L;

}