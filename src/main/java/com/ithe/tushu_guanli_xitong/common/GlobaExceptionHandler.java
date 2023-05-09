package com.ithe.tushu_guanli_xitong.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

/*
全局异常处理
* */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobaExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//处理这种sql异常
    public R<String> exceptionhandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = "失败了!"+split[2]+"太受欢迎啦！请重新输入！";
            return R.error(msg);
        }
        return R.error("未知错误！");
    }
}
