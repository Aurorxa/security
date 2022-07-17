package com.github.service;

import com.github.common.Result;
import com.github.dto.UserDto;
import com.github.entity.User;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 16:26:58
 */
public interface UserService {

    /**
     * 根据手机号查询用户信息
     *
     * @param phone
     * @return
     */
    User findByPhone(String phone);

    /**
     * 校验用户名是否存在
     *
     * @param username
     * @return
     */
    boolean checkUsernameExisted(String username);

    /**
     * 校验邮件是否存在
     *
     * @param email
     * @return
     */
    boolean checkEmailExisted(String email);

    /**
     * 校验手机号码是否存在
     *
     * @param mobile
     * @return
     */
    boolean checkMobileExisted(String mobile);

    /**
     * 注册
     *
     * @param user
     * @return
     */
    Result<?> register(UserDto user);
}
