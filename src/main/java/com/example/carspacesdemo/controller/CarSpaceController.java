package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.IdRequest;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.dto.carspacesinfo.CarSpaceUpdateRequest;
import com.example.carspacesdemo.model.entity.ComplCarspace;
import com.example.carspacesdemo.model.dto.carspacesinfo.CarSpaceCreateRequest;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.CarSpaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.carspacesdemo.common.ResultUtils.success;
import static com.example.carspacesdemo.constant.UserConstants.USER_LOGIN_STATE;

@RestController
@RequestMapping("/carSpaces")
public class CarSpaceController {
    @Resource
    private CarSpaceService carSpacesService;

    @PostMapping("/create")
    private BaseResponse<Long> carSpaceCreate(@RequestBody CarSpaceCreateRequest createRequest, HttpServletRequest httpServletRequest) {
        if (createRequest == null) {
            return null;
        }
        String location = createRequest.getLocation();
        int price = createRequest.getPrice();
        String imageUrl = createRequest.getImageUrl();
        List<LocalDateTime> timeSlots = createRequest.getTimeSlots();
        LocalDateTime startTime = timeSlots.get(0);
        LocalDateTime endTime = timeSlots.get(1);
        if(startTime.isAfter(endTime)){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"开始时间不可以比结束时间晚");
        }
        if (StringUtils.isAnyBlank(location)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        //获取用户id
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        long userId = user.getUserId();
        long CarId = carSpacesService.carSpaceCreate(userId, location, price, imageUrl, startTime,endTime);
        return success(CarId);
    }

    @PostMapping("/delete")
    private BaseResponse carSpaceDelete(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        return success(carSpacesService.removeById(id));
    }

    @PostMapping("/update")
    private BaseResponse carSpaceUpdate(@RequestBody CarSpaceUpdateRequest updateRequest, HttpServletRequest httpServletRequest) {
        if (updateRequest == null) {
            return null;
        }
        long carId = updateRequest.getCarId();
        String location = updateRequest.getLocation();
        int price = updateRequest.getPrice();
        String imageUrl = updateRequest.getImageUrl();
        Map<LocalDateTime, LocalDateTime> timeSlots = updateRequest.getTimeSlots();
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        boolean res = carSpacesService.carSpaceUpdate(carId, location, price, imageUrl);
        return success(res);
    }

    @PostMapping("/publish")
    private BaseResponse carSpacePublish(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        return success(carSpacesService.carSpacePublish(id));
    }

    @PostMapping("/invoke")
    private BaseResponse carSpaceInvoke(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        return success(carSpacesService.carSpaceInvoke(id));
    }

    @PostMapping("/listCarSpaces")
    public BaseResponse listCarSpaces() {
        List<ComplCarspace> complCarSpaces = carSpacesService.listCarSpaces();
        return success(complCarSpaces);
    }

    @PostMapping("/listUserCarSpaces")
    public BaseResponse listUserSpaces(@RequestBody IdRequest idRequest) {
        long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        List<ComplCarspace> complCarSpaces = carSpacesService.listUserCarSpaces(id);
        return success(complCarSpaces);
    }

    @PostMapping("/listReservedCarSpaces")
    public BaseResponse listReservedSpaces(@RequestBody IdRequest idRequest) {
        long id = idRequest.getId();
        List<ComplCarspace> complCarSpaces = carSpacesService.listReservedCarSpaces(id);
        return success(complCarSpaces);
    }
}
