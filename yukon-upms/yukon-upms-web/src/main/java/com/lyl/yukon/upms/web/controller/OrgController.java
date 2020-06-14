package com.lyl.yukon.upms.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.lyl.yukon.gateway.annotation.WriteLock;
import com.lyl.yukon.common.base.BaseController;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import com.lyl.yukon.common.constant.RoleConstant;
import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.common.entity.BaseParam;
import com.lyl.yukon.common.entity.upms.MenuDO;
import com.lyl.yukon.common.entity.upms.OfficeDO;
import com.lyl.yukon.common.entity.upms.OrgDO;
import com.lyl.yukon.common.entity.upms.UserDO;
import com.lyl.yukon.upms.api.dto.OrgDetailDTO;
import com.lyl.yukon.upms.api.service.IMenuService;
import com.lyl.yukon.upms.api.service.IOfficeService;
import com.lyl.yukon.upms.api.service.IOrgService;
import com.lyl.yukon.upms.api.service.IUserService;
import com.lyl.yukon.upms.web.param.OrgParam;
import com.lyl.yukon.upms.web.vo.MenuTreeVO;
import com.lyl.yukon.upms.web.vo.OrgListVO;
import com.lyl.yukon.upms.web.vo.OrgVO;
import com.lyl.yukon.common.util.StringUtils;
import com.lyl.yukon.common.util.upms.PwdUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>机构 controller</p>
 *
 * @author liaoyl
 * @version 1.0 2019/08/05 17:14
 **/
@RestController
@CrossOrigin
@Api(tags = "机构controller")
@RequestMapping("/upms/org")
public class OrgController extends BaseController {

    @Reference(version = "1.0.0")
    private IUserService userService;
    @Reference(version = "1.0.0")
    private IOrgService orgService;
    @Reference(version = "1.0.0")
    private IOfficeService officeService;
    @Reference(version = "1.0.0")
    private IMenuService menuService;

    @ApiOperation(value = "新增、详情之前调用")
    @PostMapping(value = "/insert/prepare")
    public RestResponseModel insertPrepare() {
        Map<Object, Object> result = new HashMap<>();
        // 获取菜单树
        result.put("menuTree", MenuTreeVO.MenuTreeVO(new LinkedList<>(), menuService.getAllMenuTreeList(SystemConstant.CC_FLAG_NO)));
        // 机构管理员预设菜单
        List<MenuDO> menus = menuService.getByRoleId(RoleConstant.ORG_MANAGER_ID);
        List<String> menuIds = new LinkedList<>();
        menus.forEach(menu -> menuIds.add(menu.getId()));
        result.put("managerMenuIdList", menuIds);
        return success(result);
    }

