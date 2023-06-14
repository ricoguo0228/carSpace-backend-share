package com.example.carspacesdemo.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * @TableName ireserve
 */
@TableName(value ="ireserve")
@Data
public class Ireserve implements Serializable {
    /**
     * 车位id
     */
    private Long carId;

    /**
     * 车位可预约的开始时间
     */
    private LocalDateTime startTime;

    /**
     * 车位可预约的结束时间
     */
    private LocalDateTime endTime;

    /**
     * 数据插入时间
     */
    private LocalDateTime insertTime;

    /**
     * 是否删除 0-删除 1-未删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 数据更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}