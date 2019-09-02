package com.siti.wisdomhydrologic.util;

/**
 * Created by DC on 2019/6/18.
 *
 * @data ${DATA}-9:36
 */
public class TranFailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TranFailException(ExceptionUtil.ErrorCode errorCode, Throwable th) {
        super(errorCode.getErrorCode(), th);
    }

    public TranFailException(ExceptionUtil.ErrorCode errorCode) {
        super(errorCode.getErrorMsg());
    }

}
