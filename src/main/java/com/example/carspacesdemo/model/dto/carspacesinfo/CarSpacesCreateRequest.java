package com.example.carspacesdemo.model.dto.carspacesinfo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 * 车位信息创建请求体
 *
 * @author Rico
 */
@Data
public class CarSpacesCreateRequest implements Serializable {
    String location;
    int price;
    String imageUrl;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
