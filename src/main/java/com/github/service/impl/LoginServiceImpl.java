package com.github.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.common.Result;
import com.github.config.AppProperties;
import com.github.dto.LoginDto;
import com.github.dto.LoginReturnDto;
import com.github.service.LoginService;
import com.github.utils.JwtUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Collection;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 10:24:01
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    @NonNull
    private AuthenticationManager authenticationManager;

    @NonNull
    private JwtUtil jwtUtil;

    @NonNull
    private AppProperties appProperties;

    @Override
    public Result<LoginReturnDto> login(LoginDto loginDto) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        try {
            UsernamePasswordAuthenticationToken authenticate = (UsernamePasswordAuthenticationToken) this.authenticationManager.authenticate(token);
            String username = ((UserDetails) authenticate.getPrincipal()).getUsername();
            Collection<GrantedAuthority> authorities = authenticate.getAuthorities();
            User userDetails = new User(username, loginDto.getPassword(), authorities);
            String accessToken = jwtUtil.createAccessToken(userDetails);
            String refreshToken = jwtUtil.createRefreshToken(userDetails);
            return Result.success(new LoginReturnDto().setAccessToken(accessToken).setRefreshToken(refreshToken));
        } catch (Exception e) {
            log.info("LoginServiceImpl.login == {}", e);
            e.printStackTrace();
            return Result.error();
        }
    }

    @Override
    public Result<LoginReturnDto> tokenRefresh(String authorization, String refreshToken) throws AccessDeniedException {
        String accessToken = StrUtil.removePrefix(authorization, appProperties.getJwt().getPrefix());
        if (jwtUtil.validateRefreshToken(refreshToken) && jwtUtil.validateAccessTokenWithoutExpiration(accessToken)) {
            return Result.success(new LoginReturnDto().setAccessToken(jwtUtil.createAccessTokenWithRefreshToken(refreshToken)).setRefreshToken(refreshToken));
        }
        return Result.error();
    }
}
