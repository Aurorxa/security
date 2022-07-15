package com.github.web;

import cn.hutool.core.util.StrUtil;
import com.github.common.Result;
import com.github.config.AppProperties;
import com.github.dto.LoginDto;
import com.github.service.LoginService;
import com.github.utils.JwtUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

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

    @NonNull
    private AppProperties properties;

    @NonNull
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result login(@RequestBody @Validated LoginDto loginDto) {
        return loginService.login(loginDto);
    }

    @PostMapping("/token/refresh")
    public Result tokenRefresh(@RequestHeader(name = "Authorization") String authorization, @RequestParam String refreshToken) throws AccessDeniedException {
        String accessToken = StrUtil.removePrefix(authorization, properties.getJwt().getPrefix());
        if (jwtUtil.validateRefreshToken(refreshToken) && jwtUtil.validateAccessTokenWithoutExpiration(accessToken)) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("accessToken", jwtUtil.createAccessTokenWithRefreshToken(refreshToken));
            resultMap.put("refreshToken", refreshToken);
            return Result.ok(resultMap);
        }
        return Result.ok();
    }


}
