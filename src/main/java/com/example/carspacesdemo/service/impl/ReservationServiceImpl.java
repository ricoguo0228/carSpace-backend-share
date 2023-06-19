package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.mapper.ReservationMapper;
import com.example.carspacesdemo.model.entity.Reservation;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.ReservationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static com.example.carspacesdemo.constant.UserConstants.*;

/**
* @author Rico
* @description 针对表【reservation(预约表)】的数据库操作Service实现
* @createDate 2023-06-12 14:39:46
*/
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation>
    implements ReservationService {
    @Resource
    ReservationMapper reservationMapper;

    @Override
    public boolean addReservation(long reserverId, long carId, LocalDateTime reserveStartTime,LocalDateTime reserveEndTime) {
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位id不规范");
        }
        Reservation reservation = new Reservation();
        reservation.setReserverId(reserverId);
        reservation.setCarId(carId);
        reservation.setReserveStartTime(reserveStartTime);
        reservation.setReserveEndTime(reserveEndTime);
        reservationMapper.insert(reservation);
        return true;
    }

    @Override
    public boolean deleteReservation(long reserveId) {
        if(reserveId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位id不规范");
        }
        Reservation reservation = reservationMapper.selectById(reserveId);
        if(reservation == null){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"没有这个id对应的预约记录");
        }
        reservation.setReserveStatus(1);
        int res = reservationMapper.updateById(reservation);
        if(res == 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"失败");
        }
        return true;
    }
}




