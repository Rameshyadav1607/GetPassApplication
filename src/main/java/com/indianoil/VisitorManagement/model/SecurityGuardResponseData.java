package com.indianoil.VisitorManagement.model;

import lombok.Data;

@Data
public class SecurityGuardResponseData {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private long mobileNumber;
    private String locationCode;
    private boolean status;
    private String downloadURL;
    private String filetype;
    private String filename;
}
