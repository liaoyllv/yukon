package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class OfficeDO extends BaseDO implements Serializable {

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 所有父级id
     */
    private String parentIds;

    /**
     * 根id
     */
    private String rootId;

    /**
     * 名称
     */
    private String officeName;

    /**
     * 类型：0-其他，1-机构，2-校区，3-部门
     */
    private String officeType;

    /**
     * 深度，最多为 6，最上级为 1
     */
    private Integer depth;

    /**
     * 负责人（展示信息，不存在绑定关系）
     */
    private String masterName;

    /**
     * 负责人电话
     */
    private String masterPhone;

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