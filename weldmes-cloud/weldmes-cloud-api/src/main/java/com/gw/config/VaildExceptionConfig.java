package com.gw.config;

import com.gw.common.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@SuppressWarnings({"ALL"})
@Slf4j
@ControllerAdvice
public class VaildExceptionConfig {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpResult throwCustomException(MethodArgumentNotValidException methodArgumentNotValidException){

        log.error("[ 实体类异常捕获 ] " + methodArgumentNotValidException.getMessage());

        HttpResult responseBean = new HttpResult();
        responseBean.setCode(500);
        responseBean.setMsg(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());

        return responseBean;
    }
}
