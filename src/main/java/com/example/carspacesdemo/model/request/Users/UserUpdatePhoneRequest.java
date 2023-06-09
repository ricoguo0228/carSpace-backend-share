package com.example.carspacesdemo.model.request.Users;

import lombok.Data;

/**
 * 用户更新手机号请求体
 */
@Data
public class UserUpdatePhoneRequest {
    String username;
    String userPassword;
    String userNewPhone;
}
