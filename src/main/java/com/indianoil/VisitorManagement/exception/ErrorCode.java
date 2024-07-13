package com.indianoil.VisitorManagement.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation Error!!!"),
    SERVICE_CONFLICT(HttpStatus.BAD_REQUEST, "Multiple Services exist for same type"),

    LOCATION_DOES_NOT_EXISTS(HttpStatus.BAD_REQUEST, "Location does not exist"),

    ZONE_DOES_NOT_EXISTS(HttpStatus.BAD_REQUEST, "Entity does not exist"),
    MEDIA_TYPE_NOT_SUPPORTED(UNSUPPORTED_MEDIA_TYPE,"Media type not supported"),
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "Internal Server Error"),
    FORBIDDEN_ERROR(FORBIDDEN, "forbidden access"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND,"Entity Not Found");


    private final HttpStatus httpStatus;
    private final String message;

}
