package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.UserRoleRelationDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserRoleRelationDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserRoleRelationDO record);

    int insertSelective(UserRoleRelationDO record);

    UserRoleRelationDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserRoleRelationDO record);

    int updateByPrimaryKey(UserRoleRelationDO record);

    /**
     * 用户id、角色id查询
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return 用户角色关联
     */
    UserRoleRelationDO selectByUserIdAndRoleId(@Param("userId") String userId, @Param("roleId") String roleId);

    int insertBatch(@Param("list") List<UserRoleRelationDO> list);

    /**
     * 根据用户id和角色id列表删除
     *
     * @param userId 用户id
     * @param list   角色id列表
     * @return int
     */
    int deleteByUserIdAndRoleIds(@Param("userId") String userId, @Param("list") List<String> list, @Param("updateId") String updateId, @Param("updateDate") Date updateDate);

}