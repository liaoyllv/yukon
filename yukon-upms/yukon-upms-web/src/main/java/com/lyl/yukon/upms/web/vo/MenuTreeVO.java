package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.upms.api.dto.MenuTreeDTO;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class MenuTreeVO {
    /**
     * 编号
     */
    private String menuId;

    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 子菜单
     */
    private List<MenuTreeVO> children;

    public MenuTreeVO(MenuTreeDTO menu) {
        this.menuId = menu.getId();
        this.menuName = menu.getMenuName();
        // 子菜单
        this.children = MenuTreeVO(new LinkedList<>(), menu.getChildren());
    }

    /**
     * 递归生成VO列表
     *
     * @param result   结果
     * @param menuTree 原菜单树结构
     */
    public static List<MenuTreeVO> MenuTreeVO(List<MenuTreeVO> result, List<MenuTreeDTO> menuTree) {
        if (menuTree != null) {
            menuTree.forEach(menu -> result.add(new MenuTreeVO(menu)));
        }
        return result;
    }

}