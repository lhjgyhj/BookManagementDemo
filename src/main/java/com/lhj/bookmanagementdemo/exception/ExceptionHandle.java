package com.lhj.bookmanagementdemo.exception;

import cn.hutool.core.util.StrUtil;
import com.lhj.bookmanagementdemo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
//全局异常处理
@RestControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    public Result exceptionError(Exception e){
        log.error("系统异常{}",e);
        return Result.error("系统错误");

    }
    @ExceptionHandler(value = ServiceException.class)
    public Result ServiceExceptionError(ServiceException e){
        log.error("业务异常{}",e);
        String code = e.getCode();
        if (StrUtil.isNotBlank( code)){
            return Result.error(code,e.getMessage());
        }
        return Result.error(e.getMessage());

    }
}
