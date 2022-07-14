package com.github.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 16:03:48
 */
public class PhoneAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PhoneAuthenticationToken.class);
    }
}
