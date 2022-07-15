package com.github.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 09:46:12
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    SUCCESS(20000, "成功", true),
    ERROR(20001, "失败", false);

    private Integer code;

    private String message;

    private Boolean flag;
}