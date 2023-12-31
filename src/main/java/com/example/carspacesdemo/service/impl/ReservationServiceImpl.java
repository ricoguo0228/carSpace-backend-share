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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public boolean addReservation(long reserverId, long carId, LocalDateTime reserveStartTime, LocalDateTime reserveEndTime,String carPass) {
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        //校验是否符合可预约时段
        List<Ireserve> ireserves = ireserveService.listTimeSlots(carId);
        Ireserve ireserve = timeSlotSelect(ireserves, reserveStartTime, reserveEndTime);
        //进行可预约时段的更新
        if (!saveSpiltIreserves(carId, ireserve, reserveStartTime, reserveEndTime)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        //插入预约数据
        Reservation reservation = new Reservation();
        reservation.setReserverId(reserverId);
        reservation.setCarId(carId);
        reservation.setReserveStartTime(reserveStartTime);
        reservation.setReserveEndTime(reserveEndTime);
        reservation.setCarPass(carPass);
        reservationMapper.insert(reservation);
        return true;
    }

    @Override
    public boolean deleteReservation(long reserveId) {
        if (reserveId <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        Reservation reservation = reservationMapper.selectById(reserveId);
        if (reservation == null) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "没有这个id对应的预约记录");
        }
        //修改预约状态
        reservation.setReserveStatus(1);
        int res = reservationMapper.updateById(reservation);
        if (res == 0) {
            throw new BusinessException(ErrorCode.DAO_ERROR, "数据库出现错误");
        }
        Long carId = reservation.getCarId();
        List<Ireserve> ireserves = ireserveService.listTimeSlots(carId);
        timeSlotsMerge(carId,ireserves,reservation.getReserveStartTime(),reservation.getReserveEndTime());
        return true;
    }

    @Override
    public List<Reservation> currentReservationsInReserve(long carId, long userId) {
        QueryWrapper<Reservation> query = new QueryWrapper<>();
        query.eq("reserver_id", userId);
        query.eq("car_id", carId);
        query.eq("reserve_status", 0);
        List<Reservation> reservations = reservationMapper.selectList(query);
        return reservations;
    }
    @Override
    public List<Reservation> currentReservationsInCreate(long carId) {
        QueryWrapper<Reservation> query = new QueryWrapper<>();
        query.eq("car_id", carId);
        query.eq("reserve_status", 0);
        List<Reservation> reservations = reservationMapper.selectList(query);
        return reservations;
    }

    @Override
    public boolean timeSlotsMerge(long carId,List<Ireserve> ireserves, LocalDateTime startTime, LocalDateTime endTime) {
        Map<Integer,Ireserve> map = new HashMap<>();
        for (Ireserve ireserve : ireserves) {
            if(startTime.isEqual(ireserve.getEndTime())){
                map.put(1,ireserve);
            }
            if(endTime.isEqual(ireserve.getStartTime())){
                map.put(2,ireserve);
            }
        }
        if(map.containsKey(1)&&map.containsKey(2)){
            Ireserve ireserve1 = map.get(1);
            Ireserve ireserve2 = map.get(2);
            ireserveMapper.deleteById(ireserve1);
            ireserveMapper.deleteById(ireserve2);
            Ireserve ireserve =new Ireserve();
            ireserve.setCarId(carId);
            ireserve.setStartTime(ireserve1.getStartTime());
            ireserve.setEndTime(ireserve2.getEndTime());
            ireserveMapper.insert(ireserve);
            return true;
        }
        if(map.containsKey(1)){
            Ireserve ireserve1 = map.get(1);
            ireserveMapper.deleteById(ireserve1);
            Ireserve ireserve =new Ireserve();
            ireserve.setCarId(carId);
            ireserve.setStartTime(ireserve1.getStartTime());
            ireserve.setEndTime(endTime);
            ireserveMapper.insert(ireserve);
            return true;
        }
        if(map.containsKey(2)){
            Ireserve ireserve2 = map.get(2);
            ireserveMapper.deleteById(ireserve2);
            Ireserve ireserve =new Ireserve();
            ireserve.setCarId(carId);
            ireserve.setStartTime(startTime);
            ireserve.setEndTime(ireserve2.getEndTime());
            ireserveMapper.insert(ireserve);
            return true;
        }
        throw new BusinessException(ErrorCode.ERROR_PARAM, "没有符合的时间段");
    }
    @Override
    public Ireserve timeSlotSelect(List<Ireserve> ireserves, LocalDateTime startTime, LocalDateTime endTime) {
        for (Ireserve ireserve : ireserves) {
            if (!((startTime.isAfter(ireserve.getStartTime()) || startTime.isEqual(ireserve.getStartTime())) && startTime.isBefore(ireserve.getEndTime())))
                continue;
            if (!((endTime.isEqual(ireserve.getEndTime()) || endTime.isAfter(ireserve.getStartTime())) && endTime.isBefore(ireserve.getEndTime())))
                continue;
            return ireserve;
        }
        throw new BusinessException(ErrorCode.ERROR_PARAM, "没有符合的时间段");
    }

    @Override
    public boolean saveSpiltIreserves(long carId, Ireserve ireserve, LocalDateTime startTime, LocalDateTime endTime) {
        ireserveMapper.deleteById(ireserve);
        if (!startTime.isEqual(ireserve.getStartTime())) {
            Ireserve ireserve1 = new Ireserve();
            ireserve1.setCarId(carId);
            ireserve1.setStartTime(ireserve.getStartTime());
            ireserve1.setEndTime(startTime);
            ireserveMapper.insert(ireserve1);
        }
        if (!endTime.isEqual(ireserve.getEndTime())) {
            Ireserve ireserve2 = new Ireserve();
            ireserve2.setCarId(carId);
            ireserve2.setStartTime(endTime);
            ireserve2.setEndTime(ireserve.getEndTime());
            ireserveMapper.insert(ireserve2);
        }
        return true;
    }
}




