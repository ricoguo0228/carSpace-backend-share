package com.example.carspacesdemo.common;

public enum ErrorCode {
    ERROR_PARAM(40000,"参数错误",""),
    LOGIN_ERROR(40001,"登录错误",""),
    NO_AUTH_ERROR(40002,"权限错误",""),
    SYSTEM_ERROR(50000,"系统内部错误",""),
    DAO_ERROR(40003,"数据库操作错误",""),
    ;
    private int code;
    private String message;
    private String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
