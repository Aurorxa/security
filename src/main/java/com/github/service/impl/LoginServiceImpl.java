package com.github.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.github.common.Result;
import com.github.config.AppProperties;
import com.github.dto.LoginDto;
import com.github.dto.LoginReturnDto;
import com.github.entity.User;
import com.github.service.LoginService;
import com.github.utils.JwtUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
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
        // 封装 token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        // 进行认证
        UsernamePasswordAuthenticationToken authenticate = (UsernamePasswordAuthenticationToken) this.authenticationManager.authenticate(token);
        User principal = (User) authenticate.getPrincipal();
        // 判断是否开启的 totp
        if (principal.getUsingMfa()) {
            return Result.success();
        } else { // 如果没有开启 totp，直接返回访问令牌和刷新令牌
            String username = principal.getUsername();
            Collection<GrantedAuthority> authorities = authenticate.getAuthorities();
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, loginDto.getPassword(), authorities);
            String accessToken = jwtUtil.createAccessToken(userDetails);
            String refreshToken = jwtUtil.createRefreshToken(userDetails);
            return Result.success(new LoginReturnDto().setAccessToken(accessToken).setRefreshToken(refreshToken));
        }

    }

    @Override
    public Result<LoginReturnDto> refresh(String authorization, String refreshToken) throws AccessDeniedException {
        String accessToken = CharSequenceUtil.removePrefix(authorization, appProperties.getJwt().getPrefix());
        if (jwtUtil.validateRefreshToken(refreshToken) && jwtUtil.validateAccessTokenWithoutExpiration(accessToken)) {
            return Result.success(new LoginReturnDto().setAccessToken(jwtUtil.createAccessTokenWithRefreshToken(refreshToken)).setRefreshToken(refreshToken));
        }
        return Result.error();
    }
}
