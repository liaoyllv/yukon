package com.lyl.yukon.upms.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lyl.yukon.common.base.BaseController;
import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import com.lyl.yukon.upms.api.service.IMsgService;
import com.lyl.yukon.upms.web.param.MailParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/21 12:51 PM
 **/

@RestController
@CrossOrigin
@Api(tags = "邮件controller")
@RequestMapping("/mail")
public class MailController extends BaseController {

    @Reference(version = "1.0.0")
    private IMsgService msgService;

    @ApiOperation(value = "发送邮件")
    @PostMapping(value = "/send")
    public RestResponseModel sendMail(@RequestBody @Validated MailParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return error(EnumMsgCode.MSG_SYSTEM_1002);
        }
        Map<Object, Object> result = new HashMap<>();

        msgService.sendSimpleMailMsg(param.getTo(), param.getTitle(), param.getContent());
        return success(result);
    }
}
