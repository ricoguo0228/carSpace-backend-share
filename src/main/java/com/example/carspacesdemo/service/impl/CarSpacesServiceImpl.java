package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.mapper.CarspaceMapper;
import com.example.carspacesdemo.model.Carspace;
import com.example.carspacesdemo.model.ReservedCarspace;
import com.example.carspacesdemo.service.CarSpacesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarSpacesServiceImpl extends ServiceImpl<CarspaceMapper, Carspace> implements CarSpacesService {
    @Resource
    CarspaceMapper carSpacesMapper;

    @Override
    public List<Carspace> getCarSpaces() {
        QueryWrapper<Carspace> queryWrapper = new QueryWrapper<>();
        return carSpacesMapper.selectList(queryWrapper);
    }

    @Override
    public List<Carspace> getUserCarSpaces(long userId) {
        if(userId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"用户id不能小于0");
        }
        QueryWrapper<Carspace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id",userId);
        List<Carspace> carSpaces = carSpacesMapper.selectList(queryWrapper);
        return carSpaces;
    }

    @Override
    public List<Carspace> getReservedCarSpaces(long userId) {
        QueryWrapper<Carspace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id",userId);
        List<Carspace> carSpaces = carSpacesMapper.selectList(queryWrapper);
        List<ReservedCarspace> reservedCarSpaces ;
        //todo:写完预定功能记得回来补上
        return null;

    }
}
