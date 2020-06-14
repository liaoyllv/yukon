package com.lyl.yukon.upms.api.service;


import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.entity.upms.RoleDO;
import com.lyl.yukon.upms.api.dto.RoleBriefDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 角色service
 *
 * @author liaoyl
 */
public interface IRoleService {

    /**
     * 新增角色
     *
     * @param role      角色
     * @param menuIds   角色拥有的菜单id
     * @param schoolIds 查看的校区id列表
     * @return T|F
     */
    boolean insertRole(RoleDO role, List<String> menuIds, List<String> schoolIds);

    /**
     * 获取角色分页
     *
     * @param orgId     机构id
     * @param pageNum   页号
     * @param pageSize  页大小
     * @param validFlag 生效标记
     * @return 角色分页
     */
    PageInfo<RoleDO> getRolePage(String orgId, int pageNum, int pageSize, String validFlag);

    /**
     * 获取角色详情
     *
     * @param roleId 角色id
     * @return 角色
     */
    RoleDO getRoleById(String roleId);

    /**
     * 更新角色
     *
     * @param role      角色
     * @param menuIds   角色拥有的菜单id
     * @param schoolIds 角色关联校区id
     * @return T|F
     */
    boolean updateRole(RoleDO role, List<String> menuIds, List<String> schoolIds);

    /**
     * 删除角色
     *
     * @param role 角色
     * @return T|F
     */
    boolean deleteRole(RoleDO role) throws CCWException;

    /**
     * 校验角色名称是否存在
     *
     * @param orgId    机构 id
     * @param roleName 角色名称
     * @return T|F
     */
    boolean checkRoleName(String roleId, String orgId, String roleName);

    /**
     * 修改角色生效状态
     *
     * @param role 角色
     */
    boolean updateRoleVladFlag(RoleDO role);

    /**
     * 角色查看的校区 id 列表
     *
     * @param roleId 角色 id
     */
    List<String> getSchoolIdListByRoleId(String roleId);

    /**
     * 角色查看的菜单 id 列表
     *
     * @param roleId 角色 id
     */
    List<String> getMenuIdListByRoleId(String roleId);

    /**
     * 获取该机构下所有角色
     *
     * @param orgId 机构 id
     */
    List<RoleBriefDTO> getRoleByOrgId(String orgId);

    /**
     * 用户当前机构拥有的角色
     *
     * @param userId 用户 id
     */
    List<RoleDO> getByUserId(String userId);
}
