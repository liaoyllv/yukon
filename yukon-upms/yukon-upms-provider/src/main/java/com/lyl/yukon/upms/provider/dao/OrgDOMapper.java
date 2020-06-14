package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.OrgDO;
import com.lyl.yukon.upms.api.dto.OrgDetailDTO;
import com.lyl.yukon.upms.api.dto.OrgListDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrgDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrgDO record);

    int insertSelective(OrgDO record);

    OrgDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrgDO record);

    int updateByPrimaryKey(OrgDO record);

    /**
     * 机构分页
     */
    List<OrgListDTO> selectPage(@Param("validFlag") String validFlag);

    /**
     * 查询机构完整信息
     *
     * @param orgId 机构 id
     * @return 机构信息
     */
    OrgDetailDTO selectDetailById(@Param("orgId") String orgId);

    /**
     * 查看当前切换的机构
     *
     * @param userId 用户 id
     */
    OrgDO selectSwitchOnOrgByUserId(@Param("userId") String userId);
}