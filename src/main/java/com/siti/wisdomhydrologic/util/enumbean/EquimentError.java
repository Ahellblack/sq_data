package com.siti.wisdomhydrologic.util.enumbean;

import com.siti.wisdomhydrologic.util.ExceptionUtil;

/**
 * @author DC
 */
public enum EquimentError  {
	//data_15
	WS_AI("eq_1"),
	WS_WD("eq_2"),
	ELE_ERROR("wh003");
	private String errorCode;

	EquimentError(String errorCode){
		this.errorCode = errorCode;

	}
	public String getErrorCode() {
		return errorCode;
	}


}
