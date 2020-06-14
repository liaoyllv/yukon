package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.OfficeDO;
import com.lyl.yukon.upms.api.dto.OfficeTreeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OfficeDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(OfficeDO record);

    int insertSelective(OfficeDO record);

    OfficeDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OfficeDO record);

    int updateByPrimaryKey(OfficeDO record);

    /**
     * 获取机构下所有组织架构
     *
     * @param orgId     机构 id
     * @param validFlag 是否生效
     * @return 组织架构列表
     */
    List<OfficeTreeDTO> selectAllByOrgId(@Param("orgId") String orgId, @Param("validFlag") String validFlag);

    /**
     * 查询除开officeId为xx以外officeName为xx的数据，
     *
     * @param officeId   组织架构 id
     * @param orgId      机构 id
     * @param officeName 组织架构名称
     * @return 组织架构信息
     */
    OfficeDO checkOfficeName(@Param("officeId") String officeId, @Param("orgId") String orgId, @Param("officeName") String officeName);

    /**
     * 用户在这个机构下所属的组织架构
     *
     * @param userId 用户 id
     * @param orgId  机构 id
     */
    List<OfficeDO> selectByUserIdAndOrgId(@Param("userId") String userId, @Param("orgId") String orgId);
}