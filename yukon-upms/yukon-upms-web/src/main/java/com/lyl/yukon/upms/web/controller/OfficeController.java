package com.lyl.yukon.upms.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lyl.yukon.gateway.annotation.WriteLock;
import com.lyl.yukon.common.base.BaseController;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import com.lyl.yukon.common.constant.OfficeConstant;
import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.common.entity.upms.OfficeDO;
import com.lyl.yukon.common.entity.upms.OrgDO;
import com.lyl.yukon.upms.api.dto.OfficeTreeDTO;
import com.lyl.yukon.upms.api.service.IOfficeService;
import com.lyl.yukon.upms.api.service.IOrgService;
import com.lyl.yukon.upms.api.service.IUserService;
import com.lyl.yukon.upms.web.param.OfficeParam;
import com.lyl.yukon.upms.web.param.OrgParam;
import com.lyl.yukon.upms.web.vo.OfficeTreeVO;
import com.lyl.yukon.upms.web.vo.OfficeVO;
import com.lyl.yukon.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>组织架构 controller</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/17 16:08
 **/
@RestController
@CrossOrigin
@Api(tags = "组织架构controller")
@RequestMapping("/upms/office")
public class OfficeController extends BaseController {

    @Reference(version = "1.0.0")
    private IOfficeService officeService;
    @Reference(version = "1.0.0")
    private IUserService userService;
    @Reference(version = "1.0.0")
    private IOrgService orgService;

    @ApiOperation(value = "树结构组织架构")
    @PostMapping(value = "/tree")
    public RestResponseModel getOfficeTree(HttpServletRequest request, @RequestBody OrgParam param) {
        if (StringUtils.isAnyBlank(param.getDisplayFlag())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        OrgDO org = orgService.getSwitchOnOrgByUserId(getUserId(request));
        // 获取当前用户所在组织架构
        OfficeTreeDTO officeTree = officeService.getOfficeTreeByOrgId(org.getId(), param.getDisplayFlag().equals(SystemConstant.DISPLAY_FLAG_YES) ? null : SystemConstant.VALID_FLAG_YES);
        Map<Object, Object> result = new HashMap<>();
        result.put("officeTree", new OfficeTreeVO(officeTree));
        return success(result);
    }

    @WriteLock
    @ApiOperation(value = "新增组织架构")
    @PostMapping(value = "/insert")
    public RestResponseModel insert(HttpServletRequest request, @RequestBody @Valid OfficeParam param, BindingResult bindingResult) {
        // 通用参数校验
        if (StringUtils.isAnyBlank(param.getOfficeName(), param.getParentId(), param.getOfficeType())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        // 不能在组织架构管理新增机构
        if (param.getOfficeType().equals(OfficeConstant.TYPE_ORG)) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }
        // 校验参数格式
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }
        // 父节点是否存在
        OfficeDO parentOffice = officeService.getOfficeById(param.getParentId());
        if (parentOffice == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }
        if (!officeService.checkOfficeName(null, parentOffice.getRootId(), param.getOfficeName())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }
        if (parentOffice.getDepth() == OfficeConstant.MAX_DEPTH) {
            return error(EnumMsgCode.MSG_UPMS_111);
        }

        OfficeDO office = new OfficeDO();
        BeanUtils.copyProperties(param, office);
        office.setParentId(parentOffice.getId());
        // 如果父节点是机构，那么这个节点就是这个机构的根结点
        if (StringUtils.isBlank(parentOffice.getParentId())) {
            office.setParentIds(parentOffice.getId());
            office.setRootId(parentOffice.getId());
        } else {
            office.setParentIds(parentOffice.getParentIds() + "," + parentOffice.getId());
            office.setRootId(parentOffice.getRootId());
        }
        office.preInsert(getUserId(request));
        office.setDepth(parentOffice.getDepth() + 1);

        if (!officeService.insertOffice(office)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "组织架构详情")
    @PostMapping(value = "/detail")
    public RestResponseModel getOfficeDetail(@RequestBody OfficeParam param) {
        // 参数校验
        if (StringUtils.isBlank(param.getOfficeId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        OfficeDO office = officeService.getOfficeById(param.getOfficeId());
        if (office == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        String parentOrgName = (office.getDepth() == OfficeConstant.TOP_DEPTH) ? "" : officeService.getOfficeById(office.getParentId()).getOfficeName();
        Map<Object, Object> result = new HashMap<>();
        result.put("office", new OfficeVO(office, parentOrgName));
        return success(result);
    }

    @WriteLock
    @ApiOperation(value = "修改组织架构")
    @PostMapping(value = "/update")
    public RestResponseModel update(HttpServletRequest request, @RequestBody @Valid OfficeParam param, BindingResult bindingResult) {
        // 参数校验
        if (StringUtils.isBlank(param.getOfficeId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }
        OfficeDO office = officeService.getOfficeById(param.getOfficeId());
        if (office == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        if (!officeService.checkOfficeName(param.getOfficeId(), office.getRootId(), param.getOfficeName())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }
        String userId = getUserId(request);

        BeanUtils.copyProperties(param, office);
        office.preUpdate(userId);
        if (!officeService.updateOffice(office)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "更新组织架构生效状态")
    @PostMapping(value = "/validFlag/update")
    public RestResponseModel updateValidFlag(HttpServletRequest request, @RequestBody OfficeParam param) {
        if (StringUtils.isAnyBlank(param.getOfficeId(), param.getValidFlag())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (officeService.getOfficeById(param.getOfficeId()) == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        if (!officeService.updateValidFlag(param.getOfficeId(), param.getValidFlag(), getUserId(request))) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @WriteLock
    @ApiOperation(value = "删除组织架构")
    @PostMapping(value = "/delete")
    public RestResponseModel delete(HttpServletRequest request, @RequestBody OfficeParam param) {
        // 参数校验
        if (StringUtils.isBlank(param.getOfficeId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        // 如果有子节点则无法删除
        if (officeService.getOfficeTreeByOrgId(param.getOfficeId(), null).getChildren().isEmpty()) {
            return error(EnumMsgCode.MSG_SYSTEM_1009);
        }

        OfficeDO office = officeService.getOfficeById(param.getOfficeId());
        if (office == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        // 防止删除机构节点
        if (office.getOfficeType().equals(OfficeConstant.TYPE_ORG)) {
            return error(EnumMsgCode.MSG_SYSTEM_1009);
        }

        office.preUpdate(getUserId(request));
        office.setDelFlag(SystemConstant.DEL_FLAG_YES);
        if (!officeService.deleteOffice(office)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }


}
