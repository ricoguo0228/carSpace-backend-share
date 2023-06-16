package com.example.carspacesdemo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 账户
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

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
     * 逻辑删除 0-未删除 1-已删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 插入时间
     */
    private LocalDateTime insertTime;

    /**
     * 信息更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String userAvatar;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}