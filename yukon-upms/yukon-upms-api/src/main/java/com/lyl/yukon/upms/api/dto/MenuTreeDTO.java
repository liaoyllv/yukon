package com.lyl.yukon.upms.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>机构列表</p>
 *
 * @author liaoyl
 * @version 1.0 2019/07/18 17:37
 **/
@Data
public class MenuTreeDTO implements Serializable {
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
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单key值
     */
    private String menuKey;

    /**
     * 排序（由小到大）
     */
    private Integer sort;

    /**
     * 链接
     */
    private String href;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单类型：0-左侧菜单，1-普通按钮
     */
    private String menuType;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 是否是 CC 内部使用：0-不是，1-是
     */
    private String ccFlag;

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
     * 删除标记
     */
    private String delFlag;

    /**
     * 子菜单
     */
    private List<MenuTreeDTO> children;

    private static final long serialVersionUID = 1L;

}
