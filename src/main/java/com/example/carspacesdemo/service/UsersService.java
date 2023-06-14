package com.example.carspacesdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.carspacesdemo.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rico
 * @description 针对表【users(用户)】的数据库操作Service
 * @createDate 2023-06-05 11:35:03
 */
public interface UsersService extends IService<User> {
    /**
     * @param username     用户名
     * @param userPassword 用户密码
     * @param userPhone    用户手机号
     * @return 返回用户id
     * @author Rico
     */
    long userRegister(String username, String userPassword, String userCheckPassword, String userPhone);

    /**
     * 用户登录功能实现
     *
     * @param username
     * @param userPassword
     * @param request
     * @return
     * @author Rico
     */
    User userLogin(String username, String userPassword, HttpServletRequest request);

    /**
     * 用户登出功能实现
     *
     * @param request
     * @author Rico
     **/
    boolean userLogout(HttpServletRequest request);

    /**
     * 用户修改密码功能实现
     *
     * @param username
     * @param userNewPassword
     * @param userPhone
     * @return
     */
    boolean userUpdatePassword(String username, String userNewPassword, String userPhone);

    /**
     * 用户修改手机号功能实现
     *
     * @param username
     * @param userNewPhone
     * @param userPassword
     * @return
     */
    User userUpdatePhone(String username, String userPassword, String userNewPhone);

    /**
     * 用户修改昵称功能实现
     *
     * @param username
     * @param NewNickName
     * @return
     */
    User userUpdateNickName(String username, String NewNickName);


    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     * @author Rico
     */
    User getSafetyUser(User originUser);
}
