package com.example.carspacesdemo.model.dto.carspacesinfo;

import com.example.carspacesdemo.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ListCarSpaceRequest  extends PageRequest implements Serializable{
    long id;
    String name;
    List<LocalDateTime> timeSlots;
    int startPrice;
    int endPrice;
}
