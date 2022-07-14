package com.github.service.impl;

import com.github.dao.UserRepository;
import com.github.entity.User;
import com.github.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 16:27:08
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @NonNull
    private UserRepository userRepository;

    @Override
    public User findByPhoneAndCode(String phone, String code) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(code);

        return userRepository.findOne(Example.of(user, ExampleMatcher.matching()
                        .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.exact())
                        .withMatcher("password", ExampleMatcher.GenericPropertyMatchers.exact())
                )
        ).orElse(null);
    }
}