package com.indianoil.VisitorManagement.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = -1408423962244403705L;

    private List<com.indianoil.VisitorManagement.exception.Error> errors = new ArrayList<>();
}
