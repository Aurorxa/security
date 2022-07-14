package com.github.web;

import com.github.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 10:22:04
 */
@Slf4j
@Validated
@RestController
public class RegisterAction {

    @PostMapping("/register")
    public UserDto register(@Validated @RequestBody UserDto user, Authentication authentication) {
        log.info("user = {}", user);
        log.info("authentication = {}", authentication);
        return user;
    }




}
