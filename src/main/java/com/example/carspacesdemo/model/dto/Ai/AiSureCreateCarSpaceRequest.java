package com.example.carspacesdemo.model.dto.Ai;

import lombok.Data;

import java.io.Serializable;

@Data
public class AiSureCreateCarSpaceRequest implements Serializable {
    String Location;
    int price;
}
