package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.IdRequest;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.entity.ComplCarspace;
import com.example.carspacesdemo.model.dto.carspacesinfo.CarSpacesCreateRequest;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.CarSpacesInfoService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/carSpaces")
public class CarSpacesController {
    @Resource
    private CarSpacesInfoService carSpacesService;

    @PostMapping("/create")
    private BaseResponse<Long> carSpaceCreate(@RequestBody CarSpacesCreateRequest carSpacesCreateRequest, HttpServletRequest httpServletRequest) {
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
        //获取用户id
        User user = (User)httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        long userId = user.getUserId();
        long CarId = carSpacesService.carSpaceCreate(userId,location, price, imageUrl, startDate, endDate);
        return success(CarId);
    }
    @PostMapping("/delete")
    private BaseResponse carSpaceDelete(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if(id <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesService.removeById(id));
    }
    @PostMapping("/update")
    private BaseResponse carSpaceUpdate(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if(id <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesService.removeById(id));
    }
    @PostMapping("/publish")
    private BaseResponse carSpacePublish(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if(id <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesService.carSpacePublish(id));
    }
    @PostMapping("/invoke")
    private BaseResponse carSpaceInvoke(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if(id <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车辆id不可以为空");
        }
        return success(carSpacesService.carSpaceInvoke(id));
    }
    @PostMapping("/getCarSpaces")
    public BaseResponse getCarSpaces(@RequestBody IdRequest idRequest){
        Long id = idRequest.getId();
        List<ComplCarspace> complCarSpaces = carSpacesService.getCarSpaces();
        return success(complCarSpaces);
    }
    @PostMapping("/getUserCarSpaces")
    public BaseResponse getUserSpaces(@RequestBody IdRequest idRequest){
        Long id = idRequest.getId();
        List<ComplCarspace> complCarSpaces = carSpacesService.getUserCarSpaces(id);
        return success(complCarSpaces);
    }
    @PostMapping("/getReservedCarSpaces")
    public BaseResponse getReservedSpaces(@RequestBody IdRequest idRequest){
        Long id = idRequest.getId();
        List<ComplCarspace> complCarSpaces = carSpacesService.getReservedCarSpaces(id);
        return success(complCarSpaces);
    }
}
