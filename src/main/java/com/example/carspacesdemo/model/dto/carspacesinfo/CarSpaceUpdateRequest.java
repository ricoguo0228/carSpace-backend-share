package com.example.carspacesdemo.model.dto.carspacesinfo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CarSpaceUpdateRequest implements Serializable {
    long carId;
    String location;
    int price;
    String imageUrl;
    LocalDateTime startTime;
    LocalDateTime endTime;
}
