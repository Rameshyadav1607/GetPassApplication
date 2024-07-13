package com.indianoil.VisitorManagement.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SystemException extends RuntimeException {

    private final ErrorCode errorCode;
    private List<com.indianoil.VisitorManagement.exception.Error> errors;

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SystemException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }


    public SystemException(ErrorCode errorCode,List<com.indianoil.VisitorManagement.exception.Error> errorList) {
        super("");
        this.errorCode = errorCode;
        errors = errorList;
    }

}
