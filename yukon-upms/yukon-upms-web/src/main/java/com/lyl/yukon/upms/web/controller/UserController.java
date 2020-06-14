package com.lyl.yukon.upms.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.lyl.yukon.common.entity.upms.*;
import com.lyl.yukon.gateway.annotation.WriteLock;
import com.lyl.yukon.common.base.BaseController;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import com.lyl.yukon.common.constant.OfficeConstant;
import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.common.constant.UserConstant;
import com.lyl.yukon.upms.api.service.*;
import com.lyl.yukon.upms.web.param.UserParam;
import com.lyl.yukon.upms.web.vo.*;
import com.lyl.yukon.common.util.StringUtils;
import com.lyl.yukon.common.util.upms.PwdUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>用户controller</p>
 *
 * @author liaoyl
 * @version 1.0 2019/4/15 17:40
 **/

@RestController
@CrossOrigin
@Api(tags = "用户controller")
@RequestMapping("/upms/user")
public class UserController extends BaseController {

    @Reference(version = "1.0.0")
    private IUserService userService;
    @Reference(version = "1.0.0")
    private IMenuService menuService;
    @Reference(version = "1.0.0")
    private IOrgService orgService;
    @Reference(version = "1.0.0")
    private IOfficeService officeService;
    @Reference(version = "1.0.0")
    private IRoleService roleService;

    @ApiOperation(value = "用户分页")
    @PostMapping(value = "/page")
    public RestResponseModel getUserPage(@RequestBody UserParam param) {
        Map<Object, Object> result = new HashMap<>();
        PageInfo userPage = userService.getUserPage(param.getPhone(), param.getUserName(), param.getPageNum(), param.getPageSize());
        userPage.setList(UserListVO.UserListVo(userPage.getList()));
        result.put("userPage", userPage);
        return success(result);
    }

