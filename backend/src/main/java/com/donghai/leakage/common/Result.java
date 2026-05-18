package com.donghai.leakage.common;

public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    private Result() {
    }

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 成功：200
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }
    
    // 成功，data
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // 请求错误：400
    public static <T> Result<T> fail(String message) {
        return new Result<>(400, message, null);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    // 未登录：401
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    // 无权限：403
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }

    // 数据不存在：404
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    // 系统错误
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}