package com.example.carspacesdemo.model.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ComplCarspace {
    private Carspace carspace;

    public ComplCarspace(Carspace carspace, Map<LocalDateTime, LocalDateTime> slots, User user, int reserveStatus) {
        this.carspace = carspace;
        this.slots = slots;
        this.phoneNumber = user.getUserPhone();
        this.ownerName = user.getUserAccount();
        this.reserveStatus = reserveStatus;
    }
    public ComplCarspace(Carspace carspace, Map<LocalDateTime, LocalDateTime> slots,User user) {
        this.carspace = carspace;
        this.slots = slots;
        this.phoneNumber = user.getUserPhone();
        this.ownerName = user.getUserAccount();
        this.reserveStatus = reserveStatus;
    }

    ComplCarspace() {

    }

    private String phoneNumber;
    private String ownerName;
    //用来区分可以预约的时间
    private Map<LocalDateTime, LocalDateTime> slots;
    //预约队列专用成员 0-正在预约 1-已经结束预约 -1-用不到
    private int reserveStatus;
}