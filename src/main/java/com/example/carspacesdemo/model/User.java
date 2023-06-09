package com.example.carspacesdemo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

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
    private Integer isDelete;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 信息更新时间
     */
    private Date updateTime;

    /**
     * 昵称
     */
    private String nickName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}