package com.slt.slt.Result;

import java.io.Serializable;

/**
 * 后端统一返回的结果
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化版本号

    private Integer code; // 状态码  1:成功  0:失败
    private String msg; // 提示信息
    private T data; // 数据

    // 成功
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 1;
        return result;
    }

    // 成功
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.data = object;
        return result;
    }

    // 失败
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = 0;
        result.msg = msg;
        return result;
    }

    // Getter 和 Setter 方法
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}