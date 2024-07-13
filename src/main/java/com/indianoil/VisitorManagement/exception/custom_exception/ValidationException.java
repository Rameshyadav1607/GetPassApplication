package com.indianoil.VisitorManagement.exception.custom_exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Slf4j
public class ValidationException extends Exception{

    public static final long serialVersionUID = 1L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
        if (log.isErrorEnabled()){
            log.error("CustomValidationException: {}", message);
        }
    }
}
