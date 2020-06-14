package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.UserMenuRelationDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserMenuRelationDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserMenuRelationDO record);

    int insertSelective(UserMenuRelationDO record);

    UserMenuRelationDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserMenuRelationDO record);

    int updateByPrimaryKey(UserMenuRelationDO record);

    /**
     * 批量新增用户菜单
     *
     * @param list 用户菜单列表
     * @return int
     */
    int insertBatch(List<UserMenuRelationDO> list);

    /**
     * 根据用户id和菜单id列表删除
     *
     * @param userId 用户id
     * @param orgId  机构id
     * @param list   菜单id列表
     * @return int
     */
    int deleteByUserIdAndMenuIds(@Param("userId") String userId, @Param("orgId") String orgId, @Param("list") List<String> list, @Param("updateId") String updateId, @Param("updateDate") Date updateDate);

}