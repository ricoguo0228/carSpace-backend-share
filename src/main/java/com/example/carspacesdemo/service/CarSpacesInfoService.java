package com.example.carspacesdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.carspacesdemo.model.Carspace;

import java.sql.Date;

/**
 * @author Rico
 * @description 针对表【carspaces(用户创建的车位表)】的数据库操作Service
 * @createDate 2023-06-09 17:28:31
 */
public interface CarSpacesInfoService extends IService<Carspace> {
    /**
     * 创建新车位
     *
     * @param location
     * @param price
     * @param imageUrl
     * @param startTime
     * @param endTime
     * @return
     * @author Rico
     */
    long carSpaceCreate(String location, int price, String imageUrl, Date startTime, Date endTime);

    /**
     * 更新车位信息
     *
     * @param carId
     * @param location
     * @param price
     * @param imageUrl
     * @param startTime
     * @param endTime
     * @return
     */
    boolean carSpaceUpdate(long carId, String location, int price, String imageUrl, Date startTime, Date endTime);

    /**
     * 车位发布
     *
     * @param carId
     * @return
     */
    boolean carSpacePublish(long carId);

    /**
     * 车位回收
     *
     * @param carId
     * @return
     */
    boolean carSpaceInvoke(long carId);
}
