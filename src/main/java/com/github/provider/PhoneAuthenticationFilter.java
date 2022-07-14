package com.github.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 16:46:21
 */
public class PhoneAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper;

    public PhoneAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/authorize/mobile", "POST"));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        PhoneAuthenticationToken authRequest;
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(request.getInputStream());
            String phone = jsonNode.get("phone").textValue();
            String code = jsonNode.get("code").textValue();
            authRequest = new PhoneAuthenticationToken(phone, code);
        } catch (Exception e) {
            throw new BadCredentialsException("手机号或验证码不存在");
        }

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
