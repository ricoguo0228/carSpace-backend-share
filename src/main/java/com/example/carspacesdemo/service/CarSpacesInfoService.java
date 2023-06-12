package com.example.carspacesdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.carspacesdemo.model.Carspace;
import com.example.carspacesdemo.model.ComplCarspace;
import com.example.carspacesdemo.model.User;

import java.util.Date;
import java.util.List;

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

    /**
     * 获取所有车位
     *
     * @return
     * @author Rico
     */
    List<ComplCarspace> getCarSpaces();

    /**
     * 获取用户创建的所有车位
     *
     * @param userId
     * @return
     */
    List<ComplCarspace> getUserCarSpaces(long userId);

    /***
     * 获取用户预定的车位
     *
     * @param userId
     * @return
     */
    List<ComplCarspace> getReservedCarSpaces(long userId);

    /**
     * 获取可供返回显示信息的车位
     *
     * @param carspaces
     * @return
     */
    List<ComplCarspace> getComplCarspacesByList(List<Carspace> carspaces);

    /**
     * 重载返回显示信息的车位函数
     *
     * @param CarIds
     * @return
     */
    List<ComplCarspace> getComplCarspacesByCarIds(List<Long> CarIds);
}
