package com.lyl.yukon.common.base; /**
 * 
 */


/**
 * API接口返回的实体类
 * @author louxinhua
 *
 */
public class RestResponseModel {

	private boolean isSuccess;

	private String code;

	private String description;

	private Object result;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	
	public void setResultInfo(EnumMsgCode code) {
		if ( code == null ) {
            this.code = EnumMsgCode.MSG_ERROR.getCode();
            this.description = EnumMsgCode.MSG_ERROR.getDescription();
		}
		else {
			this.code = code.getCode();
			this.description = code.getDescription();
		}
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
	
	
}
