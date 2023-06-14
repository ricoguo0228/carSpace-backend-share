package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.mapper.UserMapper;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.UserService;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.carspacesdemo.constant.UserConstants.*;

/**
 * @author Rico
 * @description 针对表【users(用户)】的数据库操作Service实现
 * @createDate 2023-06-05 11:35:03
 */
@Slf4j
@Service
public class UseServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    /**
     * 盐值，混淆密码
     */
    String SALT = "Rico";
    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String userCheckPassword, String userPhone) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, userPhone)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        if(!userPassword.equals(userCheckPassword)){
            throw new BusinessException(ErrorCode.ERROR_PARAM, "两次输入的密码不一致");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户名不可以小于4");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户密码不可以小于8");
        }
        if (userPhone.length() != 11) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "手机号码不对");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        ;
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "账户不能包含特殊字符");
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "账户重复了");
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT +
                userPassword).getBytes());
        // 插⼊数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserPhone(userPhone);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存失败了");
        }
        return user.getUserId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户名不可以小于4");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户密码不可以小于8");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        ;
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "账户重复了");
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 寻找账户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("userLogin find no match");
            throw new BusinessException(ErrorCode.ERROR_PARAM, "找不到用户");
        }
        //用户脱敏
        User safetyuser = getSafetyUser(user);
        //记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyuser);
        //返回用户
        return safetyuser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public boolean userUpdate(long userId,String userNewPassword, String userPhone, String NickName) {
        if(userId <= 0){
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户id不正确");
        }
        if (StringUtils.isAnyBlank(userNewPassword, userPhone)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        if(userPhone.length() != 11) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "手机号码不规范");
        }
        if(userNewPassword.length() < 8) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户密码至少需要8位");
        }
        User user = userMapper.selectById(userId);
        if(user == null){
            throw new BusinessException(ErrorCode.ERROR_PARAM,"用户已经不存在");
        }
        // 加密
        String encryptNewPassword = DigestUtils.md5DigestAsHex((SALT + userNewPassword).getBytes());
        //更新密码
        user.setUserPassword(encryptNewPassword);
        user.setUserPhone(userPhone);
        user.setNickName(NickName);
        int res = userMapper.updateById(user);
        if(res > 0){
            return true;
        }
        else
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
    }
    /**
     * 用户脱敏
     *
     * @param originUser 目标用户
     * @return 脱敏用户
     */
    @Override
    public User getSafetyUser(User originUser) {
        //用户脱敏
        User safetyuser = new User();
        safetyuser.setUserId(originUser.getUserId());
        safetyuser.setUserAccount(originUser.getUserAccount());
        safetyuser.setUserPhone(originUser.getUserPhone());
        safetyuser.setInsertTime(originUser.getInsertTime());
        safetyuser.setUserRole(originUser.getUserRole());
        safetyuser.setUserCredit(originUser.getUserCredit());
        safetyuser.setNickName(originUser.getNickName());
        return safetyuser;
    }
}




