package com.example.carspacesdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.carspacesdemo.model.entity.Ireserve;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author Rico
* @description 针对表【ireserve】的数据库操作Service
* @createDate 2023-06-14 21:12:51
*/
public interface IreserveService extends IService<Ireserve> {
    boolean timeSlotInsert(long carId,LocalDateTime startTime, LocalDateTime endTime);

    boolean timeSlotDelete(long iId);
    List<Ireserve> listTimeSlots(long carId);
    Ireserve getSafetyIreserve(Ireserve iReserve);
}
