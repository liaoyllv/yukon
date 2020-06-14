package com.lyl.yukon.upms.api.service;


import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.entity.upms.OfficeDO;
import com.lyl.yukon.common.entity.upms.OrgDO;
import com.lyl.yukon.common.entity.upms.UserDO;
import com.lyl.yukon.upms.api.dto.OrgDetailDTO;
import com.lyl.yukon.upms.api.dto.OrgListDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 机构 service
 *
 * @author liaoyl
 */
public interface IOrgService {

    /**
     * 新增机构
     *
     * @param org     机构
     * @param office  机构组织架构信息
     * @param user    机构管理员
     * @param menuIds 管理员菜单 id
     * @return T|F
     */
    boolean insertOrg(OrgDO org, OfficeDO office, UserDO user, List<String> menuIds) throws CCWException;

    /**
     * 获取机构列表
     *
     * @param pageNum   页号
     * @param pageSize  页大小
     * @param validFlag 生效标记
     * @return 机构分页
     */
    PageInfo<OrgListDTO> getOrgPage(int pageNum, int pageSize, String validFlag);

    /**
     * 获取机构
     *
     * @param orgId 机构 id
     * @return 机构
     */
    OrgDO getById(String orgId);

    /**
     * 获取机构完整信息
     *
     * @param orgId 机构 id
     * @return 机构
     */
    OrgDetailDTO getFullInfoById(String orgId);

    /**
     * 更新机构
     *
     * @param org     Org
     * @param userId  操作人 id
     * @param menuIds 管理员菜单 id
     * @return T|F
     */
    boolean updateOrg(OrgDetailDTO org, String userId, List<String> menuIds) throws CCWException;

    /**
     * 删除机构
     *
     * @param org 机构信息
     */
    boolean deleteOrg(OrgDO org);

    /**
     * 获取用户当前切换的机构
     *
     * @param userId 用户 id
     * @return 机构
     */
    OrgDO getSwitchOnOrgByUserId(String userId);
}
