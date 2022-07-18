package com.github.service;

import com.github.common.Result;
import com.github.dto.LoginDto;
import com.github.dto.LoginReturnDto;
import com.github.dto.SendTotpDto;
import com.github.dto.VerifyTotpDto;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.security.InvalidKeyException;

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
     * @param response
     * @return
     */
    Result<LoginReturnDto> login(LoginDto loginDto, HttpServletResponse response);

    /**
     * 刷新令牌
     *
     * @param authorization
     * @param refreshToken
     * @return
     * @throws AccessDeniedException;
     */
    Result<LoginReturnDto> refresh(String authorization, String refreshToken) throws AccessDeniedException;

    /**
     * 发送 Totp
     *
     * @param sendTotpDto
     * @return
     * @throws InvalidKeyException;
     */
    Result<String> sendTotp(SendTotpDto sendTotpDto) throws InvalidKeyException;

    Result<LoginReturnDto> verifyTotp(VerifyTotpDto verifyTotpDto) throws InvalidKeyException;

}
