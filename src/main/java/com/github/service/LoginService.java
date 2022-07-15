package com.github.service;

import com.github.common.Result;
import com.github.dto.LoginDto;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 10:23:53
 */
public interface LoginService {
    /**
     * 登录
     *
     * @param loginDto
     * @return
     */
    Result login(LoginDto loginDto);
}
