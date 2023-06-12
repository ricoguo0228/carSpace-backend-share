package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.mapper.ReservationMapper;
import com.example.carspacesdemo.service.ReservationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.example.carspacesdemo.common.ResultUtils.*;

@RestController
@RequestMapping("/reserve")
public class ReservationController {
    @Resource
    ReservationService reservationService;

}
