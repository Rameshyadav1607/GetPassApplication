package com.indianoil.VisitorManagement.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.indianoil.VisitorManagement.exception.custom_exception.DocumentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestControllerAdvice
@Slf4j
public class SystemExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorResponse> handleSystemException(SystemException ex) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(ex.getErrorCode(), "", ex.getMessage()));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(ErrorCode.INTERNAL_ERROR, ex.getMessage(), "Internal Server Error"));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public final ResponseEntity<ErrorResponse> handleValidationExceptions(WebExchangeBindException exception) {
        List<Error> errors = new ArrayList<>();

        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.add(new Error(ErrorCode.VALIDATION_ERROR, error.getField(), error.getDefaultMessage())));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        List<Error> errors = new ArrayList<>();

        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.add(new Error(ErrorCode.VALIDATION_ERROR, error.getField(), error.getDefaultMessage())));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTraceIdException(ServerWebInputException ex) {
        List<Error> errors = new ArrayList<>();

        MethodParameter methodParameter = ex.getMethodParameter();
        if (methodParameter != null) {
            if (Objects.requireNonNull(ex.getMessage()).contains("Missing Header")) {
                errors.add(new Error(ErrorCode.VALIDATION_ERROR, methodParameter.getParameterName(), "Mandatory Header Missing"));
            } else {
                errors.add(new Error(ErrorCode.VALIDATION_ERROR, methodParameter.getParameterName(),
                        methodParameter.getParameterName() + "Invalid Missing"));

            }
        }
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestDataType(JsonProcessingException exception) {

        List<Error> errors = new ArrayList<>();
        errors.add(new Error(ErrorCode.VALIDATION_ERROR, "", "Malformed Request"));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    public ResponseEntity<ErrorResponse> handleUnSupportedMediaException(UnsupportedMediaTypeStatusException ex) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(ErrorCode.MEDIA_TYPE_NOT_SUPPORTED, "", "Media Type Not Supported"));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }


    @ExceptionHandler(TransformerException.class)
    public ResponseEntity<ErrorResponse> handleTransformerException(TransformerException ex) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(ErrorCode.INTERNAL_ERROR, "", ex.getMessage()));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SAXParseException.class)
    public ResponseEntity<ErrorResponse> handleSAXParseException(SAXParseException ex) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(ErrorCode.INTERNAL_ERROR, "", ex.getLineNumber() + ex.getLineNumber() + ex.getMessage()));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(NotAcceptableStatusException.class)
    public ResponseEntity<ErrorResponse> handleNotAcceptableStatusException(NotAcceptableStatusException ex) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(ErrorCode.MEDIA_TYPE_NOT_SUPPORTED, "", "Media type not supported"));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ErrorResponse> documentNotFoundException(DocumentNotFoundException ex) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(ErrorCode.ENTITY_NOT_FOUND, ErrorCode.ENTITY_NOT_FOUND.getMessage(), ex.getMessage()));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }


}