    @WriteLock
    @ApiOperation(value = "新增机构")
    @PostMapping(value = "/insert")
    public RestResponseModel insert(HttpServletRequest request, @RequestBody @Valid OrgParam param, BindingResult bindingResult) {
        // 参数校验
        if (StringUtils.isAnyBlank(param.getOrgName(), param.getMasterName(), param.getMasterPhone(), param.getPassword(),
                param.getProvinceCode(), param.getCityCode(), param.getAreaCode(), param.getStreetCode(), param.getAddress())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (param.getMenuIds() == null || param.getMenuIds().isEmpty()) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (userService.isPhoneExist(param.getMasterPhone())) {
            return error(EnumMsgCode.MSG_UPMS_110);
        }

        // 校验参数格式
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }
        // 机构的名称就是组织架构的名称
        if (!officeService.checkOfficeName(null, null, param.getOrgName())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }

        String userId = getUserId(request);

        OfficeDO office = new OfficeDO();
        office.preInsert(userId);
        // 机构的根节点就是自己的 id
        office.setRootId(office.getId());
        office.setOfficeName(param.getOrgName());
        BeanUtils.copyProperties(param, office);

        UserDO user = new UserDO();
        user.preInsert(userId);
        user.setUserName(param.getMasterName());
        user.setRealName(param.getMasterName());
        user.setPassword(PwdUtils.entryptPassword(param.getPassword()));
        user.setPhone(param.getMasterPhone());
        // 这里创建的机构管理员和机构生效状态一致
        user.setValidFlag(param.getValidFlag());

        OrgDO org = new OrgDO();
        org.preInsert(userId);
        org.setMasterId(user.getId());
        // 和 officeId 保持一致
        org.setId(office.getId());
        org.setShortName(param.getOrgName());
        BeanUtils.copyProperties(param, org);

        if (!orgService.insertOrg(org, office, user, param.getMenuIds())) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "机构分页")
    @PostMapping(value = "/page")
    public RestResponseModel getOrgPage(@RequestBody BaseParam param) {
        if (StringUtils.isBlank(param.getDisplayFlag())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        Map<Object, Object> result = new HashMap<>();
        PageInfo orgPage = orgService.getOrgPage(param.getPageNum(), param.getPageSize(), param.getDisplayFlag().equals(SystemConstant.DISPLAY_FLAG_YES) ? null : SystemConstant.VALID_FLAG_YES);
        orgPage.setList(OrgListVO.OrgListVo(orgPage.getList()));
        result.put("orgPage", orgPage);
        return success(result);
    }

    @ApiOperation(value = "机构详情")
    @PostMapping(value = "/detail")
    public RestResponseModel getOrgDetail(@RequestBody OrgParam param) {
        if (StringUtils.isBlank(param.getOrgId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        OrgDetailDTO org = orgService.getFullInfoById(param.getOrgId());
        if (org == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }

        Map<Object, Object> result = new HashMap<>();
        result.put("org", new OrgVO(org));
        // 获取管理员的菜单
        LinkedList<String> menuIds = new LinkedList<>();
        menuService.getByUserId(org.getMasterId()).forEach(menu -> menuIds.add(menu.getId()));
        result.put("menuIdList", menuIds);
        return success(result);
    }

    @ApiOperation(value = "更新机构")
    @PostMapping(value = "/update")
    public RestResponseModel updateOrg(HttpServletRequest request, @RequestBody OrgParam param) {
        if (StringUtils.isBlank(param.getOrgId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        // 机构的名称就是组织架构的名称
        if (!officeService.checkOfficeName(param.getOrgId(), null, param.getOrgName())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }
        OrgDetailDTO org = orgService.getFullInfoById(param.getOrgId());
        if (org == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        BeanUtils.copyProperties(param, org);
        if (!orgService.updateOrg(org, getUserId(request), param.getMenuIds())) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "更新机构生效状态")
    @PostMapping(value = "/validFlag/update")
    public RestResponseModel updateValidFlag(HttpServletRequest request, @RequestBody OrgParam param) {
        if (StringUtils.isAnyBlank(param.getOrgId(), param.getValidFlag())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (orgService.getById(param.getOrgId()) == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        if (!officeService.updateValidFlag(param.getOrgId(), param.getValidFlag(), getUserId(request))) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "删除机构")
    @PostMapping(value = "/delete")
    public RestResponseModel deleteOrg(HttpServletRequest request, @RequestBody OrgParam param) {
        if (StringUtils.isBlank(param.getOrgId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        OrgDO org = orgService.getById(param.getOrgId());
        if (org == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        // admin才有权限
        String userId = getUserId(request);
        if (!userService.isAdmin(userId)) {
            return error(EnumMsgCode.MSG_UPMS_101);
        }
        // 如果有子节点则无法删除
        if (!officeService.getOfficeTreeByOrgId(org.getId(), null).getChildren().isEmpty()) {
            return error(EnumMsgCode.MSG_SYSTEM_1009);
        }

        org.setDelFlag(SystemConstant.DEL_FLAG_YES);
        org.preUpdate(userId);
        if (!orgService.deleteOrg(org)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

}
