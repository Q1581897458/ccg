package com.mr.common.advice;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.NbException;
import com.mr.common.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.xml.ws.Response;

//捕捉异常公共类
@ControllerAdvice
public class CommonExceptionHandler {

    //拦截指定一常 范围高拦截广
    @ExceptionHandler(NbException.class)
    public ResponseEntity<ExceptionResult> exceptionHadler(NbException ne){

       ExceptionEnums a= ne.getExceptionEnums();

        System.out.println("异常了"+ne.getExceptionEnums());
        return ResponseEntity.status(a.getCode()).body(new ExceptionResult(a));
    }


}
