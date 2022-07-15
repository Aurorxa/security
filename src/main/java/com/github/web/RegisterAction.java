package com.github.web;

import com.github.dto.UserDto;
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
@RestController
public class RegisterAction {

    @PostMapping("/register")
    public UserDto register(@Validated @RequestBody UserDto user) {
        log.info("user = {}", user);
        // TODO 1：确保 username、email、mobile 唯一，需要去数据库中校验
        // TODO 2：我们需要将 userDto 转换为 user ，并分配一个默认的角色（ROLE_USER）
        return user;
    }

    @GetMapping(value = "/api/demo")
    public String demo(){
        return "demo";
    }




}
