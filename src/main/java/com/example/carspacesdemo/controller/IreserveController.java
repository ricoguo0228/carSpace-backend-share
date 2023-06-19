package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.common.IdRequest;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.dto.ireserve.IreserveIncreaseRequest;
import com.example.carspacesdemo.model.entity.Ireserve;
import com.example.carspacesdemo.service.IreserveService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.carspacesdemo.common.ResultUtils.success;

@RestController
@RequestMapping("/Ireserve")
public class IreserveController {
    @Resource
    IreserveService ireserveService;

    /**
     * 添加时间段分段
     *
     * @param ireserveIncreaseRequest
     * @return
     */
    @PostMapping("/increase")
    private BaseResponse<Boolean> timeSlotsIncrease(@RequestBody IreserveIncreaseRequest ireserveIncreaseRequest) {
        List<LocalDateTime> timeSlots = ireserveIncreaseRequest.getTimeSlots();
        LocalDateTime startTime = timeSlots.get(0);
        LocalDateTime endTime = timeSlots.get(1);
        long carId = ireserveIncreaseRequest.getCarId();
        if (startTime.isAfter(endTime)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "开始时间不可以比结束时间晚");
        }
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不正确");
        }
        boolean res = ireserveService.timeSlotInsert(carId, startTime, endTime);
        return success(res);
    }

    /**
     * 删除时间段分段
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/delete")
    private BaseResponse<Boolean> timeSlotsDelete(@RequestBody IdRequest idRequest) {
        long carId = idRequest.getId();
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不正确");
        }
        boolean res = ireserveService.timeSlotDelete(carId);
        return success(res);
    }

    /**
     * 获取车位的可预约时间段
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/listTimeSlots")
    private BaseResponse<List<Ireserve>> listTimeSlots(@RequestBody IdRequest idRequest) {
        Long carId = idRequest.getId();
        if (carId <= 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "车辆id不正确");
        }
        List<Ireserve> ireserves = ireserveService.listTimeSlots(carId);
        return success(ireserves);
    }
}
