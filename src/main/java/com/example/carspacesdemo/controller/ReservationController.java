package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.common.IdRequest;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.dto.reservation.ReservationAddRequest;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.ReservationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static com.example.carspacesdemo.common.ResultUtils.success;
import static com.example.carspacesdemo.constant.UserConstants.USER_LOGIN_STATE;

@RestController
@RequestMapping("/reserve")
public class ReservationController {
    @Resource
    ReservationService reservationService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addReservation(HttpServletRequest httpServletRequest,@RequestBody ReservationAddRequest request){
        User user = (User)httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if(user == null){
            throw new BusinessException(ErrorCode.LOGIN_ERROR,"用户未登录");
        }
        long carId = request.getCarId();
        LocalDateTime reserveStartTime = request.getReserveStartTime();
        LocalDateTime reserveEndTime = request.getReserveEndTime();
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位id不规范");
        }
        if(reserveStartTime.isAfter(reserveEndTime)){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"开始时间不能晚于结束时间");
        }
        long reserverId = user.getUserId();
        boolean res = reservationService.addReservation(reserverId, carId, reserveStartTime, reserveEndTime);
        return success(res);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteReservation(@RequestBody IdRequest idRequest){
        long reserveId = idRequest.getId();
        if(reserveId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"预约id不规范");
        }
        boolean res = reservationService.deleteReservation(reserveId);
        return success(res);
    }

}
