package com.github.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
public class JwtUtil {

    /**
     * 用于签名
     */
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * 生成 Jwt
     *
     * @param userDetails
     * @return
     */
    public String createJwtToken(UserDetails userDetails) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("security")
                .claim("authorities", userDetails.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 60000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

}
