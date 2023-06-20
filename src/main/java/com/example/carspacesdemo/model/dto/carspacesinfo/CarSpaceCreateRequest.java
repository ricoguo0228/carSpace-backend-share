package com.example.carspacesdemo.model.dto.carspacesinfo;

import com.fasterxml.jackson.annotation.JacksonInject;
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
public class CarSpaceCreateRequest implements Serializable {
    String location;
    int price;
    String imageUrl;

    List<LocalDateTime> timeSlots;
}
