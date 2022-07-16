package com.github.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-16 21:47:11
 */
@Accessors(chain = true)
@Data
public class LoginReturnDto implements Serializable {
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;

}
