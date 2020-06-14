package com.lyl.yukon.upms.provider.service;

import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.entity.upms.OfficeDO;
import com.lyl.yukon.common.entity.upms.OrgDO;
import com.lyl.yukon.upms.api.service.IOfficeService;
import com.lyl.yukon.upms.api.dto.OfficeTreeDTO;
import com.lyl.yukon.upms.provider.dao.OfficeDOMapper;
import com.lyl.yukon.upms.provider.dao.OrgDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/17 16:12
 **/
@Service("officeService")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class OfficeServiceImpl implements IOfficeService {

    @Autowired
    private OfficeDOMapper officeDOMapper;

    @Autowired
    private OrgDOMapper orgDOMapper;

    /**
     * 递归生成
     *
     * @param result    id为parentId这层的列表
     * @param totalList 所有数据
     * @param parentId  父id
     * @return 最终的树结构列表
     */
    private static List<OfficeTreeDTO> recursiveGen(List<OfficeTreeDTO> result, List<OfficeTreeDTO> totalList, String parentId) {
        for (OfficeTreeDTO office : totalList) {
            // 找到所有parentId的子office
            if (office.getParentId().equals(parentId)) {
                result.add(office);
                // 当前office的子office
                List<OfficeTreeDTO> children = new LinkedList<>();
                // 填充这个office的子office
                recursiveGen(children, totalList, office.getId());
                office.setChildren(children);
            }
        }
        return result;
    }

    /**
     * 递归寻找某个节点的组织架构
     *
     * @param totalTree 所有数据（recursiveGen得到的树结构）
     * @param officeId  要找的组织架构 id
     * @return 要找的Office
     */
    private static OfficeTreeDTO recursiveFind(List<OfficeTreeDTO> totalTree, String officeId) {
        for (OfficeTreeDTO office : totalTree) {
            if (office.getId().equals(officeId)) {
                return office;
            } else {
                // 如果有子Office
                if (!office.getChildren().isEmpty()) {
                    return recursiveFind(office.getChildren(), officeId);
                }
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean insertOffice(OfficeDO office) {
        return officeDOMapper.insertSelective(office) > 0;
    }

    @Override
    public OfficeDO getOfficeById(String officeId) {
        return officeDOMapper.selectByPrimaryKey(officeId);
    }

    @Override
    public OfficeTreeDTO getOfficeTreeByOrgId(String orgId, String validFlag) {
        List<OfficeTreeDTO> result = new LinkedList<>();
        recursiveGen(result, officeDOMapper.selectAllByOrgId(orgId, validFlag), "");
        return result.get(0);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateOffice(OfficeDO office) {
        return officeDOMapper.updateByPrimaryKeySelective(office) > 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteOffice(OfficeDO office) {
        // 删除此组织架构
        if (officeDOMapper.updateByPrimaryKeySelective(office) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return true;
    }

    @Override
    public boolean checkOfficeName(String officeId, String orgId, String officeName) {
        return officeDOMapper.checkOfficeName(officeId, orgId, officeName) == null;
    }

    @Override
    public OfficeTreeDTO getOfficeTreeByUserId(String validFlag, String userId) {
        // 获取当前切换的机构
        OrgDO org = orgDOMapper.selectSwitchOnOrgByUserId(userId);
        return getOfficeTreeByOrgId(org.getId(), validFlag);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateValidFlag(String orgId, String validFlag, String userId) {
        OfficeDO office = new OfficeDO();
        office.setId(orgId);
        office.setValidFlag(validFlag);
        office.preUpdate(userId);
        return officeDOMapper.updateByPrimaryKeySelective(office) > 0;
    }

    @Override
    public List<OfficeDO> getByUserId(String userId) {
        // 获取当前切换的机构
        OrgDO org = orgDOMapper.selectSwitchOnOrgByUserId(userId);
        return officeDOMapper.selectByUserIdAndOrgId(org.getId(), userId);
    }


}
