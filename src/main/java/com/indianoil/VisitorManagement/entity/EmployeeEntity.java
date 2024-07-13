package com.indianoil.VisitorManagement.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Data
//@Table(name="cem",schema = "cem_genpsqldev")
@Table(name="cem",schema = "cem_genpsqldev")
public class EmployeeEntity {
    @Id
    private int emp_code;
    private String loc_code;
    private String loc_name;
    private String emp_name;
    private String emp_status;
    private String email_id;
    private String mobile_no;
    @Transient
    private String role;
    private String curr_comp_code;
    private String curr_comp;
    private String designation;
    private String locn_ic_yn;



}
