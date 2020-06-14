package com.lyl.yukon.upms.provider.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyl.yukon.common.entity.upms.*;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.constant.RoleConstant;
import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.upms.api.service.IOrgService;
import com.lyl.yukon.upms.api.dto.OrgDetailDTO;
import com.lyl.yukon.upms.api.dto.OrgListDTO;
import com.lyl.yukon.upms.provider.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2019/08/05 16:12
 **/
@Service("orgService")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class OrgServiceImpl implements IOrgService {

    private static Logger logger = LoggerFactory.getLogger(OrgServiceImpl.class);

    @Autowired
    private OrgDOMapper orgDOMapper;
    @Autowired
    private OfficeDOMapper officeDOMapper;
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private MenuDOMapper menuDOMapper;
    @Autowired
    private UserMenuRelationDOMapper userMenuRelationDOMapper;
    @Autowired
    private UserOrgRelationDOMapper userOrgRelationDOMapper;
    @Autowired
    private UserRoleRelationDOMapper userRoleRelationDOMapper;

    /**
     * 生成用户菜单关联列表
     *
     * @param orgDO         机构信息
     * @param insertMenuIds 菜单 id
     */
    private List<UserMenuRelationDO> generateUserMenuRelationList(OrgDO orgDO, List<String> insertMenuIds) {
        List<UserMenuRelationDO> list = new LinkedList<>();
        for (String menuId : insertMenuIds) {
            UserMenuRelationDO relation = new UserMenuRelationDO();
            relation.preInsert(orgDO.getUpdateId());
            relation.setUserId(orgDO.getMasterId());
            relation.setMenuId(menuId);
            relation.setOrgId(orgDO.getId());
            list.add(relation);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean insertOrg(OrgDO org, OfficeDO office, UserDO user, List<String> menuIds) {
        if (orgDOMapper.insertSelective(org) < 1) {
            return false;
        }
        if (officeDOMapper.insertSelective(office) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        if (userDOMapper.insertSelective(user) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        // 插入管理员机构关联
        UserOrgRelationDO userOrgRelation = new UserOrgRelationDO();
        userOrgRelation.preInsert(org.getCreateId());
        userOrgRelation.setOrgId(org.getId());
        userOrgRelation.setUserId(user.getId());
        if (userOrgRelationDOMapper.insertSelective(userOrgRelation) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        // 插入管理员角色关联
        UserRoleRelationDO userRoleRelation = new UserRoleRelationDO();
        userRoleRelation.preInsert(org.getCreateId());
        userRoleRelation.setUserId(user.getId());
        userRoleRelation.setRoleId(RoleConstant.ORG_MANAGER_ID);
        if (userRoleRelationDOMapper.insertSelective(userRoleRelation) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        // 插入管理员菜单关联
        List<UserMenuRelationDO> insertList = generateUserMenuRelationList(org, menuIds);
        if (userMenuRelationDOMapper.insertBatch(insertList) < insertList.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return true;
    }

    @Override
    public PageInfo<OrgListDTO> getOrgPage(int pageNum, int pageSize, String validFlag) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(orgDOMapper.selectPage(validFlag));
    }

    @Override
    public OrgDO getById(String orgId) {
        return orgDOMapper.selectByPrimaryKey(orgId);
    }

    @Override
    public OrgDetailDTO getFullInfoById(String orgId) {
        return orgDOMapper.selectDetailById(orgId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateOrg(OrgDetailDTO org, String userId, List<String> menuIds) {
        // 更新组织架构信息
        OfficeDO office = officeDOMapper.selectByPrimaryKey(org.getId());
        BeanUtils.copyProperties(org, office);
        office.preUpdate(userId);
        if (officeDOMapper.updateByPrimaryKeySelective(office) < 1) {
            return false;
        }
        // 更新机构信息
        OrgDO orgDO = orgDOMapper.selectByPrimaryKey(org.getId());
        orgDO.setProvinceCode(org.getProvinceCode());
        orgDO.setCityCode(org.getCityCode());
        orgDO.setAreaCode(org.getAreaCode());
        orgDO.setStreetCode(org.getStreetCode());
        orgDO.setAddress(org.getAddress());
        orgDO.preUpdate(userId);
        if (orgDOMapper.updateByPrimaryKeySelective(orgDO) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }

        // 更新管理员菜单关联
        // 获取原有的菜单
        List<MenuDO> menuList = menuDOMapper.selectMenuListByUserIdAndOrgId(orgDO.getMasterId(), orgDO.getId());
        List<String> oriMenuIds = new LinkedList<>();
        menuList.forEach(menu -> oriMenuIds.add(menu.getId()));
        // 要删除的关联菜单 id
        List<String> deleteMenuIds = new LinkedList<>();
        for (String menuId : oriMenuIds) {
            if (!menuIds.contains(menuId)) {
                deleteMenuIds.add(menuId);
            }
        }
        if (!deleteMenuIds.isEmpty() && userMenuRelationDOMapper.deleteByUserIdAndMenuIds(orgDO.getMasterId(), orgDO.getId(), deleteMenuIds, userId, orgDO.getUpdateDate()) < deleteMenuIds.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);

        }
        // 要插入的关联菜单 id
        List<String> insertMenuIds = new LinkedList<>();
        for (String menuId : menuIds) {
            if (!oriMenuIds.contains(menuId)) {
                insertMenuIds.add(menuId);
            }
        }
        List<UserMenuRelationDO> insertList = generateUserMenuRelationList(orgDO, insertMenuIds);
        if (!insertList.isEmpty() && userMenuRelationDOMapper.insertBatch(insertList) < insertList.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteOrg(OrgDO org) {
        // 此机构管理员设置为失效
        UserDO master = userDOMapper.selectByPrimaryKey(org.getMasterId());
        master.preUpdate(org.getUpdateId());
        master.setValidFlag(SystemConstant.VALID_FLAG_NO);
        if (userDOMapper.updateByPrimaryKeySelective(master) < 1) {
            return false;
        }
        if (orgDOMapper.updateByPrimaryKeySelective(org) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        // 同时也要删除组织架构节点
        OfficeDO office = officeDOMapper.selectByPrimaryKey(org.getId());
        office.preUpdate(org.getUpdateId());
        office.setDelFlag(SystemConstant.DEL_FLAG_YES);
        if (officeDOMapper.updateByPrimaryKeySelective(office) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return true;
    }

    @Override
    public OrgDO getSwitchOnOrgByUserId(String userId) {
        return orgDOMapper.selectSwitchOnOrgByUserId(userId);
    }


}
