package com.example.carspacesdemo.model.dto.Users;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String userPassword;
    private String userNewPassword;
    private String userPhone;
    private String nickName;
}
