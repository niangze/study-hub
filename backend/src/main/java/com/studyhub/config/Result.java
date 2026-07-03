package com.studyhub.config;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private T data;
    private String message;
    
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setData(data);
        r.setMessage("ok");
        return r;
    }
    
    public static <T> Result<T> success() {
        return success(null);
    }
    
    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMessage(message);
        return r;
    }
}
