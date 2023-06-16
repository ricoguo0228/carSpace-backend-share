package com.example.carspacesdemo.model.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ComplCarspace {
    public ComplCarspace(Carspace carspace,List<Ireserve> ireseres,User user) {
        this.carspace = carspace;
        this.ireseres = ireseres;
        this.phoneNumber = user.getUserPhone();
        this.ownerName = user.getUserAccount();
    }
    public ComplCarspace(Carspace carspace,List<Ireserve> ireseres) {
        this.carspace = carspace;
        this.ireseres=ireseres;
    }
    private Carspace carspace;
    private List<Ireserve> ireseres;
    private String phoneNumber;
    private String ownerName;
}
