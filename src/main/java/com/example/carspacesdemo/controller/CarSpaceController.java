package com.example.carspacesdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.IdRequest;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.constant.CommonConstant;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.dto.carspacesinfo.CarSpaceUpdateRequest;
import com.example.carspacesdemo.model.dto.carspacesinfo.ListCarSpaceRequest;
import com.example.carspacesdemo.model.entity.Carspace;
import com.example.carspacesdemo.model.entity.ComplCarspace;
import com.example.carspacesdemo.model.dto.carspacesinfo.CarSpaceCreateRequest;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.CarSpaceService;
import com.example.carspacesdemo.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.carspacesdemo.common.ResultUtils.success;
import static com.example.carspacesdemo.constant.UserConstants.USER_LOGIN_STATE;

@RestController
@RequestMapping("/carSpaces")
public class CarSpaceController {
    @Resource
    private CarSpaceService carSpacesService;
    @PostMapping("/current")
    public BaseResponse<ComplCarspace> getCurrentCarSpace(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        ComplCarspace carSpace = carSpacesService.getCurrentCarSpace(id);
        return success(carSpace);
    }

    @PostMapping("/create")
    private BaseResponse<Long> carSpaceCreate(@RequestBody CarSpaceCreateRequest createRequest, HttpServletRequest httpServletRequest) {
        if (createRequest == null) {
            return null;
        }
        String location = createRequest.getLocation();
        int price = createRequest.getPrice();
        String imageUrl = createRequest.getImageUrl();
        List<LocalDateTime> timeSlots = createRequest.getTimeSlots();
        LocalDateTime startTime = timeSlots.get(0).plusHours(8);
        LocalDateTime endTime = timeSlots.get(1).plusHours(8);
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
    private BaseResponse<Boolean> carSpaceDelete(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        boolean res = carSpacesService.carSpaceDelete(id);
        return success(res);
    }

    @PostMapping("/update")
    private BaseResponse<Boolean> carSpaceUpdate(@RequestBody CarSpaceUpdateRequest updateRequest, HttpServletRequest httpServletRequest) {
        if (updateRequest == null) {
            return null;
        }
        long carId = updateRequest.getCarId();
        String location = updateRequest.getLocation();
        int price = updateRequest.getPrice();
        String imageUrl = updateRequest.getImageUrl();
        if(StringUtils.isAnyBlank(location)){
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        if(price <0){
            throw new BusinessException(ErrorCode.ERROR_PARAM, "价格不规范");
        }
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        boolean res = carSpacesService.carSpaceUpdate(carId, location, price, imageUrl);
        return success(res);
    }

    @PostMapping("/publish")
    private BaseResponse<Boolean> carSpacePublish(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        return success(carSpacesService.carSpacePublish(id));
    }

    @PostMapping("/invoke")
    private BaseResponse<Boolean> carSpaceInvoke(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不规范");
        }
        return success(carSpacesService.carSpaceInvoke(id));
    }

    @PostMapping("/listCarSpaces")
    public BaseResponse<Page<ComplCarspace>> listCarSpaces(@RequestBody ListCarSpaceRequest listCarSpaceRequest) {
        if (listCarSpaceRequest == null) {
            throw new BusinessException(ErrorCode.ERROR_PARAM);
        }
        long current = listCarSpaceRequest.getCurrent();
        long size = listCarSpaceRequest.getPageSize();
        // 限制爬虫
        if(size >20){
            throw new BusinessException(ErrorCode.ERROR_PARAM);
        }
        Page<Carspace> carSpacesPage = carSpacesService.page(new Page<>(current, size), getQueryWrapper(listCarSpaceRequest));
        List<ComplCarspace> complCarspaces = carSpacesService.listComplCarspacesByList(carSpacesPage.getRecords());
        Page<ComplCarspace> complCarSpacesPage=new Page<>(current,size);
        complCarSpacesPage.setRecords(complCarspaces);
        return success(complCarSpacesPage);
    }


    @PostMapping("/listUserCarSpaces")
    public BaseResponse<Page<ComplCarspace>> listUserSpaces(@RequestBody ListCarSpaceRequest listCarSpaceRequest) {
        long current = listCarSpaceRequest.getCurrent();
        long size = listCarSpaceRequest.getPageSize();
        // 限制爬虫
        if(size >20){
            throw new BusinessException(ErrorCode.ERROR_PARAM);
        }
        Page<Carspace> carSpacesPage = carSpacesService.page(new Page<>(current, size), getQueryWrapper(listCarSpaceRequest));
        List<Carspace> carSpaces = carSpacesPage.getRecords();
        List<ComplCarspace> complCarspaces = carSpacesService.listComplCarspacesByList(carSpaces);
        Page<ComplCarspace> complCarSpacesPage=new Page<>(current,size);
        complCarSpacesPage.setRecords(complCarspaces);
        return success(complCarSpacesPage);
    }

    @PostMapping("/listReservedCarSpaces")
    public BaseResponse<Page<ComplCarspace>> listReservedSpaces(@RequestBody ListCarSpaceRequest listCarSpaceRequest) {
        long reserverId = listCarSpaceRequest.getReserverId();
        long current = listCarSpaceRequest.getCurrent();
        long size = listCarSpaceRequest.getPageSize();
        List<ComplCarspace> complCarSpaces = carSpacesService.listReservedCarSpaces(reserverId);
        Page<ComplCarspace> complCarSpacesPage = new Page<>(current,size);
        complCarSpacesPage.setRecords(complCarSpaces);
        return success(complCarSpacesPage);
    }
    /**
     * 获取查询包装类
     *
     * @param listCarSpaceRequest
     * @return
     */
    public QueryWrapper<Carspace> getQueryWrapper(ListCarSpaceRequest listCarSpaceRequest) {
        QueryWrapper<Carspace> queryWrapper = new QueryWrapper<>();
        if (listCarSpaceRequest == null) {
            return queryWrapper;
        }

        String sortField = listCarSpaceRequest.getSortField();
        String sortOrder = listCarSpaceRequest.getSortOrder();
        String name = listCarSpaceRequest.getLocation();
        long ownerId = listCarSpaceRequest.getOwnerId();
        int startPrice = listCarSpaceRequest.getStartPrice();
        int endPrice = listCarSpaceRequest.getEndPrice();
        queryWrapper.eq(ownerId>0, "owner_id", ownerId);
        queryWrapper.gt(startPrice>0, "price", startPrice);
        queryWrapper.lt(endPrice>0, "price", endPrice);
        queryWrapper.like(StringUtils.isNotBlank(name), "location", name);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
