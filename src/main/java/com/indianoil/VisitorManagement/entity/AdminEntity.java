package com.indianoil.VisitorManagement.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name="admin",schema = "suraksha")
public class AdminEntity {

    @Id
    private int emp_code;
    private boolean otp;
    private boolean super_admin;
    private String role;
    private String loc_code;


}
