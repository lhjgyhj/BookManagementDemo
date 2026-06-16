package com.lhj.bookmanagementdemo.exception;
//全局异常处理
public class ServiceException extends RuntimeException{
    private String code;//定义错误码
    public String getCode() {
        return code;
    }
    public ServiceException(String message,Throwable  cause) {
        super(message, cause);

    }
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }
}
