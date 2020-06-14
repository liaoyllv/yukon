package com.lyl.yukon.upms.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lyl.yukon.gateway.annotation.Anon;
import com.lyl.yukon.common.base.BaseController;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import com.lyl.yukon.common.constant.SystemConstant;
import com.lyl.yukon.common.entity.upms.UserDO;
import com.lyl.yukon.upms.api.service.IOrgService;
import com.lyl.yukon.upms.api.service.IUserService;
import com.lyl.yukon.upms.web.param.UserParam;
import com.lyl.yukon.upms.web.vo.UserVO;
import com.lyl.yukon.common.util.JwtUtils;
import com.lyl.yukon.common.util.StringUtils;
import com.lyl.yukon.common.util.WebUtils;
import com.lyl.yukon.common.util.upms.PwdUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>系统controller</p>
 *
 * @author liaoyl
 * @version 1.0 2019/4/15 17:40
 **/

@RestController
@CrossOrigin
@Api(tags = "系统controller")
@RequestMapping("/upms/system")
public class SystemController extends BaseController {

    @Reference(version = "1.0.0")
    private IUserService userService;
    @Reference(version = "1.0.0")
    private IOrgService orgService;

    @Anon
    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataTypeClass = String.class, paramType = "body"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataTypeClass = String.class, paramType = "body"),
    })
    public RestResponseModel login(HttpServletRequest request, HttpServletResponse response, @RequestBody UserParam param){
        // 参数校验
        if (StringUtils.isAnyBlank(param.getPhone(), param.getPassword())) {
            return error(EnumMsgCode.MSG_SYSTEM_1001);
        }
        // 获取用户信息
        UserDO user = userService.getUserByPhone(param.getPhone());
        if (user == null) {
            return error(EnumMsgCode.MSG_UPMS_105);
        }
        if (user.getValidFlag().equals(SystemConstant.VALID_FLAG_NO)) {
            return error(EnumMsgCode.MSG_UPMS_112);
        }
        // 校验密码
        if (!PwdUtils.validatePassword(param.getPassword(), user.getPassword())) {
            return error(EnumMsgCode.MSG_UPMS_107);
        }
        // 更新用户信息到redis
//        redis.putString(RedisConstant.USER_LIST_PREFIX + user.getId(), JsonUtils.toJson(user));
        // 在响应头返回签名
        response.setHeader("Authorization", JwtUtils.sign(user.getId(), param.getTerminalType(), WebUtils.getRemoteAddr(request)));

        Map<Object, Object> result = new HashMap<>();

        result.put("userInfo", new UserVO(user));
        return success(result);
    }

    @Anon
    @ApiOperation(value = "退出登录")
    @PostMapping(value = "/logout")
    public RestResponseModel logout(HttpServletRequest request) {
//        redis.putString(RedisConstant.USER_LOGOUT_TOKEN_PREFIX + getToken(request), "", 2 * 60 * 60);
        return success();
    }


}
