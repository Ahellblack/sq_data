package com.siti.wisdomhydrologic.util;

/**
 * Created by DC on 2019/6/18.
 *
 * @data ${DATA}-9:36
 */
public class ExceptionUtil {

	/**
	 * 错误信息定义
	 */
	public static interface ErrorCode {
		
		public String getErrorCode();
		
		public String getErrorMsg();
	}
	
	public static void throwException(ErrorCode errorCode){
		TranFailException ce = new TranFailException(errorCode);
		throw ce;
	}


	public static void throwException(ErrorCode errorCode, Throwable th){
		TranFailException ce = new TranFailException(errorCode,th);
		throw ce;
	}

}
