package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.*;
import com.example.carspacesdemo.model.request.Users.*;
import com.example.carspacesdemo.service.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.example.carspacesdemo.common.ResultUtils.success;
import static com.example.carspacesdemo.constant.UsersConstants.*;

/**
 * 用户服务接口
 *
 * @author Rico
 */
@RestController
@RequestMapping("/user")
public class UsersController {
    @Resource
    private UsersService usersService;

    @PostMapping("/register")
    private BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String username = userRegisterRequest.getUsername();
        String userPassword = userRegisterRequest.getUserPassword();
        String userCheckPassword = userRegisterRequest.getUserCheckPassword();
        String userPhone = userRegisterRequest.getUserPhone();
        if (StringUtils.isAnyBlank(username, userPassword, userPhone)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        long id = usersService.userRegister(username, userPassword, userCheckPassword, userPhone);
        return success(id);
    }

    @PostMapping("/login")
    private BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            return null;
        }
        String username = userLoginRequest.getUsername();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(username, userPassword)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        User user = usersService.userLogin(username, userPassword, httpServletRequest);
        return success(user);
    }

    @PostMapping("/logout")
    private BaseResponse<Integer> userLogout(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            return null;
        }
        return success(usersService.userLogout(httpServletRequest));
    }

    @PostMapping("/delete")
    private BaseResponse deleteUsers(@RequestBody long id, HttpServletRequest httpServletRequest) {
        if (!isAdmin(httpServletRequest))
            throw new BusinessException(ErrorCode.POWER_ERROR, "权限不足");
        if (id <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "id小于0");
        }
        return success(usersService.removeById(id));
    }

    @PostMapping("/updatePassword")
    private BaseResponse userUpdatePassword(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest) {
        if (userUpdatePasswordRequest == null) {
            return null;
        }
        String username = userUpdatePasswordRequest.getUsername();
        String userNewPassword = userUpdatePasswordRequest.getUserNewPassword();
        String userPhone = userUpdatePasswordRequest.getUserPhone();
        if (StringUtils.isAnyBlank(username, userNewPassword, userPhone)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        boolean res = usersService.userUpdatePassword(username, userNewPassword, userPhone);
        return success(res);
    }

    @PostMapping("/updateNickName")
    private BaseResponse userUpdateNickName(@RequestBody UserUpdateNickNameRequest userUpdateNickNameRequest) {
        if (userUpdateNickNameRequest == null) {
            return null;
        }
        String username = userUpdateNickNameRequest.getUsername();
        String nickName = userUpdateNickNameRequest.getNickName();
        if (StringUtils.isAnyBlank(username, nickName)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        User user = usersService.userUpdateNickName(username, nickName);
        return success(user);
    }

    @PostMapping("/updatePhone")
    private BaseResponse userUpdatePhone(@RequestBody UserUpdatePhoneRequest userUpdatePhoneRequest) {
        if (userUpdatePhoneRequest == null) {
            return null;
        }
        String username = userUpdatePhoneRequest.getUsername();
        String userPassword = userUpdatePhoneRequest.getUserPassword();
        String userPhone = userUpdatePhoneRequest.getUserNewPhone();
        if (StringUtils.isAnyBlank(username, userPassword, userPhone)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        User user = usersService.userUpdatePhone(username, userPassword, userPhone);
        return success(user);
    }

    /**
     * 检查是否是管理员
     *
     * @param httpServletRequest 请求
     * @return 管理员？
     */
    private boolean isAdmin(HttpServletRequest httpServletRequest) {
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}