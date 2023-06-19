package com.example.carspacesdemo.controller;


import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.IdRequest;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.model.dto.Users.*;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.example.carspacesdemo.common.ResultUtils.success;
import static com.example.carspacesdemo.constant.UserConstants.*;

/**
 * 用户服务接口
 *
 * @author Rico
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    @PostMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR);
        }
        long userId = currentUser.getUserId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return success(safetyUser);
    }

    @PostMapping("/register")
    private BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String userCheckPassword = userRegisterRequest.getUserCheckPassword();
        String userPhone = userRegisterRequest.getUserPhone();
        if (StringUtils.isAnyBlank(userAccount, userPassword, userPhone)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        long id = userService.userRegister(userAccount, userPassword, userCheckPassword, userPhone);
        return success(id);
    }

    @PostMapping("/login")
    private BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);
        return success(user);
    }

    @PostMapping("/logout")
    private BaseResponse<Boolean> userLogout( HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return null;
        }
        if(userService.userLogout(httpServletRequest)){
            return success(true);
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    }

    @PostMapping("/delete")

    private BaseResponse<Boolean> deleteUsers(@RequestBody IdRequest idRequest, HttpServletRequest httpServletRequest) {
        if (!isAdmin(httpServletRequest))
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "权限不足");
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "id小于0");
        }
        return success(userService.removeById(id));
    }

    @PostMapping("/update")
    private BaseResponse<Boolean> userUpdate(@RequestBody UserUpdateRequest userUpdatePasswordRequest,HttpServletRequest httpServletRequest) {
        if (userUpdatePasswordRequest == null) {
            return null;
        }
        String userNewPassword = userUpdatePasswordRequest.getUserNewPassword();
        String userNewPhone = userUpdatePasswordRequest.getUserPhone();
        String NickName = userUpdatePasswordRequest.getNickName();
        if (StringUtils.isAnyBlank(userNewPassword, userNewPhone,NickName)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        User user = (User)httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        long id = user.getUserId();
        boolean res = userService.userUpdate(id, userNewPassword, userNewPhone,NickName);
        return success(res);
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
        return user != null && user.getUserRole() == 1;
    }
}
