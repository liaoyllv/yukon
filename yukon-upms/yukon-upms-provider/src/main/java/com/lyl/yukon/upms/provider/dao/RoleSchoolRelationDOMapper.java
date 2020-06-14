package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.RoleSchoolRelationDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RoleSchoolRelationDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoleSchoolRelationDO record);

    int insertSelective(RoleSchoolRelationDO record);

    RoleSchoolRelationDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RoleSchoolRelationDO record);

    int updateByPrimaryKey(RoleSchoolRelationDO record);

    /**
     * 批量新增角色校区
     *
     * @param list 角色校区列表
     * @return int
     */
    int insertBatch(List<RoleSchoolRelationDO> list);

    /**
     * 根据角色id和校区id列表删除
     *
     * @param roleId 角色id
     * @param list   校区id列表
     * @return int
     */
    int deleteByRoleIdAndSchoolIds(@Param("roleId") String roleId, @Param("list") List<String> list, @Param("updateId") String updateId, @Param("updateDate") Date updateDate);

    /**
     * 获取角色校区关联
     *
     * @param roleId 角色 id
     */
    List<RoleSchoolRelationDO> selectByRoleId(@Param("roleId") String roleId);

}