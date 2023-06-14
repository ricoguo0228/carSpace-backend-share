package com.example.carspacesdemo.model.dto.Users;

import lombok.Data;

@Data
public class UserUpdateNickNameRequest {
    private String username;
    private String nickName;
}
