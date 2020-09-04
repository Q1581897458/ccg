package com.mr.common.exception;

import com.mr.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NbException extends RuntimeException{

    private ExceptionEnums exceptionEnums;

}
