package com.ceair.hotel.common.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应包装
 */
@Data
public class R<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    public R() {}

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<>(200, "success", null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> ok(String message, T data) {
        return new R<>(200, message, data);
    }

    public static <T> R<T> fail(String message) {
        return new R<>(500, message, null);
    }

    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null);
    }

    public boolean isSuccess() {
        return this.code == 200;
    }
}
