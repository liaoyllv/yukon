package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.common.entity.upms.MenuDO;
import lombok.Data;

@Data
public class MenuVO {
    /**
     * 菜单id
     */
    private String menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单名称
     */
    private String parentMenuName;

    /**
     * 菜单key值
     */
    private String menuKey;

    /**
     * 链接
     */
    private String href;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

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
     * 备注
     */
    private String remarks;

    public MenuVO(MenuDO menu, String parentMenuName) {
        this.menuId = menu.getId();
        this.menuName = menu.getMenuName();
        this.parentMenuName = parentMenuName;
        this.menuKey = menu.getMenuKey();
        this.href = menu.getHref();
        this.validFlag = menu.getValidFlag();
        this.remarks = menu.getRemarks();
    }


}