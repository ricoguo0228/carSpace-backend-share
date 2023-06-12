package com.example.carspacesdemo.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预约表
 * @TableName reservation
 */
@TableName(value ="reservation")
@Data
public class Reservation implements Serializable {
    /**
     * 预约id
     */
    @TableId
    private Long reserveId;

    /**
     * 预约者id
     */
    private Long reserverId;

    /**
     * 被预约车位的id
     */
    private Long carId;

    /**
     * 预约状态 0-正在预约 1-预约完成
     */
    private Integer reserveStatus;

    /**
     * 预约开始时间
     */
    private Date reserveStartTime;

    /**
     * 预约结束时间
     */
    private Date reserveEndTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}