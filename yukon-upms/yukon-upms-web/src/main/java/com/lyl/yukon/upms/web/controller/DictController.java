package com.lyl.yukon.upms.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lyl.yukon.gateway.annotation.WriteLock;
import com.lyl.yukon.common.base.BaseController;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.common.entity.upms.DictDO;
import com.lyl.yukon.common.entity.upms.UserDO;
import com.lyl.yukon.upms.api.service.IDictService;
import com.lyl.yukon.upms.api.service.IUserService;
import com.lyl.yukon.upms.web.param.DictParam;
import com.lyl.yukon.upms.web.vo.DictListVO;
import com.lyl.yukon.upms.web.vo.DictVO;
import com.lyl.yukon.common.util.StringUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>字典 controller</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/25 16:08
 **/
@RestController
@CrossOrigin
@Api(tags = "字典controller")
@RequestMapping("/upms/dict")
public class DictController extends BaseController {

    @Reference(version = "1.0.0")
    private IDictService dictService;
    @Reference(version = "1.0.0")
    private IUserService userService;

    @ApiOperation(value = "字典分页")
    @PostMapping(value = "/page")
    public RestResponseModel getDictPage(@RequestBody DictParam param) {
        Map<Object, Object> result = new HashMap<>();
        PageInfo dictPage = dictService.getDictPage(param.getPageNum(), param.getPageSize(), param.getDictType());
        dictPage.setList(DictListVO.DictListVo(dictPage.getList()));
        result.put("dictPage", dictPage);
        return success(result);
    }

    @ApiOperation(value = "字典详情")
    @PostMapping(value = "/detail")
    public RestResponseModel getDict(@RequestBody DictParam param) {
        // 参数校验
        if (StringUtils.isBlank(param.getDictId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        DictDO dict = dictService.getDictById(param.getDictId());
        if (dict == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        Map<Object, Object> result = new HashMap<>();
        result.put("dict", new DictVO(dict));
        return success(result);
    }

    @WriteLock
    @ApiOperation(value = "新增字典")
    @PostMapping(value = "/insert")
    public RestResponseModel insert(HttpServletRequest request, @RequestBody @Valid DictDO dict, BindingResult bindingResult) {
        // 参数校验
        if (StringUtils.isAnyBlank(dict.getDictKey(), dict.getDictValue(), dict.getDictType())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }
        if (!dictService.checkDictKey(null, dict.getDictKey())) {
            return error(EnumMsgCode.MSG_SYSTEM_1008);
        }
        if (!dictService.checkDictValue(null, dict.getDictValue())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }

        UserDO user = userService.getUserById(getUserId(request));
        dict.setId(UUID.randomUUID().toString());
        dict.setCreateId(user.getId());
        dict.setCreateDate(new Date());
        dict.setUpdateId(user.getId());
        dict.setUpdateDate(new Date());
        if (!dictService.insertDict(dict)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @WriteLock
    @ApiOperation(value = "修改字典")
    @PostMapping(value = "/update")
    public RestResponseModel update(HttpServletRequest request, @RequestBody @Valid DictParam param, BindingResult bindingResult) {
        // 参数校验
        if (StringUtils.isBlank(param.getDictId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }
        DictDO dict = dictService.getDictById(param.getDictId());
        if (dict == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }
        if (!dictService.checkDictKey(dict.getId(), dict.getDictKey())) {
            return error(EnumMsgCode.MSG_SYSTEM_1008);
        }
        if (!dictService.checkDictValue(dict.getId(), dict.getDictValue())) {
            return error(EnumMsgCode.MSG_SYSTEM_1007);
        }

        UserDO user = userService.getUserById(getUserId(request));
        dict.setId(param.getDictId());
        dict.setDictKey(param.getDictKey());
        dict.setDictValue(param.getDictValue());
        dict.setDictType(param.getDictType());
        dict.setUpdateId(user.getId());
        dict.setUpdateDate(new Date());
        if (!dictService.updateDict(dict)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }

    @WriteLock
    @ApiOperation(value = "删除字典")
    @PostMapping(value = "/delete")
    public RestResponseModel delete(HttpServletRequest request, @RequestBody @Valid DictParam param) {
        // 参数校验
        if (StringUtils.isBlank(param.getDictId())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        DictDO dict = dictService.getDictById(param.getDictId());
        if (dict == null) {
            return error(EnumMsgCode.MSG_SYSTEM_1006);
        }

        UserDO user = userService.getUserById(getUserId(request));
        dict.setUpdateId(user.getId());
        dict.setUpdateDate(new Date());
        dict.setDelFlag(SystemConstant.DEL_FLAG_YES);
        if (!dictService.deleteDict(dict)) {
            return error(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return success();
    }


}
