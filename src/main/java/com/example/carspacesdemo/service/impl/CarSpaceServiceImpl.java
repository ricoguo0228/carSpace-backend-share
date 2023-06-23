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
import com.example.carspacesdemo.service.IreserveService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Resource
    IreserveService ireserveService;

    @Override
    public ComplCarspace getCurrentCarSpace(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        Carspace carspace = carSpaceMapper.selectById(id);
        QueryWrapper<Ireserve> query = new QueryWrapper<Ireserve>();
        query.eq("car_id", id);
        List<Ireserve> irs = ireserveMapper.selectList(query);
        User user = userMapper.selectById(carspace.getOwnerId());
        ComplCarspace complCarspace = new ComplCarspace(carspace, irs, user);
        return getSafetyComplCarSpace(complCarspace);
    }

    @Override
    public long carSpaceCreate(long userId, String location, int price, String imageUrl, LocalDateTime startTime, LocalDateTime endTime) {
        if (StringUtils.isAnyBlank(location)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "位置信息不能为空");
        }
        if (price < 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "价格不能小于0");
        }
        if (startTime.isAfter(endTime)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "开始时间不可以比结束时间晚");
        }
        //保存基础车位信息到车位表中
        Carspace carSpace = new Carspace();
        carSpace.setLocation(location);
        carSpace.setPrice(price);
        carSpace.setImageUrl(imageUrl);
        carSpace.setOwnerId(userId);
        boolean saveResult = this.save(carSpace);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.DAO_ERROR, "数据库出现错误");
        }
        //保存车位可用时间段信息
        Ireserve ireserve = new Ireserve();
        ireserve.setCarId(carSpace.getCarId());
        ireserve.setStartTime(startTime);
        ireserve.setEndTime(endTime);
        int i = ireserveMapper.insert(ireserve);
        if (i == 0) {
            throw new BusinessException(ErrorCode.DAO_ERROR, "数据库出现错误");
        }
        return carSpace.getCarId();
    }

    @Override
    public long AiCarSpaceCreate(long userId, String location, int price, String imageUrl) {
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
            throw new BusinessException(ErrorCode.DAO_ERROR, "数据库出现错误");
        }
        return carSpace.getCarId();
    }


    @Override
    public boolean carSpaceUpdate(long carId, String location, int price, String imageUrl) {
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        //保存基础车位信息到车位表中
        Carspace carSpace = carSpaceMapper.selectById(carId);
        carSpace.setLocation(location);
        carSpace.setPrice(price);
        carSpace.setImageUrl(imageUrl);
        boolean updateResult = this.updateById(carSpace);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        return true;
    }

    @Override
    public boolean carSpaceDelete(long carId) {
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        QueryWrapper<Reservation> reservationQueryWrapper = new QueryWrapper<>();
        reservationQueryWrapper.eq("car_id", carId);
        reservationQueryWrapper.eq("reserve_status", 0);
        Long count = reservationMapper.selectCount(reservationQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车位正在被预定，无法删除");
        }
        int carSpaceDeleteRes = carSpaceMapper.deleteById(carId);
        QueryWrapper<Ireserve> ireserveQueryWrapper = new QueryWrapper<>();
        ireserveQueryWrapper.eq("car_id", carId);
        ireserveMapper.delete(ireserveQueryWrapper);
        if (carSpaceDeleteRes == 0) {
            throw new BusinessException(ErrorCode.DAO_ERROR, "数据库出现错误");
        }
        return true;
    }

    @Override
    public boolean carSpacePublish(long carId) {
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        Carspace carSpace = carSpaceMapper.selectById(carId);
        carSpace.setCarStatus(1);
        boolean updateResult = this.updateById(carSpace);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        return updateResult;
    }

    @Override
    public boolean carSpaceInvoke(long carId) {
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        QueryWrapper<Reservation> reservation = new QueryWrapper<>();
        reservation.eq("car_id",carId);
        reservation.eq("reserve_status",0);
        Long count = reservationMapper.selectCount(reservation);
        if(count > 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆仍被预约");
        }
        Carspace carSpace = carSpaceMapper.selectById(carId);
        carSpace.setCarStatus(0);
        boolean updateResult = this.updateById(carSpace);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出现错误");
        }
        return updateResult;
    }

    @Override
    public List<ComplCarspace> listCarSpaces() {
        QueryWrapper<Carspace> carspaceQueryWrapper = new QueryWrapper<>();
        carspaceQueryWrapper.eq("car_status",1);
        List<Carspace> carspaces = carSpaceMapper.selectList(carspaceQueryWrapper);
        return listComplCarspacesByList(carspaces);
    }

    @Override
    public List<ComplCarspace> listUserCarSpaces(long ownerId) {
        QueryWrapper<Carspace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", ownerId);
        List<Carspace> carspaces = carSpaceMapper.selectList(queryWrapper);
        return listComplCarspacesByList(carspaces);
    }

    @Override
    public List<ComplCarspace> listReservedCarSpaces(long reserverId) {
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reserver_id", reserverId);
        queryWrapper.eq("reserve_status", 0);
        Set<Reservation> reservations = new HashSet<Reservation>(reservationMapper.selectList(queryWrapper));
        List<Long> CarIds = new ArrayList<>();
        for (Reservation reservation : reservations) {
            CarIds.add(reservation.getCarId());
        }
        return listComplCarspacesByCarIds(CarIds);
    }

    @Override
    public List<ComplCarspace> listComplCarspacesByList(List<Carspace> carspaces) {
        List<ComplCarspace> complCarspaces = new ArrayList<>();
        for (Carspace carSpace : carspaces) {
            QueryWrapper<Ireserve> ireserveQueryWrapper = new QueryWrapper<Ireserve>();
            ireserveQueryWrapper.eq("car_id", carSpace.getCarId());
            List<Ireserve> ires = ireserveMapper.selectList(ireserveQueryWrapper);
            User user = userMapper.selectById(carSpace.getOwnerId());
            complCarspaces.add(getSafetyComplCarSpace(new ComplCarspace(carSpace, ires, user)));
        }
        return complCarspaces;
    }

    @Override
    public List<ComplCarspace> listComplCarspacesByCarIds(List<Long> CarIds) {
        List<ComplCarspace> complCarspaces = new ArrayList<>();
        for (long CarId : CarIds) {
            Carspace carSpace = carSpaceMapper.selectById(CarId);
            //筛选可预约的时间
            QueryWrapper<Ireserve> ireserveQueryWrapper = new QueryWrapper<Ireserve>();
            ireserveQueryWrapper.eq("car_id", carSpace.getCarId());
            List<Ireserve> ires = ireserveMapper.selectList(ireserveQueryWrapper);
            User user = userMapper.selectById(carSpace.getOwnerId());
            complCarspaces.add(getSafetyComplCarSpace(new ComplCarspace(carSpace, ires, user)));
        }
        return complCarspaces;
    }

    @Override
    public ComplCarspace getSafetyComplCarSpace(ComplCarspace complCarspace) {
        Carspace safetyCarspace = new Carspace();
        Carspace carspace = complCarspace.getCarspace();
        safetyCarspace.setCarId(carspace.getCarId());
        safetyCarspace.setLocation(carspace.getLocation());
        safetyCarspace.setPrice(carspace.getPrice());
        safetyCarspace.setImageUrl(carspace.getImageUrl());
        safetyCarspace.setCarStatus(carspace.getCarStatus());
        safetyCarspace.setOwnerId(carspace.getOwnerId());
        List<Ireserve> safetyIreserves = new ArrayList<>();
        for (Ireserve ireserve : complCarspace.getIreseres()) {
            safetyIreserves.add(ireserveService.getSafetyIreserve(ireserve));
        }
        complCarspace.setCarspace(safetyCarspace);
        complCarspace.setIreseres(safetyIreserves);
        return complCarspace;
    }

}




