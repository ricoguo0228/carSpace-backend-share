package com.example.carspacesdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.carspacesdemo.model.entity.Carspace;
import com.example.carspacesdemo.model.entity.ComplCarspace;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Rico
 * @description 针对表【carspaces(用户创建的车位表)】的数据库操作Service
 * @createDate 2023-06-09 17:28:31
 */
public interface CarSpaceService extends IService<Carspace> {
    /**
     * 创建新车位
     *
     * @param userId
     * @param location
     * @param price
     * @param imageUrl
     * @param startTime
     * @param endTime
     * @return
     * @author Rico
     */
    long carSpaceCreate(long userId, String location, int price, String imageUrl, LocalDateTime startTime, LocalDateTime endTime);

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
    boolean carSpaceUpdate(long carId, String location, int price, String imageUrl, LocalDateTime startTime, LocalDateTime endTime);

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
     * @param ownerId
     * @return
     */
    List<ComplCarspace> getUserCarSpaces(long ownerId);

    /***
     * 获取用户预定的车位
     *
     * @param reserverId
     * @return
     */
    List<ComplCarspace> getReservedCarSpaces(long reserverId);

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
