package com.example.carspacesdemo.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图(脱敏）
 */
@Data
public class UserVO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 账户
     */
    private String userAccount;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户身份 0-普通用户 1-管理员
     */
    private Integer userRole;

    /**
     * 用户信用-百分制
     */
    private Integer userCredit;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 昵称
     */
    private String nickName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}