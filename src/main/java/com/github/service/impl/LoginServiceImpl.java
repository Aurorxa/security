package com.github.service.impl;

import com.github.common.Result;
import com.github.dto.LoginDto;
import com.github.service.LoginService;
import com.github.utils.JwtUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 10:24:01
 */
@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    @NonNull
    private AuthenticationManager authenticationManager;

    @NonNull
    private JwtUtil jwtUtil;

    @Override
    public Result login(LoginDto loginDto) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        try {
            UsernamePasswordAuthenticationToken authenticate = (UsernamePasswordAuthenticationToken) this.authenticationManager.authenticate(token);
            String username = ((UserDetails) authenticate.getPrincipal()).getUsername();
            Collection<GrantedAuthority> authorities = authenticate.getAuthorities();
            User userDetails = new User(username, loginDto.getPassword(), authorities);
            String accessToken = jwtUtil.createAccessToken(userDetails);
            String refreshToken = jwtUtil.createRefreshToken(userDetails);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("accessToken", accessToken);
            resultMap.put("refreshToken", refreshToken);

            return Result.ok(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}
