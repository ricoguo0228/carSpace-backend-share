package com.example.carspacesdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.carspacesdemo.model.dto.carspacesinfo.ListCarSpaceRequest;
import com.example.carspacesdemo.model.entity.Carspace;
import com.example.carspacesdemo.model.entity.ComplCarspace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Rico
 * @description 针对表【carspaces(用户创建的车位表)】的数据库操作Service
 * @createDate 2023-06-09 17:28:31
 */
public interface CarSpaceService extends IService<Carspace> {
    /**
     * 获取当前车位
     *
     * @param id
     * @return
     */
    ComplCarspace getCurrentCarSpace(long id);
    /**
     * 创建新车位
     *
     * @param location
     * @param price
     * @param imageUrl
     * @return
     * @author Rico
     */
    long carSpaceCreate(long userId, String location, int price, String imageUrl, LocalDateTime startTime, LocalDateTime endTime);
    long AiCarSpaceCreate(long userId, String location, int price, String imageUrl);
    /**
     * 更新车位信息
     *
     * @param carId
     * @param location
     * @param price
     * @param imageUrl
     * @return
     */
    boolean carSpaceUpdate(long carId, String location, int price, String imageUrl);

    boolean carSpaceDelete(long carId);

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
     * 获取用户创建的所有车位
     *
     * @param ownerId
     * @return
     */
    List<ComplCarspace> listUserCarSpaces(long ownerId);

    /***
     * 获取用户预定的车位
     *
     * @param reserverId
     * @return
     */
    List<ComplCarspace> listReservedCarSpaces(long reserverId);

    /**
     * 获取可供返回显示信息的车位
     *
     * @param carspaces
     * @return
     */
    List<ComplCarspace> listComplCarspacesByList(List<Carspace> carspaces);

    /**
     * 重载返回显示信息的车位函数
     *
     * @param CarIds
     * @return
     */
    List<ComplCarspace> listComplCarspacesByCarIds(List<Long> CarIds);

    /**
     * 获取安全的完整车位信息
     * @param complCarspace
     * @return
     */
    ComplCarspace getSafetyComplCarSpace(ComplCarspace complCarspace);

    List<Carspace> timeSelect(ListCarSpaceRequest listCarSpaceRequest);
}
