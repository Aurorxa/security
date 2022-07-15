package com.github.utils;

import com.github.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.util.Date;
import java.util.List;
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
    public static final Key ACCESS_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    /**
     * 用于签名的刷新令牌的密钥
     */
    public static final Key REFRESH_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @NonNull
    private AppProperties appProperties;

    /**
     * 创建访问令牌
     *
     * @param userDetails 用户信息
     * @return 访问令牌
     */
    public String createAccessToken(UserDetails userDetails) {
        return createJwtToken(userDetails, appProperties.getJwt().getAccessTokenExpireTime(), ACCESS_KEY);
    }


    /**
     * 通过刷新令牌创建访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 访问令牌
     */
    public String createAccessTokenWithRefreshToken(String refreshToken) throws AccessDeniedException {
        // 从刷新令牌中解析出所需要的信息，并重新生成信息的访问令牌

        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(JwtUtil.REFRESH_KEY).build().parseClaimsJws(refreshToken).getBody();
            String subject = claims.getSubject();
            String id = claims.getId();
            List authorities = claims.get("authorities", List.class);
            return Jwts.builder()
                    .setId(id)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + appProperties.getJwt().getAccessTokenExpireTime()))
                    .claim("authorities", authorities)
                    .signWith(ACCESS_KEY, SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AccessDeniedException("访问被拒绝");
        }
    }

    /**
     * 创建刷新令牌
     *
     * @param userDetails 用户信息
     * @return 刷新令牌
     */
    public String createRefreshToken(UserDetails userDetails) {
        return createJwtToken(userDetails, appProperties.getJwt().getRefreshTokenExpireTime(), REFRESH_KEY);
    }

    /**
     * 校验访问令牌，不考虑过期的情况下。
     *
     * @param accessToken
     * @return
     */
    public Boolean validateAccessTokenWithoutExpiration(String accessToken) {
        return validateJwtToken(accessToken, ACCESS_KEY, false);
    }

    /**
     * 校验访问令牌
     *
     * @param accessToken
     * @return
     */
    public Boolean validateAccessToken(String accessToken) {
        return validateJwtToken(accessToken, ACCESS_KEY, true);
    }

    /**
     * 校验刷新令牌
     *
     * @param refreshToken
     * @return
     */
    public Boolean validateRefreshToken(String refreshToken) {
        return validateJwtToken(refreshToken, REFRESH_KEY, true);
    }


    /**
     * 验证 Jwt
     *
     * @param jwtToken token
     * @param key      密钥
     * @return
     */
    private Boolean validateJwtToken(String jwtToken, Key key, boolean isExpiredInvalid) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parse(jwtToken);
            return true;
        } catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            if (e instanceof ExpiredJwtException) {
                return !isExpiredInvalid;
            }
            return false;
        }
    }


    /**
     * 生成 Jwt
     *
     * @param userDetails 用户信息
     * @param key         密钥
     * @param expireTime  过期时间
     * @return Jwt Token
     */
    public String createJwtToken(UserDetails userDetails, Long expireTime, Key key) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("security")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireTime))
                .claim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }


}
