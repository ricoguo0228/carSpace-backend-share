package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.mapper.CarspaceMapper;
import com.example.carspacesdemo.mapper.IreserveMapper;
import com.example.carspacesdemo.mapper.ReservationMapper;
import com.example.carspacesdemo.mapper.UserMapper;
import com.example.carspacesdemo.model.entity.*;
import com.example.carspacesdemo.service.CarSpaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rico
 * @description 针对表【carspaces(用户创建的车位表)】的数据库操作Service实现
 * @createDate 2023-06-09 17:28:31
 */
@Service
public class CarSpaceServiceImpl extends ServiceImpl<CarspaceMapper, Carspace> implements CarSpaceService {

    @Resource
    CarspaceMapper carSpaceMapper;
    @Resource
    ReservationMapper reservationMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    IreserveMapper ireserveMapper;

    @Override
    public long carSpaceCreate(long userId, String location, int price, String imageUrl, Map<LocalDateTime,LocalDateTime> TimeSlots) {
        if (StringUtils.isAnyBlank(location)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "位置信息不能为空");
        }
        if (price < 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "价格不能小于0");
        }
        //保存基础车位信息到车位表中
        Carspace carSpace = new Carspace();
        carSpace.setLocation(location);
        carSpace.setPrice(price);
        carSpace.setImageUrl(imageUrl);
        carSpace.setOwnerId(userId);
        boolean saveResult = this.save(carSpace);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存失败了");
        }
        //保存车位可用时间段信息
        if(!TimeSlots.isEmpty()){
            for(LocalDateTime localDateTime : TimeSlots.keySet()){
                Ireserve ireserve = new Ireserve();
                ireserve.setCarId(carSpace.getCarId());
                ireserve.setStartTime(localDateTime);
                ireserve.setEndTime(TimeSlots.get(localDateTime));
                int i = ireserveMapper.insert(ireserve);
                if(i == 0){
                    throw new BusinessException(ErrorCode.DAO_ERROR);
                }
            }
        }
        return carSpace.getCarId();
    }

    @Override
    public boolean carSpaceUpdate(long carId, String location, int price, String imageUrl, Map<LocalDateTime,LocalDateTime> TimeSlots) {
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        //保存基础车位信息到车位表中
        Carspace carSpace = carSpaceMapper.selectById(carId);
        carSpace.setLocation(location);
        carSpace.setPrice(price);
        carSpace.setImageUrl(imageUrl);
        boolean updateResult = this.updateById(carSpace);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败了");
        }
        //保存车位可用时间段信息
        if(!TimeSlots.isEmpty()){
            for(LocalDateTime localDateTime : TimeSlots.keySet()){
                Ireserve ireserve = new Ireserve();
                ireserve.setCarId(carSpace.getCarId());
                ireserve.setStartTime(localDateTime);
                ireserve.setEndTime(TimeSlots.get(localDateTime));
                int i = ireserveMapper.insert(ireserve);
                if(i == 0){
                    throw new BusinessException(ErrorCode.DAO_ERROR);
                }
            }
        }
        return true;
    }

    @Override
    public boolean carSpacePublish(long carId) {
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车位ID不能为空");
        }
        Carspace carSpace = carSpaceMapper.selectById(carId);
        carSpace.setCarStatus(1);
        boolean updateResult = this.updateById(carSpace);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发布失败了");
        }
        return updateResult;
    }

    @Override
    public boolean carSpaceInvoke(long carId) {
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车位ID不能为空");
        }
        Carspace carSpace = carSpaceMapper.selectById(carId);
        carSpace.setCarStatus(0);
        boolean updateResult = this.updateById(carSpace);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发布失败了");
        }
        return updateResult;
    }

    @Override
    public List<ComplCarspace> getCarSpaces() {
        List<Carspace> carspaces = carSpaceMapper.selectList(new QueryWrapper<>());
        return getComplCarspacesByList(carspaces);
    }

    @Override
    public List<ComplCarspace> getUserCarSpaces(long ownerId) {
        QueryWrapper<Carspace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", ownerId);
        List<Carspace> carspaces = carSpaceMapper.selectList(queryWrapper);
        return getComplCarspacesByList(carspaces);
    }

    @Override
    public List<ComplCarspace> getReservedCarSpaces(long reserverId) {
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reserver_id", reserverId);
        List<Reservation> reservations = reservationMapper.selectList(queryWrapper);
        List<Long> CarIds = new ArrayList<>();
        for (Reservation reservation : reservations) {
            CarIds.add(reservation.getCarId());
        }
        return getComplCarspacesByCarIds(CarIds);
    }

    @Override
    public List<ComplCarspace> getComplCarspacesByList(List<Carspace> carspaces) {
        List<ComplCarspace> complCarspaces = new ArrayList<>();
        for (Carspace carSpace : carspaces) {
            //筛选可预约的时间
            QueryWrapper<Ireserve> ireserveQueryWrapper = new QueryWrapper<Ireserve>();
            ireserveQueryWrapper.eq("car_id", carSpace.getCarId());
            List<Ireserve> ires = ireserveMapper.selectList(ireserveQueryWrapper);
            Map<LocalDateTime,LocalDateTime> availableSlots = new HashMap<>();
            for(Ireserve ire : ires) {
                availableSlots.put(ire.getStartTime(),ire.getEndTime());
            }
//            //筛选已经预约的时间
//            QueryWrapper<Reservation> queryWrapper = new QueryWrapper<Reservation>();
//            queryWrapper.eq("car_id", carSpace.getCarId());
//            queryWrapper.eq("reserve_status", 0);
//            List<Reservation> reservations = reservationMapper.selectList(queryWrapper);
//            //时间切分
//            Map<LocalDateTime, LocalDateTime> reserveSlots = new HashMap<LocalDateTime, LocalDateTime>();
//            int reserveStatus = -1;
//            for (Reservation reservation : reservations) {
//                reserveSlots.put(reservation.getReserveStartTime(), reservation.getReserveEndTime());
//                reserveStatus = reservation.getReserveStatus();
//            }
//            Map<LocalDateTime, LocalDateTime> availableSlots = new HashMap<>();
//            for (LocalDateTime left : reserveSlots.keySet()) {
//                if (left.isAfter(startTime) && left.isBefore(endTime)) {
//                    availableSlots.put(startTime, left);
//                }
//                if (reserveSlots.get(left).isAfter(startTime) && reserveSlots.get(left).isBefore(endTime)) {
//                    availableSlots.put(reserveSlots.get(left), endTime);
//                }
//            }
            User user = userMapper.selectById(carSpace.getOwnerId());
            complCarspaces.add(new ComplCarspace(carSpace, availableSlots, user));
        }
        return complCarspaces;
    }

    @Override
    public List<ComplCarspace> getComplCarspacesByCarIds(List<Long> CarIds) {
        List<ComplCarspace> complCarspaces = new ArrayList<>();
        for (long CarId : CarIds) {
            Carspace carSpace = carSpaceMapper.selectById(CarId);
            //筛选可预约的时间
            QueryWrapper<Ireserve> ireserveQueryWrapper = new QueryWrapper<Ireserve>();
            ireserveQueryWrapper.eq("car_id", carSpace.getCarId());
            List<Ireserve> ires = ireserveMapper.selectList(ireserveQueryWrapper);
            Map<LocalDateTime,LocalDateTime> availableSlots = new HashMap<>();
            for(Ireserve ire : ires) {
                availableSlots.put(ire.getStartTime(),ire.getEndTime());
            }
//            QueryWrapper<Reservation> queryWrapper = new QueryWrapper<Reservation>();
//            queryWrapper.eq("car_id", carSpace.getCarId());
//            queryWrapper.eq("reserve_status", 0);
//            List<Reservation> reservations = reservationMapper.selectList(queryWrapper);
//            Map<LocalDateTime, LocalDateTime> reserveSlots = new HashMap<LocalDateTime, LocalDateTime>();
//            int reserveStatus = -1;
//            for (Reservation reservation : reservations) {
//                reserveSlots.put(reservation.getReserveStartTime(), reservation.getReserveEndTime());
//                reserveStatus = reservation.getReserveStatus();
//            }
//            Map<LocalDateTime, LocalDateTime> availableSlots = new HashMap<>();
//            for (LocalDateTime left : reserveSlots.keySet()) {
//                if (left.isAfter(startTime) && left.isBefore(endTime)) {
//                    availableSlots.put(startTime, left);
//                }
//                if (reserveSlots.get(left).isAfter(startTime) && reserveSlots.get(left).isBefore(endTime)) {
//                    availableSlots.put(reserveSlots.get(left), endTime);
//                }
//            }
            User user = userMapper.selectById(carSpace.getOwnerId());
            complCarspaces.add(new ComplCarspace(carSpace, availableSlots, user));
        }
        return complCarspaces;
    }

}




