package com.example.carspacesdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.mapper.UserMapper;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.UsersService;

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
public class UsersServiceImpl extends ServiceImpl<UserMapper, User>
        implements UsersService {
    /**
     * 盐值，混淆密码
     */
    String SALT = "Rico";
    @Resource
    private UserMapper usersMapper;

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
        long count = usersMapper.selectCount(queryWrapper);
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
        User user = usersMapper.selectOne(queryWrapper);
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
    public boolean userUpdatePassword(String userAccount, String userNewPassword, String userPhone) {
        if (StringUtils.isAnyBlank(userAccount, userNewPassword, userPhone)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户名不可以小于4");
        }
        if(!userNewPassword.equals(userPhone)){
            throw new BusinessException(ErrorCode.ERROR_PARAM, "两次输入的密码不一致");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "账户不能包含特殊字符");
        }
        // 加密
        String encryptNewPassword = DigestUtils.md5DigestAsHex((SALT + userNewPassword).getBytes());
        // 寻找账户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_phone", userPhone);
        User user = usersMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("findPassword failed because user is null");
            throw new BusinessException(ErrorCode.ERROR_PARAM, "找不到用户");
        }
        //更新密码
        user.setUserPassword(encryptNewPassword);
        int res = usersMapper.updateById(user);
        if(res > 0){
            return true;
        }
        else
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新密码失败");
    }

    @Override
    public User userUpdatePhone(String userAccount, String userPassword,String userNewPhone) {
        if (StringUtils.isAnyBlank(userAccount,userPassword,userNewPhone)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户名不可以小于4");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户密码不可以小于8");
        }
        if(userNewPhone.length() != 11){
            throw new BusinessException(ErrorCode.ERROR_PARAM, "手机号格式不对");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "账户不能包含特殊字符");
        }
        // 寻找账户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", userPassword);
        User user = usersMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("findPassword failed because user is null");
            throw new BusinessException(ErrorCode.ERROR_PARAM, "找不到用户");
        }
        user.setUserPhone(userNewPhone);
        int res = usersMapper.updateById(user);
        if(res > 0){
            return user;
        }
        else
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新手机号失败");
    }

    @Override
    public User userUpdateNickName(String userAccount, String NewNickName) {
        if (StringUtils.isAnyBlank(userAccount,NewNickName)) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "参数不可以为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.ERROR_PARAM, "用户名不可以小于4");
        }
        // 寻找账户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        User user = usersMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("findPassword failed because user is null");
            throw new BusinessException(ErrorCode.ERROR_PARAM, "找不到用户");
        }
        user.setNickName(NewNickName);
        int res = usersMapper.updateById(user);
        if(res > 0){
            User safetyUser = getSafetyUser(user);
            return safetyUser;
        }
        else
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新昵称失败");
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




