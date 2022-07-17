package com.github.web;

import com.github.common.Result;
import com.github.dto.UserDto;
import com.github.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequiredArgsConstructor
@RestController
public class RegisterAction {

    @NonNull
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody UserDto user) {
        log.info("user = {}", user);
        return userService.register(user);
    }

    @GetMapping(value = "/api/demo")
    public String demo() {
        return "demo";
    }
}
