/**
 *
 */
package com.lyl.yukon.common.base;

import com.lyl.yukon.common.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


/**
 * Controller 的基类
 *
 * @author louxinhua
 */
public class BaseController {

    /**
     * logger类
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 业务处理成功
     */
    protected RestResponseModel success() {
        return createResultModel(true, EnumMsgCode.MSG_SUCCESS, "");
    }

    /**
     * 业务处理成功
     */
    protected RestResponseModel success(Object resultData) {
        return createResultModel(true, EnumMsgCode.MSG_SUCCESS, resultData);
    }

    /**
     * 业务失败
     */
    protected RestResponseModel error(EnumMsgCode code) {
        return createResultModel(false, code, null);
    }

    /**
     * 业务失败
     */
    protected RestResponseModel error() {
        return createResultModel(false, EnumMsgCode.MSG_ERROR, null);
    }

    /**
     * 创建返回给客户端的结果对象
     */
    private RestResponseModel createResultModel(Boolean isSuccess, EnumMsgCode code, Object resultData) {
        RestResponseModel result = new RestResponseModel();
        if (isSuccess == null || !isSuccess) {
            result.setSuccess(false);
            if (code == null) {
                result.setCode(EnumMsgCode.MSG_ERROR.getCode());
                result.setDescription(EnumMsgCode.MSG_ERROR.getDescription());
            } else {
                result.setCode(code.getCode());
                result.setDescription(code.getDescription());
            }
        } else {
            result.setSuccess(true);
            result.setCode(EnumMsgCode.MSG_SUCCESS.getCode());
            result.setDescription(EnumMsgCode.MSG_SUCCESS.getDescription());
        }
        if (resultData == null) {
            result.setResult("");
        } else {
            result.setResult(resultData);
        }
        return result;
    }

    /**
     * 获取请求的token
     */
    protected static String getToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring("Bearer ".length());
    }

    /**
     * 获取请求的用户id
     */
    protected static String getUserId(HttpServletRequest request) {
        return JwtUtils.getUserId(request.getHeader("Authorization").substring("Bearer ".length()));
    }

}
