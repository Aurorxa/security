package com.github.provider;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 16:15:23
 */
public class PhoneAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 手机号
     */
    private Object phone;
    /**
     * 密码
     */
    private Object credentials;


    public PhoneAuthenticationToken(Object phone, Object credentials) {
        super(null);
        this.phone = phone;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public PhoneAuthenticationToken(Object phone, Object credentials,
                                    Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = phone;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }


    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.phone;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }


}
