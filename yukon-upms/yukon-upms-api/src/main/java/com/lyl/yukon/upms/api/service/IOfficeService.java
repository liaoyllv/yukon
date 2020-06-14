package com.lyl.yukon.upms.api.service;


import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.entity.upms.OfficeDO;
import com.lyl.yukon.upms.api.dto.OfficeTreeDTO;

import java.util.List;

/**
 * 组织架构 service
 *
 * @author liaoyl
 */
public interface IOfficeService {

    /**
     * 新增组织架构
     *
     * @param office 组织架构
     * @return T|F
     */
    boolean insertOffice(OfficeDO office);

    /**
     * 获取组织架构详情
     *
     * @param officeId 组织架构 id
     */
    OfficeDO getOfficeById(String officeId);

    /**
     * 获取机构的组织架构树
     *
     * @param orgId     机构 id
     * @param validFlag 是否生效：0-不是，1-是
     */
    OfficeTreeDTO getOfficeTreeByOrgId(String orgId, String validFlag);

    /**
     * 更新组织架构
     *
     * @param office Office
     * @return T|F
     */
    boolean updateOffice(OfficeDO office);

    /**
     * 删除组织架构
     *
     * @param office 组织架构
     * @return T|F
     */
    boolean deleteOffice(OfficeDO office) throws CCWException;

    /**
     * 校验组织架构名称是否存在
     *
     * @param officeId   组织架构 id
     * @param orgId      机构id
     * @param officeName 组织架构名称
     * @return T|F
     */
    boolean checkOfficeName(String officeId, String orgId, String officeName);

    /**
     * 获取用户所在的组织架构树
     *
     * @param validFlag 生效状态
     * @param userId    用户 id
     * @return 所有组织架构树结构
     */
    OfficeTreeDTO getOfficeTreeByUserId(String validFlag, String userId);

    /**
     * 更新生效状态
     *
     * @param orgId     机构 id
     * @param validFlag 是否生效：0-不是，1-是
     * @param userId    操作人 id
     * @return T|F
     */
    boolean updateValidFlag(String orgId, String validFlag, String userId);

    /**
     * 用户当前机构下所属组织架构
     *
     * @param userId 用户 id
     */
    List<OfficeDO> getByUserId(String userId);
}
