package com.example.carspacesdemo.model.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 车位信息创建请求体
 *
 * @author Rico
 */
@Data
public class ReservationAddRequest implements Serializable {
    private static final long serialVersionUID = 77690550829374813L;
    long carId;
    List<LocalDateTime> timeSlots;
}
