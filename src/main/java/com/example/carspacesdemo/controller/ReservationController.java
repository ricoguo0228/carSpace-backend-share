package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.common.IdRequest;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.dto.reservation.ReservationAddRequest;
import com.example.carspacesdemo.model.entity.Reservation;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.ReservationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.carspacesdemo.common.ResultUtils.success;
import static com.example.carspacesdemo.constant.UserConstants.USER_LOGIN_STATE;

@RestController
@RequestMapping("/reserve")
public class ReservationController {
    @Resource
    ReservationService reservationService;

    /**
     * 添加预约信息实现
     *
     * @param httpServletRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addReservation(HttpServletRequest httpServletRequest,@RequestBody ReservationAddRequest request){
        User user = (User)httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if(user == null){
            throw new BusinessException(ErrorCode.LOGIN_ERROR,"用户未登录");
        }
        long carId = request.getCarId();
        LocalDateTime reserveStartTime= request.getTimeSlots().get(0).plusHours(8);
        LocalDateTime reserveEndTime = request.getTimeSlots().get(1).plusHours(8);
        String carPass = request.getCarPass();
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位id不规范");
        }
        if(reserveStartTime.isAfter(reserveEndTime)){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"开始时间不能晚于结束时间");
        }
        long reserverId = user.getUserId();
        boolean res = reservationService.addReservation(reserverId, carId, reserveStartTime, reserveEndTime,carPass);
        return success(res);
    }

    /**
     * 删除预约信息实现
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteReservation(@RequestBody IdRequest idRequest){
        long reserveId = idRequest.getId();
        if(reserveId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"预约id不规范");
        }
        boolean res = reservationService.deleteReservation(reserveId);
        return success(res);
    }

    /**
     * 获取用户的车位预约信息实现
     *
     * @param idRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/currentReserve")
    public BaseResponse<List<Reservation>> currentReservationsInReserve(@RequestBody IdRequest idRequest,HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if(user==null){
            throw new BusinessException(ErrorCode.LOGIN_ERROR,"用户未登录");
        }
        Long userId = user.getUserId();
        long carId = idRequest.getId();
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位id不规范");
        }
        List<Reservation> reservations = reservationService.currentReservationsInReserve(carId, userId);
        return success(reservations);
    }
    @PostMapping("/currentCreate")
    public BaseResponse<List<Reservation>> currentReservationsInCreate(@RequestBody IdRequest idRequest){
        long carId = idRequest.getId();
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位id不规范");
        }
        List<Reservation> reservations = reservationService.currentReservationsInCreate(carId);
        return success(reservations);
    }
}
