package com.example.carspacesdemo.common;

public class BaseResponse<T> {
    public int code;
    T data;
    public String message;
    public String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

}
