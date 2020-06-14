package com.lyl.yukon.upms.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.lyl.yukon.gateway.annotation.WriteLock;
import com.lyl.yukon.common.base.BaseController;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import com.lyl.yukon.common.constant.OfficeConstant;
import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.common.entity.upms.OrgDO;
import com.lyl.yukon.common.entity.upms.RoleDO;
import com.lyl.yukon.upms.api.service.IMenuService;
import com.lyl.yukon.upms.api.service.IOrgService;
import com.lyl.yukon.upms.api.service.IRoleService;
import com.lyl.yukon.upms.api.service.IUserService;
import com.lyl.yukon.upms.web.param.RoleParam;
import com.lyl.yukon.upms.web.vo.MenuTreeVO;
import com.lyl.yukon.upms.web.vo.RoleListVO;
import com.lyl.yukon.upms.web.vo.RoleVO;
import com.lyl.yukon.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * <p>角色controller</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/17 16:08
 **/
@RestController
@CrossOrigin
@Api(tags = "角色controller")
@RequestMapping("/upms/role")
public class RoleController extends BaseController {

    @Reference(version = "1.0.0")
    private IUserService userService;
    @Reference(version = "1.0.0")
    private IRoleService roleService;
    @Reference(version = "1.0.0")
    private IMenuService menuService;
    @Reference(version = "1.0.0")
    private IOrgService orgService;

    @ApiOperation(value = "角色分页")
    @PostMapping(value = "/page")
    public RestResponseModel getRolePage(HttpServletRequest request, @RequestBody RoleParam param) {
        if (StringUtils.isAnyBlank(param.getDisplayFlag())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        Map<Object, Object> result = new HashMap<>();

        OrgDO org = orgService.getSwitchOnOrgByUserId(getUserId(request));
        PageInfo rolePage = roleService.getRolePage(org.getId(), param.getPageNum(), param.getPageSize(), param.getDisplayFlag().equals(SystemConstant.DISPLAY_FLAG_YES) ? null : SystemConstant.VALID_FLAG_YES);
        rolePage.setList(RoleListVO.RoleListVo(rolePage.getList()));
        result.put("rolePage", rolePage);
        return success(result);
    }

    @ApiOperation(value = "新增、详情之前调用")
    @PostMapping(value = "/insert/prepare")
    public RestResponseModel insertPrepare(HttpServletRequest request) {
        Map<Object, Object> result = new HashMap<>();
        // 如果是 CC 的角色展示出所有菜单
        OrgDO org = orgService.getSwitchOnOrgByUserId(getUserId(request));
        if (org.getId().equals(OfficeConstant.CC_ID)) {
            result.put("menuTree", MenuTreeVO.MenuTreeVO(new LinkedList<>(), menuService.getAllMenuTreeList(null)));
        } else {
            // 展示当前机构管理员的菜单
            result.put("menuTree", MenuTreeVO.MenuTreeVO(new LinkedList<>(), menuService.getMenuTreeByUserId(org.getMasterId())));
        }

        return success(result);
    }

    @WriteLock
    @ApiOperation(value = "新增角色")
    @PostMapping(value = "/insert")
    public RestResponseModel insert(HttpServletRequest request, @RequestBody @Valid RoleParam param, BindingResult bindingResult) {
        // 参数校验
        if (StringUtils.isAnyBlank(param.getRoleName())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (param.getMenuIds() == null || param.getSchoolIds() == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }

        // 获取当前用户所在机构 id
        String userId = getUserId(request);
        OrgDO org = orgService.getSwitchOnOrgByUserId(userId);
        if (!roleService.checkRoleName(null, org.getId(), param.getRoleName())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }

        RoleDO role = new RoleDO();
        role.preInsert(userId);
        BeanUtils.copyProperties(param, role);
        if (!roleService.insertRole(role, param.getMenuIds(), param.getSchoolIds())) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "角色详情")
    @PostMapping(value = "/detail")
    public RestResponseModel getRoleDetail(HttpServletRequest request, @RequestBody RoleParam param) {
        // 参数校验
        if (StringUtils.isBlank(param.getRoleId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        RoleDO role = roleService.getRoleById(param.getRoleId());
        if (role == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        Map<Object, Object> result = new HashMap<>();
        result.put("role", new RoleVO(role));
        // 获取角色的菜单
        result.put("menuIdList", roleService.getMenuIdListByRoleId(param.getRoleId()));
        // 角色可查看的校区id列表
        result.put("schoolIdList", roleService.getSchoolIdListByRoleId(param.getRoleId()));
        return success(result);
    }

    @WriteLock
    @ApiOperation(value = "修改角色")
    @PostMapping(value = "/update")
    public RestResponseModel update(HttpServletRequest request, @RequestBody @Valid RoleParam param, BindingResult bindingResult) {
        // 参数校验
        if (StringUtils.isBlank(param.getRoleId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }
        RoleDO role = roleService.getRoleById(param.getRoleId());
        if (role == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        if (!roleService.checkRoleName(param.getRoleId(), "", param.getRoleName())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }

        role.preUpdate(getUserId(request));
        role.setId(param.getRoleId());
        role.setRoleName(param.getRoleName());
        role.setDescription(param.getDescription());
        role.setValidFlag(param.getValidFlag());
        if (!roleService.updateRole(role, param.getMenuIds(), param.getSchoolIds())) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @WriteLock
    @ApiOperation(value = "删除角色")
    @PostMapping(value = "/delete")
    public RestResponseModel update(HttpServletRequest request, @RequestBody @Valid RoleParam param) {
        // 参数校验
        if (StringUtils.isBlank(param.getRoleId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        RoleDO role = roleService.getRoleById(param.getRoleId());
        if (role == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }

        role.preUpdate(getUserId(request));
        role.setDelFlag(SystemConstant.DEL_FLAG_YES);
        if (!roleService.deleteRole(role)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @WriteLock
    @ApiOperation(value = "生效/失效")
    @PostMapping(value = "/validFlag/update")
    public RestResponseModel updateValidFlag(HttpServletRequest request, @RequestBody @Valid RoleParam param) {
        // 参数校验
        if (StringUtils.isBlank(param.getRoleId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        RoleDO role = roleService.getRoleById(param.getRoleId());
        if (role == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }

        role.preUpdate(getUserId(request));
        role.setValidFlag(param.getValidFlag());
        if (!roleService.updateRoleVladFlag(role)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }


}
