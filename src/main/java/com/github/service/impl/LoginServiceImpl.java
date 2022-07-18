package com.github.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.github.common.Result;
import com.github.config.AppProperties;
import com.github.dto.LoginDto;
import com.github.dto.LoginReturnDto;
import com.github.dto.SendTotpDto;
import com.github.dto.VerifyTotpDto;
import com.github.entity.User;
import com.github.enums.MfaType;
import com.github.service.EmailService;
import com.github.service.LoginService;
import com.github.service.UserCacheService;
import com.github.utils.JwtUtil;
import com.github.utils.TotpUtil;
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

import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

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
    private UserCacheService userCacheService;

    @NonNull
    private EmailService emailService;

    @NonNull
    private AuthenticationManager authenticationManager;

    @NonNull
    private JwtUtil jwtUtil;

    @NonNull
    private TotpUtil totpUtil;

    @NonNull
    private AppProperties appProperties;

    @Override
    public Result<LoginReturnDto> login(LoginDto loginDto, HttpServletResponse response) {
        // 封装 token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        // 进行认证
        UsernamePasswordAuthenticationToken authenticate = (UsernamePasswordAuthenticationToken) this.authenticationManager.authenticate(token);
        User principal = (User) authenticate.getPrincipal();
        // 判断是否开启的 totp
        if (principal.getUsingMfa()) {
            String mfaId = userCacheService.cacheUser(principal);
            response.addHeader("x-Authenticate", "mfa");
            response.addHeader("x-Authenticate", "realm=" + mfaId);
            return Result.error("登录失败");
        } else { // 如果没有开启 totp，直接返回访问令牌和刷新令牌
            String username = principal.getUsername();
            Collection<GrantedAuthority> authorities = authenticate.getAuthorities();
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, loginDto.getPassword(), authorities);
            String accessToken = jwtUtil.createAccessToken(userDetails);
            String refreshToken = jwtUtil.createRefreshToken(userDetails);
            return Result.success(new LoginReturnDto().setAccessToken(accessToken).setRefreshToken(refreshToken), "登录成功");
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

    @Override
    public Result<String> sendTotp(SendTotpDto sendTotpDto) throws InvalidKeyException {

        Optional<User> optional = userCacheService.extractUser(sendTotpDto.getMfaId());
        if (optional.isPresent()) {
            User user = optional.get();
            String mfaKey = user.getMfaKey();
            Key key = totpUtil.decodeKeyFromString(mfaKey);
            String totp = totpUtil.createTotp(key, Instant.now());

            if (sendTotpDto.getMfaType().equals(MfaType.EMAIL)) {
                emailService.sendEmail(user.getEmail(), totp);
            }

            return Result.success("发送成功");
        }

        return Result.error("发送失败");
    }

    @Override
    public Result<LoginReturnDto> verifyTotp(VerifyTotpDto verifyTotpDto) {

        String mfaId = verifyTotpDto.getMfaId();

        userCacheService.verifyTotp(verifyTotpDto.getMfaId(), verifyTotpDto.getCode());

        return null;
    }

}
