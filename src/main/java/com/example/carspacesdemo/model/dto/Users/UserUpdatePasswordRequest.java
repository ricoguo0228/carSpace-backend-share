package com.example.carspacesdemo.model.dto.Users;

import lombok.Data;

/**
 * 用户修改密码请求体
 */
@Data
public class UserUpdatePasswordRequest {
    private String userAccount;
    private String userNewPassword;
    private String userPhone;
}