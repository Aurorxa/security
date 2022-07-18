package com.github.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-18 11:24:23
 */
@Data
public class VerifyTotpDto implements Serializable {

    /**
     * mfaId
     */
    @NotBlank(message = "mfaId不能为空")
    private String mfaId;

    /**
     * Mfa Code
     */
    @NotNull(message = "mfa不能为空")
    private String code;


}
