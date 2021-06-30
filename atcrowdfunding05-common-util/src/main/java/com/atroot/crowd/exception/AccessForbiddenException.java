package com.atroot.crowd.exception;

/**
 * Description:定义异常类---禁止访问异常
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.5.30 14:13
 */
public class AccessForbiddenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AccessForbiddenException() {
        super();
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }

    protected AccessForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
