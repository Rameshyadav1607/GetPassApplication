package com.indianoil.VisitorManagement.exception.custom_exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Slf4j
public class DocumentNotFoundException extends Exception {

    public static final long serialVersionUID = 1L;


    public DocumentNotFoundException(String message) {

        super(message);
        if (log.isErrorEnabled()){
            log.error("DocumentNotFoundException: {}", message);
        }
    }
    public DocumentNotFoundException(){
        super();
    }
}
