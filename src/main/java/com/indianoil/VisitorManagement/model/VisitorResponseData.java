package com.indianoil.VisitorManagement.model;

import com.indianoil.VisitorManagement.entity.MaterialEntity;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
public class VisitorResponseData {

    private String id;
    private String age;
    private String name;
    private String type;
    private String gender;
    private String date;
    private String time;
    private String in_time;
    private String out_ime;
    private String dob;
    private String email;
    private String number;
    private String identity_type;
    private String identity_number;
    private String address;
    private String reporting_officer;
    private String purpose;
    private String vac;
    private String status;
    private String security_guard;
    private String pass_validity;
    private MaterialEntity Material;
    private String loc_code;
    private String filetype;
    private String filename;
    private String downloadURL;

}
