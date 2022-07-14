package com.github.service;

import com.github.entity.User;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 16:26:58
 */
public interface UserService {

    /**
     * 根据手机号和验证码查询用户信息
     *
     * @param phone
     * @param code
     * @return
     */
    User findByPhoneAndCode(String phone, String code);

}
