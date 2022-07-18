package com.github.common;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 09:46:12
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    public static Boolean SUCCESS_STATUS = true;

    public static Boolean ERROR_STATUS = false;

    private Boolean status;

    private T data;

    private String msg;

    private String timestamp = DateUtil.now();


    public static <T> Result<T> success(T data, String msg) {
        Result<T> result = new Result<>();
        result.setStatus(Result.SUCCESS_STATUS);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(T data) {
        return success(data, "操作成功");
    }

    public static <T> Result<T> success() {
        return success(null, "操作成功");
    }

    public static <T> Result<T> success(String msg) {
        return success(null, msg);
    }

    public static <T> Result<T> error(T data, String msg) {
        Result<T> result = new Result<>();
        result.setStatus(Result.ERROR_STATUS);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String msg) {
        return error(null, msg);
    }

    public static <T> Result<T> error() {
        return error(null, "操作失败");
    }

}