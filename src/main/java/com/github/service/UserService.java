package com.github.service;

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

}
