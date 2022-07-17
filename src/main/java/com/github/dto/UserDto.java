package com.github.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 10:22:22
 */
@Data
public class UserDto implements Serializable {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String checkPassword;

    private String realName;

    private String nickName;

    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;

    private String mobile;
}
