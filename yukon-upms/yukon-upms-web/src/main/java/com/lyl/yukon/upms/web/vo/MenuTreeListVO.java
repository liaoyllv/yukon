package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.upms.api.dto.MenuTreeDTO;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class MenuTreeListVO {
    /**
     * 编号
     */
    private String menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 菜单key值
     */
    private String menuKey;

    /**
     * 菜单类型：0-页面，1-功能
     */
    private String menuType;

    /**
     * 链接
     */
    private String href;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 是否是 CC 内部使用：0-不是，1-是
     */
    private String ccFlag;

    /**
     * 子菜单
     */
    private List<MenuTreeListVO> children;

    private MenuTreeListVO(MenuTreeDTO menu, String parentName) {
        this.menuId = menu.getId();
        this.menuName = menu.getMenuName();
        this.parentName = parentName;
        this.menuKey = menu.getMenuKey();
        this.menuType = menu.getMenuType();
        this.href = menu.getHref();
        this.validFlag = menu.getValidFlag();
        this.ccFlag = menu.getCcFlag();
        // 子菜单
        this.children = MenuTreeVoList(new LinkedList<>(), menu.getChildren(), menu.getMenuName());
    }

    /**
     * 递归生成VO列表
     *
     * @param result   结果
     * @param menuTree 原菜单树结构
     * @param parentName 父菜单名称
     */
    public static List<MenuTreeListVO> MenuTreeVoList(List<MenuTreeListVO> result, List<MenuTreeDTO> menuTree, String parentName) {
        if (menuTree != null) {
            menuTree.forEach(menu -> result.add(new MenuTreeListVO(menu, parentName)));
        }
        return result;
    }

}