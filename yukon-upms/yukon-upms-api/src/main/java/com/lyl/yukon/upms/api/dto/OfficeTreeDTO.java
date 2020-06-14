package com.lyl.yukon.upms.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class OfficeTreeDTO implements Serializable {
    /**
     * id
     */
    private String id;

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
     * 创建者
     */
    private String createId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新者
     */
    private String updateId;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    private String delFlag;
    /**
     * 子组织架构
     */
    private List<OfficeTreeDTO> children = new LinkedList<>();

    private static final long serialVersionUID = 1L;

}