package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.RoleDO;
import com.lyl.yukon.upms.api.dto.RoleBriefDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoleDO record);

    int insertSelective(RoleDO record);

    RoleDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RoleDO record);

    int updateByPrimaryKey(RoleDO record);

    /**
     * 获取角色分页
     *
     * @param orgId     机构id
     * @param validFlag 生效标记
     * @return 角色列表
     */
    List<RoleDO> selectPage(@Param("orgId") String orgId, @Param("validFlag") String validFlag);

    /**
     * 查询除开roleId为xx以外角色名称为xx的数据，
     *
     * @param roleId   角色id
     * @param orgId    机构id
     * @param roleName 角色名称
     * @return 角色信息
     */
    RoleDO checkRoleName(@Param("roleId") String roleId, @Param("orgId") String orgId, @Param("roleName") String roleName);

    /**
     * 获取用户该机构以及系统通用的角色列表
     *
     * @param userId 用户 id
     * @param orgId  机构 id
     */
    List<RoleDO> selectByUserIdAndOrgId(@Param("userId") String userId, @Param("orgId") String orgId);

    /**
     * 机构下的角色
     *
     * @param orgId 机构 id
     */
    List<RoleBriefDTO> selectBriefInfoByOrgId(@Param("orgId") String orgId);
}