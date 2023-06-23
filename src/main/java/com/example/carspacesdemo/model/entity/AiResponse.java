package com.example.carspacesdemo.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AiResponse implements Serializable {
    public AiResponse(String location, int price, String str){
        this.location = location;
        this.price = price;
        this.str = str;
    }
    private String str;
    private String location;
    private int price;
}
