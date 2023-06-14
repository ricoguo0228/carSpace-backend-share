package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.mapper.ReservationMapper;
import com.example.carspacesdemo.model.entity.Reservation;
import com.example.carspacesdemo.service.ReservationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

}




