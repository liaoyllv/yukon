package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.RoleMenuRelationDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RoleMenuRelationDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoleMenuRelationDO record);

    int insertSelective(RoleMenuRelationDO record);

    RoleMenuRelationDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RoleMenuRelationDO record);

    int updateByPrimaryKey(RoleMenuRelationDO record);

    /**
     * 批量新增角色菜单
     *
     * @param list 角色菜单列表
     * @return int
     */
    int insertBatch(List<RoleMenuRelationDO> list);

    /**
     * 根据角色id和菜单id列表删除
     *
     * @param roleId 角色id
     * @param list   菜单id列表
     * @return int
     */
    int deleteByRoleIdAndMenuIds(@Param("roleId") String roleId, @Param("list") List<String> list, @Param("updateId") String updateId, @Param("updateDate") Date updateDate);

    /**
     * 角色菜单关联
     *
     * @param roleId 角色 id
     */
    List<RoleMenuRelationDO> selectByRoleId(@Param("roleId") String roleId);
}