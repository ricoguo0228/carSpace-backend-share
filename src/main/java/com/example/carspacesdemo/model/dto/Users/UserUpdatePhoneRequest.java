package com.example.carspacesdemo.model.dto.Users;

import lombok.Data;

/**
 * 用户更新手机号请求体
 */
@Data
public class UserUpdatePhoneRequest {
    String userAccount;
    String userPassword;
    String userNewPhone;
}