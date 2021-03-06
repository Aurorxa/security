package com.github.web;

import com.github.common.Result;
import com.github.dto.LoginDto;
import com.github.dto.LoginReturnDto;
import com.github.dto.SendTotpDto;
import com.github.dto.VerifyTotpDto;
import com.github.service.LoginService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.security.InvalidKeyException;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 09:45:29
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class LoginAction {

    @NonNull
    private LoginService loginService;

    /**
     * 登录
     *
     * @param loginDto
     * @return
     */
    @PostMapping("/auth/login")
    public Result<LoginReturnDto> login(@RequestBody @Validated LoginDto loginDto, HttpServletResponse response) {
        return loginService.login(loginDto, response);
    }

    /**
     * 发送 Totp
     *
     * @param sendTotpDto
     * @return
     */
    @PostMapping("/auth/sendTotp")
    public Result<String> sendTotp(@RequestBody @Validated SendTotpDto sendTotpDto) throws InvalidKeyException {
        return loginService.sendTotp(sendTotpDto);
    }

    /**
     * 验证 totp
     *
     * @param verifyTotpDto
     * @return
     * @throws InvalidKeyException
     */
    @PostMapping("/auth/verifyTotp")
    public Result<LoginReturnDto> verifyTotp(@RequestBody @Validated VerifyTotpDto verifyTotpDto) throws InvalidKeyException {
        return loginService.verifyTotp(verifyTotpDto);
    }

    /**
     * 刷新令牌
     *
     * @param authorization 请求头中存放访问令牌
     * @param refreshToken  刷新令牌
     * @return
     * @throws AccessDeniedException
     */
    @PostMapping("/auth/refresh")
    public Result<LoginReturnDto> refresh(@RequestHeader(name = "Authorization") String authorization, @RequestParam String refreshToken) throws AccessDeniedException {
        return loginService.refresh(authorization, refreshToken);
    }


}
