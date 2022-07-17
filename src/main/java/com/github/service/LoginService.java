package com.github.service;

import com.github.common.Result;
import com.github.dto.LoginDto;
import com.github.dto.LoginReturnDto;

import java.nio.file.AccessDeniedException;

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
    Result<LoginReturnDto> login(LoginDto loginDto);

    /**
     * 刷新令牌
     *
     * @param authorization
     * @param refreshToken
     * @return
     * @throws AccessDeniedException;
     */
    Result<LoginReturnDto> refresh(String authorization, String refreshToken) throws AccessDeniedException;
}
