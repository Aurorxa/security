package com.github.provider;

import com.github.entity.User;
import com.github.service.UserService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

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
        String code = (String) authentication.getCredentials();

        if (StringUtils.isEmpty(phone)) {
            throw new UsernameNotFoundException("手机号不可以为空");
        }

        if (StringUtils.isEmpty(code)) {
            throw new BadCredentialsException("验证码不能为空");
        }

        // 根据手机号、验证码查询用户信息
        User user = userService.findByPhoneAndCode(phone, code);

        return new PhoneAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(PhoneAuthenticationToken.class);
    }


}
