package com.example.carspacesdemo.model.request.Users;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author Rico
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -150010351605082849L;
    public String username;
    public String userPassword;
    public String userCheckPassword;
    public String userPhone;
}