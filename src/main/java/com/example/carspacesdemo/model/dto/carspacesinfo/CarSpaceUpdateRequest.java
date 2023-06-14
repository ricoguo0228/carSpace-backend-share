package com.example.carspacesdemo.model.dto.carspacesinfo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CarSpaceUpdateRequest implements Serializable {
    long carId;
    String location;
    int price;
    String imageUrl;
    Map<LocalDateTime,LocalDateTime> TimeSlots;
}
