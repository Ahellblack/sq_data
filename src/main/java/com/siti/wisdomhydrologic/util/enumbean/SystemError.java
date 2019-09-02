package com.siti.wisdomhydrologic.util.enumbean;

import com.siti.wisdomhydrologic.util.ExceptionUtil;

/**
 * @author DC
 */
public enum SystemError implements ExceptionUtil.ErrorCode {

	SENDLIST_NULL("SHSY0009","集合数据为空");

	private String errorCode;
	private String errorMsg;
	
	SystemError(String errorCode, String errorMsg){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[").append(errorCode);
		buffer.append("]").append(errorMsg);
		return buffer.toString();
	}

}
