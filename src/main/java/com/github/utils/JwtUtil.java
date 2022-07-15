package com.github.utils;

import com.github.config.AppProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 20:27:56
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    /**
     * 用于签名的访问令牌的密钥
     */
    public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    /**
     * 用于签名的刷新令牌的密钥
     */
    public static final Key REFRESH_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @NonNull
    private AppProperties appProperties;

    /**
     * 创建访问令牌
     *
     * @param userDetails
     * @return
     */
    public String createAccessToken(UserDetails userDetails) {
        return createJwtToken(userDetails, appProperties.getJwt().getAccessTokenExpireTime(), KEY);
    }


    /**
     * 创建刷新令牌
     *
     * @param userDetails
     * @return
     */
    public String createRefreshToken(UserDetails userDetails) {
        return createJwtToken(userDetails, appProperties.getJwt().getRefreshTokenExpireTime(), REFRESH_KEY);
    }

    /**
     * 生成 Jwt
     *
     * @param userDetails
     * @param key         密钥
     * @param expireTime  过期时间
     * @return
     */
    public String createJwtToken(UserDetails userDetails, Long expireTime, Key key) {
        long now = System.currentTimeMillis();
        return Jwts.builder().setId("security").claim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).setSubject(userDetails.getUsername()).setIssuedAt(new Date(now)).setExpiration(new Date(now + expireTime)).signWith(key, SignatureAlgorithm.HS512).compact();
    }

}
