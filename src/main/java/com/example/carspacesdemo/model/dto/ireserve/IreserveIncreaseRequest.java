package com.example.carspacesdemo.model.dto.ireserve;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约时间增加请求体
 */
@Data
public class IreserveIncreaseRequest implements Serializable {
    private static final long serialVersionUID = 77690550829374813L;
    long carId;

    List<LocalDateTime> timeSlots;
}
