package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.MenuDO;
import com.lyl.yukon.upms.api.dto.MenuTreeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(MenuDO record);

    int insertSelective(MenuDO record);

    MenuDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MenuDO record);

    int updateByPrimaryKey(MenuDO record);

    /**
     * 获取所有菜单
     *
     * @param ccFlag 是否是 CC 内部使用：0-不是，1-是
     * @return 菜单列表
     */
    List<MenuTreeDTO> selectAll(@Param("ccFlag") String ccFlag);

    /**
     * 查询除开menuId为xx以外菜单名称为xx的数据，
     *
     * @param menuId   菜单id
     * @param menuName 菜单名称
     * @return 菜单信息
     */
    MenuDO checkMenuName(@Param("menuId") String menuId, @Param("menuName") String menuName);

    /**
     * 查询最大排序值
     *
     * @return 排序值
     */
    int selectMaxSort();

    /**
     * 查询除开menuKey为xx以外菜单名称为xx的数据，
     *
     * @param menuId  菜单id
     * @param menuKey 菜单key
     * @return 菜单信息
     */
    MenuDO checkMenuKey(@Param("menuId") String menuId, @Param("menuKey") String menuKey);

    /**
     * 获取排序值sort相邻的文章
     *
     * @param parentId  父菜单id
     * @param menuId    菜单id
     * @param sort      排序值
     * @param direction 方向
     */
    MenuDO selectAdjacentMenu(@Param("parentId") String parentId, @Param("menuId") String menuId, @Param("sort") Integer sort, @Param("direction") String direction);

    /**
     * 获取角色的菜单
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    List<MenuDO> selectMenuListByRoleId(@Param("roleId") String roleId);

    /**
     * 获取菜单的功能列表详情
     *
     * @param menuId    菜单id
     * @param validFlag 生效标记
     * @return 功能列表
     */
    List<MenuDO> selectFunctionListByMenuId(@Param("menuId") String menuId, @Param("validFlag") String validFlag);

    /**
     * 获取用户当前机构的菜单列表
     *
     * @param userId 用户id
     * @param orgId  机构id
     * @return 菜单列表
     */
    List<MenuDO> selectMenuListByUserIdAndOrgId(@Param("userId") String userId, @Param("orgId") String orgId);
}