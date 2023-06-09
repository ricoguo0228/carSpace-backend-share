package com.example.carspacesdemo.model.request.Users;

import lombok.Data;

/**
 * 用户修改密码请求体
 */
@Data
public class UserUpdatePasswordRequest {
    private String username;
    private String userNewPassword;
    private String userPhone;
}
