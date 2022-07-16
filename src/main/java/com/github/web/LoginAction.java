package com.github.web;

import com.github.common.Result;
import com.github.dto.LoginDto;
import com.github.dto.LoginReturnDto;
import com.github.service.LoginService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

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
    @PostMapping("/login")
    public Result<LoginReturnDto> login(@RequestBody @Validated LoginDto loginDto) {
        return loginService.login(loginDto);
    }

    /**
     * 刷新令牌
     *
     * @param authorization 请求头中存放访问令牌
     * @param refreshToken  刷新令牌
     * @return
     * @throws AccessDeniedException
     */
    @PostMapping("/token/refresh")
    public Result<LoginReturnDto> tokenRefresh(@RequestHeader(name = "Authorization") String authorization, @RequestParam String refreshToken) throws AccessDeniedException {
        return loginService.tokenRefresh(authorization, refreshToken);
    }


}
