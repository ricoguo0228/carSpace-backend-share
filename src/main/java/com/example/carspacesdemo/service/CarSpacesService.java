package com.example.carspacesdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.carspacesdemo.model.Carspace;

import java.util.List;

/**
 * @author Rico
 * <p>
 * 针对获取车位信息显示写了一个类
 */
public interface CarSpacesService extends IService<Carspace> {
    /**
     * 获取所有车位
     *
     * @return
     * @author Rico
     */
    List<Carspace> getCarSpaces();

    /**
     * 获取用户创建的所有车位
     *
     * @param userId
     * @return
     */
    List<Carspace> getUserCarSpaces(long userId);

    /**
     * 获取用户预定的车位
     *
     * @param userId
     * @return
     */

    List<Carspace> getReservedCarSpaces(long userId);
}
