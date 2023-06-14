package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.entity.ComplCarspace;
import com.example.carspacesdemo.model.dto.carspacesinfo.CarSpacesCreateRequest;
import com.example.carspacesdemo.service.CarSpacesInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.carspacesdemo.common.ResultUtils.success;

@RestController
@RequestMapping("/carSpaces")
public class CarSpacesController {
    @Resource
    private CarSpacesInfoService carSpacesService;

    @PostMapping("/create")
    private BaseResponse<Long> carSpaceCreate(@RequestBody CarSpacesCreateRequest carSpacesCreateRequest) {
        if(carSpacesCreateRequest == null){
            return null;
        }
        String location = carSpacesCreateRequest.getLocation();
        int price = carSpacesCreateRequest.getPrice();
        LocalDateTime startDate = carSpacesCreateRequest.getStartDate();
        LocalDateTime endDate = carSpacesCreateRequest.getEndDate();
        String imageUrl = carSpacesCreateRequest.getImageUrl();
        if (StringUtils.isAnyBlank(location)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM,"参数不可以为空");
        }
        long id = carSpacesService.carSpaceCreate(location, price, imageUrl, startDate, endDate);
        return success(id);
    }
    @PostMapping("/delete")
    private BaseResponse carSpaceDelete(@RequestBody long CarId) {
        if(CarId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesService.removeById(CarId));
    }
    @PostMapping("/publish")
    private BaseResponse carSpacePublish(@RequestBody long CarId) {
        if(CarId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesService.carSpacePublish(CarId));
    }
    @PostMapping("/invoke")
    private BaseResponse carSpaceInvoke(@RequestBody long CarId) {
        if(CarId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesService.carSpaceInvoke(CarId));
    }
    @RequestMapping("/getCarSpaces")
    public BaseResponse getCarSpaces(@RequestBody long userId){
        List<ComplCarspace> complCarSpaces = carSpacesService.getCarSpaces();
        return success(complCarSpaces);
    }
    @RequestMapping("/getUserCarSpaces")
    public BaseResponse getUserSpaces(@RequestBody long userId){
        List<ComplCarspace> complCarSpaces = carSpacesService.getUserCarSpaces(userId);
        return success(complCarSpaces);
    }
    @RequestMapping("/getReservedCarSpaces")
    public BaseResponse getReservedSpaces(@RequestBody long userId){
        List<ComplCarspace> complCarSpaces = carSpacesService.getReservedCarSpaces(userId);
        return success(complCarSpaces);
    }
}
