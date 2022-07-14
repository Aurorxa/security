package com.github.provider;

import com.github.service.UserService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 16:03:48
 */
@Data
public class PhoneAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取手机号
        String phone = authentication.getName();
        // 获取密码
        String password = (String) authentication.getCredentials();



        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(PhoneAuthenticationToken.class);
    }



}
