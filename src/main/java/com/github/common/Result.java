package com.github.common;

import lombok.Data;
import lombok.With;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 09:46:12
 */
@With
@Data
public class Result {

    private Integer code;
    private Boolean success;
    private String message;

    private Map<String, Object> data = new HashMap<>();

    private Result() {

    }

    public static Result ok() {
        Result result = new Result();
        // 硬编码 : 枚举类 : 代码规范的
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setSuccess(ResultEnum.SUCCESS.getFlag());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setCode(ResultEnum.ERROR.getCode());
        result.setSuccess(ResultEnum.ERROR.getFlag());
        result.setMessage(ResultEnum.ERROR.getMessage());
        return result;
    }

}