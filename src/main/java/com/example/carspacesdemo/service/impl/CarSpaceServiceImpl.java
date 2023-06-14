package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.mapper.CarspaceMapper;
import com.example.carspacesdemo.mapper.ReservationMapper;
import com.example.carspacesdemo.mapper.UserMapper;
import com.example.carspacesdemo.model.entity.Carspace;
import com.example.carspacesdemo.model.entity.ComplCarspace;
import com.example.carspacesdemo.model.entity.Reservation;
import com.example.carspacesdemo.model.entity.User;
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
public class CarSpaceServiceImpl extends ServiceImpl<CarspaceMapper, Carspace>  implements CarSpaceService {

    @Resource
    CarspaceMapper carSpacesMapper;
    @Resource
    ReservationMapper reservationMapper;
    @Resource
    UserMapper userMapper;
    @Override
    public long carSpaceCreate(long userId,String location, int price, String imageUrl, LocalDateTime startTime, LocalDateTime endTime) {
        if(StringUtils.isAnyBlank(location)){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"位置信息不能为空");
        }
        if(price < 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"价格不能小于0");
        }

        Carspace carSpace = new Carspace();
        carSpace.setLocation(location);
        carSpace.setPrice(price);
        carSpace.setImageUrl(imageUrl);
        carSpace.setStartTime(startTime);
        carSpace.setEndTime(endTime);
        carSpace.setOwnerId(userId);
        boolean saveResult = this.save(carSpace);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"保存失败了");
        }
        return carSpace.getCarId();
    }

    @Override
    public boolean carSpaceUpdate(long carId, String location, int price, String imageUrl, LocalDateTime startTime, LocalDateTime endTime) {
        if(StringUtils.isAnyBlank(location)){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"位置信息不能为空");
        }
        if(price < 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"价格不能小于0");
        }
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位ID不能为空");
        }
        Carspace carSpace = carSpacesMapper.selectById(carId);
        carSpace.setLocation(location);
        carSpace.setPrice(price);
        carSpace.setImageUrl(imageUrl);
        carSpace.setStartTime(startTime);
        carSpace.setEndTime(endTime);
        boolean updateResult = this.updateById(carSpace);
        if(!updateResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败了");
        }
        return true;
    }

    @Override
    public boolean carSpacePublish(long carId) {
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位ID不能为空");
        }
        Carspace carSpace = carSpacesMapper.selectById(carId);
        carSpace.setCarStatus(1);
        boolean updateResult = this.updateById(carSpace);
        if(!updateResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"发布失败了");
        }
        return updateResult;
    }

    @Override
    public boolean carSpaceInvoke(long carId) {
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位ID不能为空");
        }
        Carspace carSpace = carSpacesMapper.selectById(carId);
        carSpace.setCarStatus(0);
        boolean updateResult = this.updateById(carSpace);
        if(!updateResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"发布失败了");
        }
        return updateResult;
    }

    @Override
    public List<ComplCarspace> getCarSpaces() {
        List<Carspace> carspaces = carSpacesMapper.selectList(new QueryWrapper<>());
        return getComplCarspacesByList(carspaces);
    }

    @Override
    public List<ComplCarspace> getUserCarSpaces(long ownerId) {
        QueryWrapper<Carspace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id",ownerId);
        List<Carspace> carspaces = carSpacesMapper.selectList(queryWrapper);
        return getComplCarspacesByList(carspaces);
    }

    @Override
    public List<ComplCarspace> getReservedCarSpaces(long reserverId) {
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reserver_id",reserverId);
        List<Reservation> reservations = reservationMapper.selectList(queryWrapper);
        List<Long> CarIds = new ArrayList<>();
        for(Reservation reservation : reservations){
            CarIds.add(reservation.getCarId());
        }
        return getComplCarspacesByCarIds(CarIds);
    }

    @Override
    public List<ComplCarspace> getComplCarspacesByList(List<Carspace> carspaces) {
        List<ComplCarspace> complCarspaces = new ArrayList<>();
        for(Carspace carSpace : carspaces){
            LocalDateTime startTime = carSpace.getStartTime();
            LocalDateTime endTime = carSpace.getEndTime();
            QueryWrapper<Reservation> queryWrapper = new QueryWrapper<Reservation>();
            queryWrapper.eq("car_id",carSpace.getCarId());
            queryWrapper.eq("reserve_status",0);
            List<Reservation> reservations = reservationMapper.selectList(queryWrapper);
            Map<LocalDateTime,LocalDateTime> reserveSlots = new HashMap<LocalDateTime,LocalDateTime>();
            int reserveStatus = -1;
            for(Reservation reservation : reservations){
                reserveSlots.put(reservation.getReserveStartTime(),reservation.getReserveEndTime());
                reserveStatus = reservation.getReserveStatus();
            }
            Map<LocalDateTime, LocalDateTime> availableSlots = new HashMap<>();
            for(LocalDateTime left : reserveSlots.keySet()){
                if(left.isAfter(startTime) && left.isBefore(endTime)){
                    availableSlots.put(startTime, left);
                }
                if(reserveSlots.get(left).isAfter(startTime) && reserveSlots.get(left).isBefore(endTime)){
                    availableSlots.put(reserveSlots.get(left), endTime);
                }
            }
            User user = userMapper.selectById(carSpace.getOwnerId());
            complCarspaces.add(new ComplCarspace(carSpace,availableSlots,user,reserveStatus));
        }
        return complCarspaces;
    }

    @Override
    public List<ComplCarspace> getComplCarspacesByCarIds(List<Long> CarIds) {
        List<ComplCarspace> complCarspaces = new ArrayList<>();
        for(long CarId : CarIds){
            Carspace carSpace = carSpacesMapper.selectById(CarId);
            LocalDateTime startTime = carSpace.getStartTime();
            LocalDateTime endTime = carSpace.getEndTime();
            QueryWrapper<Reservation> queryWrapper = new QueryWrapper<Reservation>();
            queryWrapper.eq("car_id",carSpace.getCarId());
            queryWrapper.eq("reserve_status",0);
            List<Reservation> reservations = reservationMapper.selectList(queryWrapper);
            Map<LocalDateTime,LocalDateTime> reserveSlots = new HashMap<LocalDateTime,LocalDateTime>();
            int reserveStatus = -1;
            for(Reservation reservation : reservations){
                reserveSlots.put(reservation.getReserveStartTime(),reservation.getReserveEndTime());
                reserveStatus = reservation.getReserveStatus();
            }
            Map<LocalDateTime, LocalDateTime> availableSlots = new HashMap<>();
            for(LocalDateTime left : reserveSlots.keySet()){
                if(left.isAfter(startTime) && left.isBefore(endTime)){
                    availableSlots.put(startTime, left);
                }
                if(reserveSlots.get(left).isAfter(startTime) && reserveSlots.get(left).isBefore(endTime)){
                    availableSlots.put(reserveSlots.get(left), endTime);
                }
            }
            User user = userMapper.selectById(carSpace.getOwnerId());
            complCarspaces.add(new ComplCarspace(carSpace,availableSlots,user,reserveStatus));
        }
        return complCarspaces;
    }

}




