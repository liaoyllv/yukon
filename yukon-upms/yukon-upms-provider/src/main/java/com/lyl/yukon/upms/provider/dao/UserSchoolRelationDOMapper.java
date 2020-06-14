package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.UserSchoolRelationDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserSchoolRelationDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserSchoolRelationDO record);

    int insertSelective(UserSchoolRelationDO record);

    UserSchoolRelationDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserSchoolRelationDO record);

    int updateByPrimaryKey(UserSchoolRelationDO record);

    int insertBatch(@Param("list") List<UserSchoolRelationDO> list);

    /**
     * 根据用户id和组织架构id列表删除
     *
     * @param userId 用户id
     * @param list   组织架构id列表
     * @return int
     */
    int deleteByUserIdAndSchoolIds(@Param("userId") String userId, @Param("list") List<String> list, @Param("updateId") String updateId, @Param("updateDate") Date updateDate);

    /**
     * 获取用户在该机构下有的角色
     *
     * @param userId 用户 id
     * @param orgId  记过 id
     */
    List<UserSchoolRelationDO> selectByUserIdAndOrgId(@Param("userId") String userId, @Param("orgId") String orgId);
}