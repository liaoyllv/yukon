package com.lyl.yukon.upms.provider.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyl.yukon.common.entity.upms.*;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.upms.api.service.IRoleService;
import com.lyl.yukon.upms.api.dto.RoleBriefDTO;
import com.lyl.yukon.upms.provider.dao.*;
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
@Service("roleService")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleDOMapper roleDOMapper;
    @Autowired
    private RoleMenuRelationDOMapper roleMenuRelationDOMapper;
    @Autowired
    private MenuDOMapper menuDOMapper;
    @Autowired
    private RoleSchoolRelationDOMapper roleSchoolRelationDOMapper;
    @Autowired
    private OrgDOMapper orgDOMapper;

    /**
     * 批量生成角色菜单关联列表
     *
     * @param role    角色信息
     * @param menuIds 菜单 id
     */
    private List<RoleMenuRelationDO> generateRoleMenuRelationList(RoleDO role, List<String> menuIds) {
        List<RoleMenuRelationDO> roleMenuList = new LinkedList<>();
        for (String menuId : menuIds) {
            RoleMenuRelationDO roleMenu = new RoleMenuRelationDO();
            roleMenu.preInsert(role.getUpdateId());
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        return roleMenuList;
    }

    /**
     * 批量生成角色校区关联列表
     *
     * @param role      角色信息
     * @param schoolIds 校区 id
     */
    private List<RoleSchoolRelationDO> generateRoleSchoolRelationList(RoleDO role, List<String> schoolIds) {
        List<RoleSchoolRelationDO> roleSchoolList = new LinkedList<>();
        for (String schoolId : schoolIds) {
            RoleSchoolRelationDO roleSchool = new RoleSchoolRelationDO();
            roleSchool.preInsert(role.getUpdateId());
            roleSchool.setRoleId(role.getId());
            roleSchool.setSchoolId(schoolId);
            roleSchoolList.add(roleSchool);
        }
        return roleSchoolList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean insertRole(RoleDO role, List<String> menuIds, List<String> schoolIds) {
        if (roleDOMapper.insertSelective(role) < 1) {
            return false;
        }
        if (!menuIds.isEmpty()) {
            // 批量插入角色菜单
            List<RoleMenuRelationDO> roleMenuList = generateRoleMenuRelationList(role, menuIds);
            if (roleMenuRelationDOMapper.insertBatch(roleMenuList) < roleMenuList.size()) {
                throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
            }
        }
        if (!schoolIds.isEmpty()) {
            // 批量插入角色校区
            List<RoleSchoolRelationDO> roleSchoolList = generateRoleSchoolRelationList(role, schoolIds);
            if (roleSchoolRelationDOMapper.insertBatch(roleSchoolList) < roleSchoolList.size()) {
                throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
            }
        }
        return true;
    }

    @Override
    public PageInfo<RoleDO> getRolePage(String orgId, int pageNum, int pageSize, String validFlag) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(roleDOMapper.selectPage(orgId, validFlag));
    }

    @Override
    public boolean checkRoleName(String roleId, String orgId, String roleName) {
        return roleDOMapper.checkRoleName(roleId, orgId, roleName) == null;
    }

    @Override
    public RoleDO getRoleById(String roleId) {
        return roleDOMapper.selectByPrimaryKey(roleId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateRole(RoleDO role, List<String> newMenuIds, List<String> newSchoolIds) {
        if (roleDOMapper.updateByPrimaryKeySelective(role) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        // 获取原有的菜单
        List<MenuDO> menuList = menuDOMapper.selectMenuListByRoleId(role.getId());
        List<String> oriMenuIds = new LinkedList<>();
        menuList.forEach(menu -> oriMenuIds.add(menu.getId()));
        // 要删除的关联菜单 id
        List<String> deleteMenuIds = new LinkedList<>();
        for (String menuId : oriMenuIds) {
            if (!newMenuIds.contains(menuId)) {
                deleteMenuIds.add(menuId);
            }
        }
        if (!deleteMenuIds.isEmpty() && roleMenuRelationDOMapper.deleteByRoleIdAndMenuIds(role.getId(), deleteMenuIds, role.getUpdateId(), role.getUpdateDate()) < deleteMenuIds.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);

        }
        // 要插入的关联菜单 id
        List<String> insertMenuIds = new LinkedList<>();
        for (String menuId : newMenuIds) {
            if (!oriMenuIds.contains(menuId)) {
                insertMenuIds.add(menuId);
            }
        }
        List<RoleMenuRelationDO> insertRoleMenuList = generateRoleMenuRelationList(role, insertMenuIds);
        if (!insertRoleMenuList.isEmpty() && roleMenuRelationDOMapper.insertBatch(insertRoleMenuList) < insertRoleMenuList.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }

        // 处理角色校区关联
        // 获取原有的校区
        List<RoleSchoolRelationDO> roleSchoolRelationList = roleSchoolRelationDOMapper.selectByRoleId(role.getId());
        List<String> oriSchoolIds = new LinkedList<>();
        roleSchoolRelationList.forEach(relation -> oriSchoolIds.add(relation.getSchoolId()));
        // 要删除的关联校区 id
        List<String> deleteSchoolIds = new LinkedList<>();
        for (String schoolId : oriSchoolIds) {
            if (!newSchoolIds.contains(schoolId)) {
                deleteSchoolIds.add(schoolId);
            }
        }
        if (!deleteSchoolIds.isEmpty() && roleSchoolRelationDOMapper.deleteByRoleIdAndSchoolIds(role.getId(), deleteSchoolIds, role.getUpdateId(), role.getUpdateDate()) < deleteSchoolIds.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);

        }
        // 要插入的关联的校区 id
        List<String> insertSchoolIds = new LinkedList<>();
        for (String schoolId : newSchoolIds) {
            if (!oriSchoolIds.contains(schoolId)) {
                insertSchoolIds.add(schoolId);
            }
        }
        List<RoleSchoolRelationDO> insertRoleSchoolList = generateRoleSchoolRelationList(role, insertSchoolIds);
        if (!insertRoleSchoolList.isEmpty() && roleSchoolRelationDOMapper.insertBatch(insertRoleSchoolList) < insertRoleSchoolList.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteRole(RoleDO role) throws CCWException {
        return roleDOMapper.updateByPrimaryKeySelective(role) > 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateRoleVladFlag(RoleDO role) {
        return roleDOMapper.updateByPrimaryKeySelective(role) > 0;
    }

    @Override
    public List<String> getSchoolIdListByRoleId(String roleId) {
        List<RoleSchoolRelationDO> relations = roleSchoolRelationDOMapper.selectByRoleId(roleId);
        LinkedList<String> schoolIds = new LinkedList<>();
        relations.forEach(relation -> schoolIds.add(relation.getSchoolId()));
        return schoolIds;
    }

    @Override
    public List<String> getMenuIdListByRoleId(String roleId) {
        List<RoleMenuRelationDO> relations = roleMenuRelationDOMapper.selectByRoleId(roleId);
        LinkedList<String> menuIds = new LinkedList<>();
        relations.forEach(relation -> menuIds.add(relation.getMenuId()));
        return menuIds;
    }

    @Override
    public List<RoleBriefDTO> getRoleByOrgId(String orgId) {
        List<RoleBriefDTO> roleList = roleDOMapper.selectBriefInfoByOrgId(orgId);
        for (RoleBriefDTO role : roleList) {
            // 获取角色的菜单 id
            List<String> menuIds = new LinkedList<>();
            roleMenuRelationDOMapper.selectByRoleId(role.getId()).forEach(relation -> menuIds.add(relation.getMenuId()));
            role.setMenuIds(menuIds);
            // 获取角色的校区 id
            List<String> schoolIds = new LinkedList<>();
            roleSchoolRelationDOMapper.selectByRoleId(role.getId()).forEach(relation -> menuIds.add(relation.getSchoolId()));
            role.setSchoolIds(schoolIds);
        }
        return roleList;
    }

    @Override
    public List<RoleDO> getByUserId(String userId) {
        // 查询当前机构
        OrgDO org = orgDOMapper.selectSwitchOnOrgByUserId(userId);
        return roleDOMapper.selectByUserIdAndOrgId(userId, org.getId());
    }

}
