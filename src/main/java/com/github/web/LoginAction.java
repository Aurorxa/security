package com.github.web;

import com.github.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 09:45:29
 */
@Slf4j
@Validated
@RestController
public class LoginAction {

    @PostMapping("/login")
    public void login(@RequestBody @Validated LoginDto loginDto){

    }

}
