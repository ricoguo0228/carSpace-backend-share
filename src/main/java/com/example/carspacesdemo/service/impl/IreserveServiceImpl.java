package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.mapper.IreserveMapper;
import com.example.carspacesdemo.model.entity.Ireserve;
import com.example.carspacesdemo.service.IreserveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* @author Rico
* @description 针对表【ireserve】的数据库操作Service实现
* @createDate 2023-06-14 21:12:51
*/
@Service
public class IreserveServiceImpl extends ServiceImpl<IreserveMapper, Ireserve>
    implements IreserveService{
    @Resource
    IreserveMapper ireserveMapper;

    @Override
    public boolean timeSlotInsert(long carId,LocalDateTime startTime, LocalDateTime endTime) {
        if(startTime.isAfter(endTime)){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"开始时间不可以比结束时间晚");
        }
        //保存车位可用时间段信息
        Ireserve ireserve = new Ireserve();
        ireserve.setCarId(carId);
        ireserve.setStartTime(startTime);
        ireserve.setEndTime(endTime);
        int i = ireserveMapper.insert(ireserve);
        if (i == 0) {
            throw new BusinessException(ErrorCode.DAO_ERROR,"删除失败");
        }
        return true;
    }


    @Override
    public boolean timeSlotDelete(long iId) {
        int i = ireserveMapper.deleteById(iId);
        if(i == 0){
            throw new BusinessException(ErrorCode.DAO_ERROR,"删除失败");
        }
        return true;
    }

    @Override
    public List<Ireserve> listTimeSlots(long carId) {
        QueryWrapper<Ireserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id",carId);
        List<Ireserve> ireserves = ireserveMapper.selectList(queryWrapper);
        List<Ireserve> safetyIreserves = new ArrayList<>();
        for(Ireserve ireserve :ireserves){
            safetyIreserves.add(getSafetyIreserve(ireserve));
        }
        return safetyIreserves;
    }

    @Override
    public Ireserve getSafetyIreserve(Ireserve iReserve) {
        Ireserve safetyIreserve = new Ireserve();
        safetyIreserve.setStartTime(iReserve.getStartTime());
        safetyIreserve.setEndTime(iReserve.getEndTime());
        return safetyIreserve;
    }
}




