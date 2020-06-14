package com.lyl.yukon.upms.provider.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyl.yukon.common.entity.upms.*;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.constant.OfficeConstant;
import com.lyl.yukon.common.constant.RoleConstant;
import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.upms.api.service.IUserService;
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
 * @version 1.0 2019/04/09 18:25
 **/
@Service("userService")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private RoleDOMapper roleDOMapper;
    @Autowired
    private OrgDOMapper orgDOMapper;
    @Autowired
    private UserRoleRelationDOMapper userRoleRelationDOMapper;
    @Autowired
    private UserOrgRelationDOMapper userOrgRelationDOMapper;
    @Autowired
    private UserOfficeRelationDOMapper userOfficeRelationDOMapper;
    @Autowired
    private UserMenuRelationDOMapper userMenuRelationDOMapper;
    @Autowired
    private UserSchoolRelationDOMapper userSchoolRelationDOMapper;
    @Autowired
    private OfficeDOMapper officeDOMapper;
    @Autowired
    private MenuDOMapper menuDOMapper;

    /**
     * 批量生成用户组织架构关联列表
     *
     * @param user      用户信息
     * @param officeIds 组织架构 id
     */
    private List<UserOfficeRelationDO> generateUserOfficeRelationList(UserDO user, List<String> officeIds) {
        List<UserOfficeRelationDO> relationList = new LinkedList<>();
        for (String officeId : officeIds) {
            UserOfficeRelationDO relation = new UserOfficeRelationDO();
            relation.preInsert(user.getUpdateId());
            relation.setUserId(user.getId());
            relation.setOfficeId(officeId);
            relationList.add(relation);
        }
        return relationList;
    }

    /**
     * 批量生成用户角色关联列表
     *
     * @param user    用户信息
     * @param roleIds 角色 id
     */
    private List<UserRoleRelationDO> generateUserRoleRelationList(UserDO user, List<String> roleIds) {
        List<UserRoleRelationDO> relationList = new LinkedList<>();
        for (String roleId : roleIds) {
            UserRoleRelationDO relation = new UserRoleRelationDO();
            relation.preInsert(user.getUpdateId());
            relation.setUserId(user.getId());
            relation.setRoleId(roleId);
            relationList.add(relation);
        }
        return relationList;
    }

    /**
     * 批量生成用户校区关联列表
     *
     * @param user      用户信息
     * @param schoolIds 校区 id
     */
    private List<UserSchoolRelationDO> generateUserSchoolRelationList(UserDO user, List<String> schoolIds) {
        List<UserSchoolRelationDO> relationList = new LinkedList<>();
        for (String schoolId : schoolIds) {
            UserSchoolRelationDO relation = new UserSchoolRelationDO();
            relation.preInsert(user.getUpdateId());
            relation.setUserId(user.getId());
            relation.setSchoolId(schoolId);
            relationList.add(relation);
        }
        return relationList;
    }

    /**
     * 批量生成用户菜单关联列表
     *
     * @param user    用户信息
     * @param menuIds 菜单 id
     * @param orgId   机构 id
     */
    private List<UserMenuRelationDO> generateUserMenuRelationList(UserDO user, List<String> menuIds, String orgId) {
        List<UserMenuRelationDO> relationList = new LinkedList<>();
        for (String menuId : menuIds) {
            UserMenuRelationDO relation = new UserMenuRelationDO();
            relation.preInsert(user.getUpdateId());
            relation.setUserId(user.getId());
            relation.setMenuId(menuId);
            relation.setOrgId(orgId);
            relationList.add(relation);
        }
        return relationList;
    }

    @Override
    public UserDO getUserById(String userId) {
        return userDOMapper.selectByPrimaryKey(userId);
    }

    @Override
    public UserDO getUserByPhone(String phone) {
        return userDOMapper.selectByUserMobile(phone);
    }

    @Override
    public boolean modifyPassword(String mobile, String username, String password, String vCode) {
        return false;
    }

    @Override
    public PageInfo<UserDO> getUserPage(String userMobile, String userName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(userDOMapper.selectUserPage(userMobile, userName));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean insert(UserDO user, List<String> officeIds, List<String> roleIds, List<String> schoolIds, List<String> menuIds) {
        // TODO: 2019/9/3 调用用户中心

        if (userDOMapper.insertSelective(user) < 1) {
            return false;
        }
        // 查询当前机构
        OrgDO org = orgDOMapper.selectSwitchOnOrgByUserId(user.getCreateId());
        // 和机构建立关联
        UserOrgRelationDO userOrgRelation = new UserOrgRelationDO();
        userOrgRelation.preInsert(org.getCreateId());
        userOrgRelation.setUserId(user.getId());
        userOrgRelation.setOrgId(org.getId());
        if (userOrgRelationDOMapper.insertSelective(userOrgRelation) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }

        // 和 office 建立关联
        List<UserOfficeRelationDO> userOfficeRelations = generateUserOfficeRelationList(user, officeIds);
        if (userOfficeRelationDOMapper.insertBatch(userOfficeRelations) < userOfficeRelations.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }

        // 和角色建立关联
        List<UserRoleRelationDO> userRoleRelations = generateUserRoleRelationList(user, roleIds);
        if (userRoleRelationDOMapper.insertBatch(userRoleRelations) < userRoleRelations.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }

        // 和校区建立关联
        if (!schoolIds.isEmpty()) {
            List<UserSchoolRelationDO> userSchoolRelations = generateUserSchoolRelationList(user, schoolIds);
            if (userSchoolRelationDOMapper.insertBatch(userSchoolRelations) < userSchoolRelations.size()) {
                throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
            }
        }

        // 和菜单建立关联
        if (!menuIds.isEmpty()) {
            List<UserMenuRelationDO> userMenuRelations = generateUserMenuRelationList(user, menuIds, org.getId());
            if (userMenuRelationDOMapper.insertBatch(userMenuRelations) < userMenuRelations.size()) {
                throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean update(UserDO user, List<String> officeIds, List<String> roleIds, List<String> schoolIds, List<String> menuIds) {
        if (userDOMapper.updateByPrimaryKeySelective(user) < 1) {
            return false;
        }
        // 查询当前机构
        OrgDO org = orgDOMapper.selectSwitchOnOrgByUserId(user.getCreateId());

        // 组织架构相关
        // 获取原来所在的 office
        List<OfficeDO> oriOffices = officeDOMapper.selectByUserIdAndOrgId(user.getId(), org.getId());
        List<String> oriOfficeIds = new LinkedList<>();
        oriOffices.forEach(officeDO -> oriOfficeIds.add(officeDO.getId()));

        // 要删除的关联的office id
        List<String> deleteOfficeIds = new LinkedList<>();
        for (String officeId : oriOfficeIds) {
            if (!officeIds.contains(officeId)) {
                deleteOfficeIds.add(officeId);
            }
        }
        if (!deleteOfficeIds.isEmpty() && userOfficeRelationDOMapper.deleteByUserIdAndOfficeIds(user.getId(), deleteOfficeIds, user.getUpdateId(), user.getUpdateDate()) < deleteOfficeIds.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);

        }
        // 要插入的关联office id
        List<String> insertOfficeIds = new LinkedList<>();
        for (String officeId : officeIds) {
            if (!oriOfficeIds.contains(officeId)) {
                insertOfficeIds.add(officeId);
            }
        }
        List<UserOfficeRelationDO> insertUserOfficeList = generateUserOfficeRelationList(user, insertOfficeIds);
        if (!insertUserOfficeList.isEmpty() && userOfficeRelationDOMapper.insertBatch(insertUserOfficeList) < insertUserOfficeList.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }

        // 角色相关
        // 获取原来所拥有的角色
        List<RoleDO> oriRoles = roleDOMapper.selectByUserIdAndOrgId(user.getId(), org.getId());
        List<String> oriRoleIds = new LinkedList<>();
        for (RoleDO role : oriRoles) {
            // 通用角色除外
            if (role.getRoleType().equals(RoleConstant.TYPE_CUSTOM)) {
                oriRoleIds.add(role.getId());
            }
        }

        // 要删除的关联的角色 id
        List<String> deleteRoleIds = new LinkedList<>();
        for (String roleId : oriRoleIds) {
            if (!roleIds.contains(roleId)) {
                deleteRoleIds.add(roleId);
            }
        }
        if (!deleteRoleIds.isEmpty() && userRoleRelationDOMapper.deleteByUserIdAndRoleIds(user.getId(), deleteRoleIds, user.getUpdateId(), user.getUpdateDate()) < deleteRoleIds.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        // 要插入的关联角色 id
        List<String> insertRoleIds = new LinkedList<>();
        for (String roleId : roleIds) {
            if (!oriRoleIds.contains(roleId)) {
                insertRoleIds.add(roleId);
            }
        }
        List<UserRoleRelationDO> insertUserRoleList = generateUserRoleRelationList(user, insertRoleIds);
        if (!insertUserRoleList.isEmpty() && userRoleRelationDOMapper.insertBatch(insertUserRoleList) < insertUserRoleList.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }

        // 校区相关
        // 获取原来所拥有的校区
        List<UserSchoolRelationDO> oriSchools = userSchoolRelationDOMapper.selectByUserIdAndOrgId(user.getId(), org.getId());
        List<String> oriSchoolIds = new LinkedList<>();
        oriSchools.forEach(school -> oriSchoolIds.add(school.getId()));

        // 要删除的关联的角色 id
        List<String> deleteSchoolIds = new LinkedList<>();
        for (String schoolId : oriSchoolIds) {
            if (!roleIds.contains(schoolId)) {
                deleteSchoolIds.add(schoolId);
            }
        }
        if (!deleteSchoolIds.isEmpty() && userSchoolRelationDOMapper.deleteByUserIdAndSchoolIds(user.getId(), deleteSchoolIds, user.getUpdateId(), user.getUpdateDate()) < deleteRoleIds.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        // 要插入的关联校区 id
        List<String> insertSchoolIds = new LinkedList<>();
        for (String schoolId : schoolIds) {
            if (!oriRoleIds.contains(schoolId)) {
                insertRoleIds.add(schoolId);
            }
        }
        List<UserSchoolRelationDO> insertUserSchoolList = generateUserSchoolRelationList(user, insertSchoolIds);
        if (!insertUserSchoolList.isEmpty() && userSchoolRelationDOMapper.insertBatch(insertUserSchoolList) < insertUserSchoolList.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }

        // 菜单相关
        // 获取原来所拥有的菜单
        List<MenuDO> oriMenus = menuDOMapper.selectMenuListByUserIdAndOrgId(user.getId(), org.getId());
        List<String> oriMenuIds = new LinkedList<>();
        oriMenus.forEach(menuDO -> oriMenuIds.add(menuDO.getId()));

        // 要删除的关联的菜单 id
        List<String> deleteMenuIds = new LinkedList<>();
        for (String menuId : oriMenuIds) {
            if (!menuIds.contains(menuId)) {
                deleteMenuIds.add(menuId);
            }
        }
        if (!deleteMenuIds.isEmpty() && userMenuRelationDOMapper.deleteByUserIdAndMenuIds(user.getId(), org.getId(), deleteRoleIds, user.getUpdateId(), user.getUpdateDate()) < deleteRoleIds.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        // 要插入的关联角色 id
        List<String> insertMenuIds = new LinkedList<>();
        for (String menuId : menuIds) {
            if (!oriMenuIds.contains(menuId)) {
                insertMenuIds.add(menuId);
            }
        }
        List<UserMenuRelationDO> insertUserMenuList = generateUserMenuRelationList(user, insertMenuIds, org.getId());
        if (!insertUserMenuList.isEmpty() && userMenuRelationDOMapper.insertBatch(insertUserMenuList) < insertUserMenuList.size()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean update(UserDO user) {
        return userDOMapper.updateByPrimaryKeySelective(user) > 0;
    }

    @Override
    public boolean isPhoneExist(String phone) {
        return userDOMapper.selectByPhone(phone) != null;
    }

    @Override
    public boolean isAdmin(String userId) {
        List<RoleDO> roleList = roleDOMapper.selectByUserIdAndOrgId(userId, OfficeConstant.CC_ID);
        for (RoleDO role : roleList) {
            if (role.getValidFlag().equals(SystemConstant.VALID_FLAG_YES) && role.getId().equals(RoleConstant.ADMIN_ID)) {
                return true;
            }
        }
        return false;
    }

}
