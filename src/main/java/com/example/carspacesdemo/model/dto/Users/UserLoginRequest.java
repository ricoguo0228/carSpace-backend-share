package com.example.carspacesdemo.model.dto.Users;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author Rico
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 77690550829374813L;
    public String userAccount;
    public String userPassword;
}