    @ApiOperation(value = "检查手机号")
    @PostMapping(value = "/phone/check")
    public RestResponseModel checkPhone(@RequestBody UserDO param) {
        // 参数校验
        if (StringUtils.isAnyBlank(param.getPhone())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (!userService.isPhoneExist(param.getPhone())) {
            return error(EnumMsgCode.MSG_UPMS_110);
        }
        return success();
    }

    @ApiOperation(value = "新增、详情之前调用")
    @PostMapping(value = "/insert/prepare")
    public RestResponseModel insertPrepare(HttpServletRequest request) {
        Map<Object, Object> result = new HashMap<>();

        // 获取当前用户所在的机构
        OrgDO org = orgService.getSwitchOnOrgByUserId(getUserId(request));
        // cc 的用户可以选择所有菜单
        if (org.getId().equals(OfficeConstant.CC_ID)) {
            result.put("menuTree", MenuTreeVO.MenuTreeVO(new LinkedList<>(), menuService.getAllMenuTreeList(null)));
        } else {
            result.put("menuTree", MenuTreeVO.MenuTreeVO(new LinkedList<>(), menuService.getMenuTreeByUserId(getUserId(request))));
        }

        // 机构下角色列表
        result.put("roleList", RoleBriefVO.RoleListVo(roleService.getRoleByOrgId(org.getId())));

        // 组织架构树
        result.put("officeTree", new OfficeTreeVO(officeService.getOfficeTreeByOrgId(org.getId(), SystemConstant.VALID_FLAG_YES)));
        return success(result);
    }

    @WriteLock
    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/insert")
    public RestResponseModel insert(HttpServletRequest request, @RequestBody UserParam param) {
        // 参数校验
        if (StringUtils.isAnyBlank(param.getPhone(), param.getUserName(), param.getGender())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (userService.isPhoneExist(param.getPhone())) {
            return error(EnumMsgCode.MSG_UPMS_110);
        }
        if (param.getRoleIds() == null || param.getRoleIds().isEmpty()) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (param.getOfficeIds() == null || param.getSchoolIds() == null || param.getMenuIds() == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }

        UserDO user = new UserDO();
        BeanUtils.copyProperties(param, user);
        user.setPassword(PwdUtils.entryptPassword(param.getPassword()));
        user.preInsert(getUserId(request));
        if (!userService.insert(user, param.getOfficeIds(), param.getRoleIds(), param.getSchoolIds(), param.getMenuIds())) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @WriteLock
    @ApiOperation(value = "用户信息")
    @PostMapping(value = "/detail")
    public RestResponseModel getUserDetail(HttpServletRequest request, @RequestBody UserParam param) {
        // 参数校验
        if (StringUtils.isAnyBlank(param.getUserId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }

        UserDO user = userService.getUserById(param.getUserId());
        if (user == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        Map<Object, Object> result = new HashMap<>();
        result.put("user", new UserVO(user));

        // 获取用户的菜单
        List<MenuDO> menuList = menuService.getByUserId(user.getId());
        List<String> menuIdList = new LinkedList<>();
        menuList.forEach(menu -> menuIdList.add(menu.getId()));
        result.put("menuIdList", menuIdList);

        // 获取用户的角色
        List<RoleDO> roleList = roleService.getByUserId(user.getId());
        List<String> roleIdList = new LinkedList<>();
        roleList.forEach(role -> roleIdList.add(role.getId()));
        result.put("roleIdList", roleIdList);

        // 获取用户所在组织架构
        List<OfficeDO> officeList = officeService.getByUserId(user.getId());
        List<String> officeIdList = new LinkedList<>();
        officeList.forEach(office -> officeIdList.add(office.getId()));
        result.put("officeIdList", officeIdList);

        // 获取用户的校区权限，直接从所在组织架构中过滤
        List<String> schoolIdList = new LinkedList<>();
        for (OfficeDO officeDO : officeList) {
            if (officeDO.getOfficeType().equals(OfficeConstant.TYPE_SCHOOL)) {
                schoolIdList.add(officeDO.getId());
            }
        }
        result.put("schoolIdList", schoolIdList);
        return success(result);
    }

    @WriteLock
    @ApiOperation(value = "编辑用户")
    @PostMapping(value = "/update")
    public RestResponseModel update(HttpServletRequest request, @RequestBody UserParam param) {
        // 参数校验
        if (StringUtils.isAnyBlank(param.getUserId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        UserDO user = userService.getUserById(param.getUserId());
        if (user == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        if (param.getRoleIds() == null || param.getRoleIds().isEmpty()) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (param.getOfficeIds() == null || param.getSchoolIds() == null || param.getMenuIds() == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }

        BeanUtils.copyProperties(param, user);
        // 如果重置了密码
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(PwdUtils.entryptPassword(param.getPassword()));
        }
        user.preUpdate(getUserId(request));
        if (!userService.update(user, param.getOfficeIds(), param.getRoleIds(), param.getSchoolIds(), param.getMenuIds())) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "更新用户生效状态")
    @PostMapping(value = "/validFlag/update")
    public RestResponseModel updateValidFlag(HttpServletRequest request, @RequestBody UserParam param) {
        if (StringUtils.isAnyBlank(param.getUserId(), param.getValidFlag())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        UserDO user = userService.getUserById(param.getUserId());
        if (user == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        user.preUpdate(getUserId(request));
        user.setValidFlag(param.getValidFlag());
        if (!userService.update(user)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "重置密码")
    @PostMapping(value = "/password/reset")
    public RestResponseModel resetPassword(HttpServletRequest request, @RequestBody UserParam param) {
        if (StringUtils.isBlank(param.getUserId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        UserDO user = userService.getUserById(param.getUserId());
        if (user == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        user.preUpdate(getUserId(request));
        user.setPassword(PwdUtils.entryptPassword(UserConstant.DEFAULT_PASSWORD));
        if (!userService.update(user)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "删除用户")
    @PostMapping(value = "/delete")
    public RestResponseModel delete(HttpServletRequest request, @RequestBody UserParam param) {
        if (StringUtils.isBlank(param.getUserId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        UserDO user = userService.getUserById(param.getUserId());
        if (user == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        user.preUpdate(getUserId(request));
        user.setDelFlag(SystemConstant.DEL_FLAG_YES);
        if (!userService.update(user)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }


}
