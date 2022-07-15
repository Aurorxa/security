package com.github.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.config.AppProperties;
import com.github.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 09:12:46
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @NonNull
    private AppProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkJwtToken(request)) {
            Optional<Claims> optional = validateJwtToken(request).filter(claims -> ObjectUtil.isNotEmpty(claims.get("authorities")));
            if (optional.isPresent()) {
                Claims claims = optional.get();
                List<? extends GrantedAuthority> authorities = claims.get("authorities", List.class);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }


    private Optional<Claims> validateJwtToken(HttpServletRequest request) {
        String jwtToken = StrUtil.removePrefix(request.getHeader(properties.getJwt().getHeader()), properties.getJwt().getPrefix());
        try {
            return Optional.of(Jwts.parserBuilder().setSigningKey(JwtUtil.ACCESS_KEY).build().parseClaimsJws(jwtToken).getBody());
        } catch (Exception e) {
            log.info("JwtFilter.validateJwtToken == {}", e);
            return Optional.empty();
        }
    }


    /**
     * 判断请求中是否含有的 jwt Token
     *
     * @param request 请求
     * @return 如果存在，返回 true；如果不存在，返回 false
     */
    private boolean checkJwtToken(HttpServletRequest request) {
        String header = request.getHeader(properties.getJwt().getHeader());
        return CharSequenceUtil.isNotBlank(header) && header.startsWith(properties.getJwt().getPrefix());
    }
}
