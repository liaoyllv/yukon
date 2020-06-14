package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuDO extends BaseDO implements Serializable {

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 所有父级id
     */
    private String parentIds;

    /**
     * 根目录id
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
     * 菜单类型：0-页面，1-功能
     */
    private String menuType;

    /**
     * 是否是 CC 内部使用：0-不是，1-是
     */
    private String ccFlag;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    private static final long serialVersionUID = 1L;

}