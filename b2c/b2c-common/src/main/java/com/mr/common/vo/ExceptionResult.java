package com.mr.common.vo;

import com.mr.common.enums.ExceptionEnums;
import lombok.Data;

import java.util.Date;

//优化返回格式
@Data
public class ExceptionResult {


    private Integer code;

    private String msg;

    private Long timestamp;

    private Date date;

    private String path;

    private String error;

    public ExceptionResult(ExceptionEnums enums){
        this.code = enums.getCode();
        this.msg = enums.getMsg();
        this.timestamp = System.currentTimeMillis();
        this.date = new Date();
    }

}
