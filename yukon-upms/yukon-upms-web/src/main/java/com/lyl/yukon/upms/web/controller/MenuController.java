package com.lyl.yukon.upms.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lyl.yukon.gateway.annotation.WriteLock;
import com.lyl.yukon.common.base.BaseController;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import com.lyl.yukon.common.constant.MenuConstant;
import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.common.entity.upms.MenuDO;
import com.lyl.yukon.upms.api.service.IMenuService;
import com.lyl.yukon.upms.api.service.IUserService;
import com.lyl.yukon.upms.web.param.MenuParam;
import com.lyl.yukon.upms.web.vo.MenuTreeListVO;
import com.lyl.yukon.upms.web.vo.MenuVO;
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
 * <p>菜单controller</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/17 16:08
 **/
@RestController
@CrossOrigin
@Api(tags = "菜单controller")
@RequestMapping("/upms/menu")
public class MenuController extends BaseController {

    @Reference(version = "1.0.0")
    private IMenuService menuService;
    @Reference(version = "1.0.0")
    private IUserService userService;

    @ApiOperation(value = "所有菜单")
    @PostMapping(value = "/all")
    public RestResponseModel getAllMenuTree() {
        Map<Object, Object> result = new HashMap<>();
        result.put("menuTreeList", MenuTreeListVO.MenuTreeVoList(new LinkedList<>(), menuService.getAllMenuTreeList(null), ""));
        return success(result);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping(value = "/insert")
    public RestResponseModel insert(HttpServletRequest request, @RequestBody @Valid MenuParam menuParam, BindingResult bindingResult) {
        // 参数校验
        if (StringUtils.isAnyBlank(menuParam.getMenuName(), menuParam.getMenuKey(), menuParam.getHref(), menuParam.getMenuType(), menuParam.getCcFlag())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }

        if (!menuService.checkMenuName(null, menuParam.getMenuName())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }
        if (!menuService.checkMenuKey(null, menuParam.getMenuKey())) {
            return error(EnumMsgCode.MSG_SYSTEM_1008);
        }

        MenuDO menu = new MenuDO();
        // 如果有父菜单
        if (StringUtils.isNotBlank(menuParam.getParentId())) {
            MenuDO parentMenu = menuService.getMenuById(menuParam.getParentId());
            if (parentMenu == null) {
                return error(EnumMsgCode.MSG_SYSTEM_1006);
            }
            if (parentMenu.getMenuType().equals(MenuConstant.TYPE_FUNCTION)) {
                return error(EnumMsgCode.MSG_UPMS_109);
            }
            // 如果父菜单是一级菜单
            if (StringUtils.isBlank(parentMenu.getParentId())) {
                menu.setParentIds(parentMenu.getId());
                // 如果父菜单是一级菜单那么 rootId 就是父菜单 id
                menu.setRootId(parentMenu.getId());
            } else {
                menu.setParentIds(parentMenu.getParentIds() + "," + parentMenu.getId());
                menu.setRootId(parentMenu.getRootId());
            }
        }

        BeanUtils.copyProperties(menuParam, menu);
        menu.preInsert(getUserId(request));
        if (!menuService.insertMenu(menu)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @ApiOperation(value = "菜单详情")
    @PostMapping(value = "/detail")
    public RestResponseModel getMenu(@RequestBody MenuParam param) {
        // 参数校验
        if (StringUtils.isBlank(param.getMenuId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        MenuDO menu = menuService.getMenuById(param.getMenuId());
        if (menu == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        Map<Object, Object> result = new HashMap<>();
        String parentMenuName = StringUtils.isBlank(menu.getParentId()) ? "" : menuService.getMenuById(menu.getParentId()).getMenuName();
        result.put("menu", new MenuVO(menu, parentMenuName));
        return success(result);
    }

    @WriteLock
    @ApiOperation(value = "修改菜单")
    @PostMapping(value = "/update")
    public RestResponseModel update(HttpServletRequest request, @RequestBody @Valid MenuParam param, BindingResult bindingResult) {
        // 参数校验
        if (StringUtils.isBlank(param.getMenuId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }

        MenuDO menu = menuService.getMenuById(param.getMenuId());
        if (menu == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        if (!menuService.checkMenuName(param.getMenuId(), param.getMenuName())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }
        if (!menuService.checkMenuKey(param.getMenuId(), param.getMenuKey())) {
            return error(EnumMsgCode.MSG_SYSTEM_1008);
        }
        menu.setId(param.getMenuId());
        menu.setMenuName(param.getMenuName());
        menu.setMenuKey(param.getMenuKey());
        menu.setHref(param.getHref());
        menu.setIcon(param.getIcon());
        menu.setCcFlag(param.getCcFlag());
        menu.setCcFlag(param.getCcFlag());
        menu.preUpdate(getUserId(request));
        if (!menuService.updateMenu(menu)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @WriteLock
    @ApiOperation(value = "删除菜单")
    @PostMapping(value = "/delete")
    public RestResponseModel update(HttpServletRequest request, @RequestBody @Valid MenuParam param) {
        // 参数校验
        if (StringUtils.isBlank(param.getMenuId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        MenuDO menu = menuService.getMenuById(param.getMenuId());
        if (menu == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        menu.preUpdate(getUserId(request));
        menu.setDelFlag(SystemConstant.DEL_FLAG_YES);
        if (!menuService.deleteMenu(menu)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @WriteLock
    @ApiOperation(value = "移动菜单")
    @PostMapping(value = "/move")
    public RestResponseModel move(@RequestBody @Valid MenuParam param) {
        // 参数校验
        if (StringUtils.isAnyBlank(param.getMenuId(), param.getDirection())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        MenuDO menu = menuService.getMenuById(param.getMenuId());
        if (menu == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        if (!menuService.move(menu.getId(), param.getDirection())) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

}
