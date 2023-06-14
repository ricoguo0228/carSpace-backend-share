package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.service.ReservationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/reserve")
public class ReservationController {
    @Resource
    ReservationService reservationService;

}
