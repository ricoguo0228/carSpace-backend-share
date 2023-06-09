package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.mapper.CarspaceMapper;
import com.example.carspacesdemo.model.Carspace;
import com.example.carspacesdemo.service.CarSpacesInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;

/**
* @author Rico
* @description 针对表【carspaces(用户创建的车位表)】的数据库操作Service实现
* @createDate 2023-06-09 17:28:31
*/
@Service
public class CarSpacesInfoServiceImpl extends ServiceImpl<CarspaceMapper, Carspace>  implements CarSpacesInfoService {

    @Resource
    CarspaceMapper carSpacesMapper;
    @Override
    public long carSpaceCreate(String location, int price, String imageUrl, Date startTime, Date endTime) {
        if(StringUtils.isAnyBlank(location)){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"位置信息不能为空");
        }
        if(price < 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"价格不能小于0");
        }
        Carspace carSpace = new Carspace();
        carSpace.setLocation(location);
        carSpace.setPrice(price);
        carSpace.setImageUrl(imageUrl);
        carSpace.setStartTime(startTime);
        carSpace.setEndTime(endTime);
        boolean saveResult = this.save(carSpace);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"保存失败了");
        }
        return carSpace.getCarId();
    }

    @Override
    public boolean carSpaceUpdate(long carId, String location, int price, String imageUrl, Date startTime, Date endTime) {
        if(StringUtils.isAnyBlank(location)){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"位置信息不能为空");
        }
        if(price < 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"价格不能小于0");
        }
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位ID不能为空");
        }
        Carspace carSpace = carSpacesMapper.selectById(carId);
        carSpace.setLocation(location);
        carSpace.setPrice(price);
        carSpace.setImageUrl(imageUrl);
        carSpace.setStartTime(startTime);
        carSpace.setEndTime(endTime);
        boolean updateResult = this.updateById(carSpace);
        if(!updateResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败了");
        }
        return true;
    }

    @Override
    public boolean carSpacePublish(long carId) {
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位ID不能为空");
        }
        Carspace carSpace = carSpacesMapper.selectById(carId);
        carSpace.setCarStatus(1);
        boolean updateResult = this.updateById(carSpace);
        if(!updateResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"发布失败了");
        }
        return updateResult;
    }

    @Override
    public boolean carSpaceInvoke(long carId) {
        if(carId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"车位ID不能为空");
        }
        Carspace carSpace = carSpacesMapper.selectById(carId);
        carSpace.setCarStatus(0);
        boolean updateResult = this.updateById(carSpace);
        if(!updateResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"发布失败了");
        }
        return updateResult;
    }
}




