package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.request.CarSpacesInfo.CarSpacesCreateRequest;
import com.example.carspacesdemo.service.CarSpacesInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Date;

import static com.example.carspacesdemo.common.ResultUtils.success;

@RestController
@RequestMapping("/carSpacesInfo")
public class CarSpacesInfoController {
    @Resource
    private CarSpacesInfoService carSpacesInfoService;

    @PostMapping("/create")
    private BaseResponse<Long> carSpaceCreate(@RequestBody CarSpacesCreateRequest carSpacesCreateRequest) {
        if(carSpacesCreateRequest == null){
            return null;
        }
        String location = carSpacesCreateRequest.getLocation();
        int price = carSpacesCreateRequest.getPrice();
        Date startDate = carSpacesCreateRequest.getStartDate();
        Date endDate = carSpacesCreateRequest.getEndDate();
        String imageUrl = carSpacesCreateRequest.getImageUrl();
        if (StringUtils.isAnyBlank(location)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM,"参数不可以为空");
        }
        long id = carSpacesInfoService.carSpaceCreate(location, price, imageUrl, startDate, endDate);
        return success(id);
    }
    @PostMapping("/delete")
    private BaseResponse carSpaceDelete(@RequestBody long CarId) {
        if(CarId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesInfoService.removeById(CarId));
    }
    @PostMapping("/publish")
    private BaseResponse carSpacePublish(@RequestBody long CarId) {
        if(CarId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesInfoService.carSpacePublish(CarId));
    }
    @PostMapping("/invoke")
    private BaseResponse carSpaceInvoke(@RequestBody long CarId) {
        if(CarId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesInfoService.carSpaceInvoke(CarId));
    }
}
