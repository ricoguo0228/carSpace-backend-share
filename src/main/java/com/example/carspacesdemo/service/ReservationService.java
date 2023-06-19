package com.example.carspacesdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.carspacesdemo.model.entity.Reservation;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


/**
* @author Rico
* @description 针对表【reservation(预约表)】的数据库操作Service
* @createDate 2023-06-12 14:39:46
*/
public interface ReservationService extends IService<Reservation> {
    boolean addReservation(long reserverId, long carId, LocalDateTime reserveStartTime, LocalDateTime reserveEndTime   );
    boolean deleteReservation( long reserveId);
}
