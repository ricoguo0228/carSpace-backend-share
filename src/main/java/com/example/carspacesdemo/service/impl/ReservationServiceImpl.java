package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.mapper.IreserveMapper;
import com.example.carspacesdemo.mapper.ReservationMapper;
import com.example.carspacesdemo.model.entity.Ireserve;
import com.example.carspacesdemo.model.entity.Reservation;
import com.example.carspacesdemo.service.IreserveService;
import com.example.carspacesdemo.service.ReservationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Resource
    IreserveMapper ireserveMapper;

    @Resource
    IreserveService ireserveService;

    @Override
    public boolean addReservation(long reserverId, long carId, LocalDateTime reserveStartTime, LocalDateTime reserveEndTime) {
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车位id不规范");
        }
        //校验是否符合可预约时段
        List<Ireserve> ireserves = ireserveService.listTimeSlots(carId);
        Ireserve ireserve = timeSlotSelect(ireserves, reserveStartTime, reserveEndTime);
        //进行可预约时段的更新
        ireserveMapper.deleteById(ireserve);
        Ireserve ireserve1 = new Ireserve();
        ireserve1.setCarId(carId);
        ireserve1.setStartTime(reserveStartTime);
        ireserve1.setEndTime(ireserve.getStartTime());
        Ireserve ireserve2 = new Ireserve();
        ireserve2.setCarId(carId);
        ireserve2.setStartTime(ireserve.getEndTime());
        ireserve2.setEndTime(reserveEndTime);
        int i1 = ireserveMapper.insert(ireserve1);
        int i2 = ireserveMapper.insert(ireserve2);
        if (i1 == 0 || i2 == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新可预约时间失败");
        }
        //插入预约数据
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
        if (reserveId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车位id不规范");
        }
        Reservation reservation = reservationMapper.selectById(reserveId);
        if (reservation == null) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "没有这个id对应的预约记录");
        }
        reservation.setReserveStatus(1);
        int res = reservationMapper.updateById(reservation);
        if (res == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "失败");
        }
        return true;
    }

    @Override
    public List<Reservation> currentReservations(long carId, long userId) {
        QueryWrapper<Reservation> query = new QueryWrapper<>();
        query.eq("reserver_id", userId);
        query.eq("car_id", carId);
        query.eq("reserve_status", 0);
        List<Reservation> reservations = reservationMapper.selectList(query);
        return reservations;
    }

    @Override
    public Ireserve timeSlotSelect(List<Ireserve> ireserves, LocalDateTime startTime, LocalDateTime endTime) {
        for (Ireserve ireserve : ireserves) {
            if (startTime.isAfter(ireserve.getStartTime())
                    && startTime.isBefore(ireserve.getEndTime())
                    && endTime.isBefore(ireserve.getEndTime())
                    && endTime.isAfter(ireserve.getStartTime())) {
                return ireserve;
            }
        }
        throw new BusinessException(ErrorCode.ERROR_PARAM, "没有符合的时间段");
    }
}




