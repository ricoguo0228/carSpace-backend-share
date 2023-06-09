package com.example.carspacesdemo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

public class ReservedCarspace {
    /**
     * 车位id
     */

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
    private Date insertTime;

    /**
     * 逻辑删除 0-未删除 1-已删除
     */
    private Integer isDelete;

    /**
     * 车位更新时间
     */
    private Date updateTime;

    /**
     * 车位预定的开始时间
     */
    private Date startTime;

    /**
     * 车位预定的结束时间
     */
    private Date endTime;

    /**
     * 车位拥有者
     */
    private String ownerName;
    /**
     * 车位拥有着的电话
     */
    private String ownerPhone;
}
