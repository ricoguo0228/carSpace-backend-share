package com.example.carspacesdemo.model.request.Users;

import lombok.Data;

@Data
public class UserUpdateNickNameRequest {
    private String username;
    private String nickName;
}
