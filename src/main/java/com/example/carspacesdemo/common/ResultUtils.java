package com.example.carspacesdemo.common;

public class ResultUtils {
    public static BaseResponse success(Object data) {
        return new BaseResponse(0, data, "成功", "");
    }

    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, message, description);
    }


    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse(errorCode.getCode(), errorCode.getMessage(), errorCode.getDescription());
    }

    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), errorCode.getMessage(), description);
    }

    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(), message, description);
    }
}
