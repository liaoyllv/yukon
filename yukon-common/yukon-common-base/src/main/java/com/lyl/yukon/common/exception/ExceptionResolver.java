/**
 *
 */
package com.lyl.yukon.common.exception;

import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.base.RestResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * @author louxinhua
 */
@ControllerAdvice
public class ExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    @ExceptionHandler(CCWException.class)
    @ResponseBody
    public RestResponseModel handlerCCWException(HttpServletRequest request, Exception ex) {
        CCWException tempEX = (CCWException) ex;
        EnumMsgCode code = tempEX.getResultMsgCode();
        if (code == null) {
            code = EnumMsgCode.MSG_ERROR;
        }
        RestResponseModel model = new RestResponseModel();
        model.setSuccess(false);
        model.setResultInfo(code);
        model.setResult("");
        logger.error("\npath : {}\n errorCode : {}\n msg:", request.getServletPath(), code, code.getDescription());
        return model;
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestResponseModel handlerException(HttpServletRequest request, Exception ex) {
        RestResponseModel model = new RestResponseModel();
        model.setSuccess(false);
        model.setResultInfo(EnumMsgCode.MSG_ERROR);
        model.setResult("");
        logger.error("\npath : {}\n", request.getServletPath(), ex);
        return model;
    }

}
