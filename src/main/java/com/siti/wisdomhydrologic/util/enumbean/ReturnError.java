package com.siti.wisdomhydrologic.util.enumbean;

import com.siti.wisdomhydrologic.util.ExceptionUtil;

/**
 * @author DC
 */
public enum ReturnError implements ExceptionUtil.ErrorCode {
	QUEUE_FULL ("ORSY0002", "队列已满进入阻塞状态"),
	SYSTEM_ERROR ("ORSY0001", "系统异常");

	private String errorCode;
	private String errorMsg;

	ReturnError(String errorCode,String errorMsg){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	@Override
	public String getErrorCode() {		
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String getErrorMsg() {
		return errorMsg;
	}

}
