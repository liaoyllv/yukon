package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.UserOrgRelationDO;

public interface UserOrgRelationDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserOrgRelationDO record);

    int insertSelective(UserOrgRelationDO record);

    UserOrgRelationDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserOrgRelationDO record);

    int updateByPrimaryKey(UserOrgRelationDO record);
}