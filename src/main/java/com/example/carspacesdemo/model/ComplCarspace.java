package com.example.carspacesdemo.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ComplCarspace {
    private Carspace carspace;

    public ComplCarspace(Carspace carspace, Map<Date, Date> slots,User user,int reserveStatus) {
        this.carspace = carspace;
        this.slots = slots;
        this.phoneNumber = user.getUserPhone();
        this.ownerName = user.getUsername();
        this.reserveStatus = reserveStatus;
    }
    public ComplCarspace(Carspace carspace, Map<Date, Date> slots,User user) {
        this.carspace = carspace;
        this.slots = slots;
        this.phoneNumber = user.getUserPhone();
        this.ownerName = user.getUsername();
        this.reserveStatus = reserveStatus;
    }

    ComplCarspace() {

    }

    private String phoneNumber;
    private String ownerName;
    //用来区分可以预约的时间
    private Map<Date, Date> slots;
    //预约队列专用成员 0-正在预约 1-已经结束预约 -1-用不到
    private int reserveStatus;
}
