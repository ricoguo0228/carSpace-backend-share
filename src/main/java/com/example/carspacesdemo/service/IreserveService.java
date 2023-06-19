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
    /**
     * 插入一个可预约的时间段
     * @param carId
     * @param startTime
     * @param endTime
     * @return
     */
    boolean timeSlotInsert(long carId,LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 删除一个可预约的时间段
     * @param iId
     * @return
     */
    boolean timeSlotDelete(long iId);

    /**
     * 根据车位id获取这个车位的所有可预约时间段
     * @param carId
     * @return
     */
    List<Ireserve> listTimeSlots(long carId);

    /**
     * 时间段脱敏
     * @param iReserve
     * @return
     */
    Ireserve getSafetyIreserve(Ireserve iReserve);

}
