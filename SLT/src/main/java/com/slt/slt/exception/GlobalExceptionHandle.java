package com.slt.slt.exception;

import com.slt.slt.Result.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// 全局异常处理
@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(Exception.class)
    // 异常处理方法
    public Result HandleException(Exception e){
        // 打印异常信息
        e.printStackTrace();
        // 返回结果
        return Result.error(StringUtils.hasLength(e.getMessage())?e.getMessage():"操作失败!" );

    }
}
