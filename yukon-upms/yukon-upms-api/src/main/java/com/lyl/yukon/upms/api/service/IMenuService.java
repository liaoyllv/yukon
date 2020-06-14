package com.lyl.yukon.upms.api.service;


import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.entity.upms.MenuDO;
import com.lyl.yukon.upms.api.dto.MenuTreeDTO;

import java.util.List;

/**
 * 菜单service
 *
 * @author liaoyl
 */
public interface IMenuService {


    /**
     * 新增菜单
     *
     * @param menu 菜单
     * @return T|F
     */
    boolean insertMenu(MenuDO menu);

    /**
     * 获取菜单详情
     *
     * @param menuId 菜单id
     * @return 菜单
     */
    MenuDO getMenuById(String menuId);

    /**
     * 更新菜单
     *
     * @param menu 菜单
     * @return T|F
     */
    boolean updateMenu(MenuDO menu);

    /**
     * 删除菜单
     *
     * @param menu 菜单
     * @return T|F
     */
    boolean deleteMenu(MenuDO menu) throws CCWException;

    /**
     * 获取所有菜单
     *
     * @param ccFlag 是否是 CC 内部使用：0-不是，1-是
     * @return 所有菜单列表
     */
    List<MenuTreeDTO> getAllMenuTreeList(String ccFlag);

    /**
     * 校验菜单名称是否存在
     *
     * @param menuId   菜单id
     * @param menuName 菜单名称
     * @return T|F
     */
    boolean checkMenuName(String menuId, String menuName);

    /**
     * 校验菜单key是否存在
     *
     * @param menuId  菜单id
     * @param menuKey 菜单key
     * @return T|F
     */
    boolean checkMenuKey(String menuId, String menuKey);

    /**
     * 移动
     *
     * @param menuId    菜单 id
     * @param direction 方向
     * @return T|F
     */
    boolean move(String menuId, String direction);

    /**
     * 获取角色的菜单
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    List<MenuDO> getByRoleId(String roleId);

    /**
     * 获取用户的菜单
     *
     * @param userId 用户id
     * @return 菜单列表
     */
    List<MenuDO> getByUserId(String userId);

    /**
     * 获取菜单的功能列表详情
     *
     * @param menuId    菜单id
     * @param validFlag 生效标记
     * @return 功能列表
     */
    List<MenuDO> getFunctionListByMenuId(String menuId, String validFlag);

    /**
     * 获取用户当前机构下的菜单树
     *
     * @param userId 用户id
     * @return 菜单树
     */
    List<MenuTreeDTO> getMenuTreeByUserId(String userId);

}
