package com.example.carspacesdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.carspacesdemo.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rico
 * @description 针对表【users(用户)】的数据库操作Service
 * @createDate 2023-06-05 11:35:03
 */
public interface UserService extends IService<User> {
    /**
     * @param userAccount    用户名
     * @param userPassword 用户密码
     * @param userPhone    用户手机号
     * @return 返回用户id
     * @author Rico
     */
    long userRegister(String userAccount, String userPassword, String userCheckPassword, String userPhone);

    /**
     * 用户登录功能实现
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     * @author Rico
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

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
     * @param userId
     * @param userNewPassword
     * @param userPhone
     * @param NickName
     * @return
     */
    boolean userUpdate(long userId,String userNewPassword, String userPhone, String NickName) ;

    User getLoginUser(HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     * @author Rico
     */
    User getSafetyUser(User originUser);
}
