package com.lyl.yukon.common.exception;


import com.lyl.yukon.common.base.EnumMsgCode;

/**
 * CCW 业务异常类
 *
 * @author louxinhua
 */
public class CCWException extends RuntimeException {


	private static final long serialVersionUID = 1184306942479888629L;

	private EnumMsgCode resultMsgCode;

	public CCWException(EnumMsgCode msgCode) {
		if (msgCode == null) {
            this.resultMsgCode = EnumMsgCode.MSG_ERROR;
		} else {
			this.resultMsgCode = msgCode;
		}
	}

	public EnumMsgCode getResultMsgCode() {
		return resultMsgCode;
	}

}
