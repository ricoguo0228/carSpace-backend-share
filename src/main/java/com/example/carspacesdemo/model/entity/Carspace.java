package com.example.carspacesdemo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户创建的车位表
 * @TableName carspace
 */
@TableName(value ="carspace")
@Data
public class Carspace implements Serializable {
    /**
     * 车位id
     */
    @TableId(type = IdType.AUTO)
    private Long carId;

    /**
     * 车位地点
     */
    private String location;

    /**
     * 车位出价（元/时）
     */
    private Integer price;

    /**
     * 车位图片地址
     */
    private String imageUrl;

    /**
     * 车辆状态 0-正常未发布 1-已经发布无人预定 2-已经被预定
     */
    private Integer carStatus;

    /**
     * 车位创建时间
     */
    private LocalDateTime insertTime;

    /**
     * 逻辑删除 0-未删除 1-已删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 车位更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 车位预定的开始时间
     */
    private LocalDateTime startTime;

    /**
     * 车位预定的结束时间
     */
    private LocalDateTime endTime;

    /**
     * 车位拥有者
     */
    private Long ownerId;

    /**
     * 被点击次数
     */
    private Integer countTime;

    /**
     * 被预约的次数
     */
    private Integer reserveTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}