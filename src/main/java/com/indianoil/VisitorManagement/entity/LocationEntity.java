package com.indianoil.VisitorManagement.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name="cem",schema = "cem_genpsqldev")
public class LocationEntity {
    @Id
    private int emp_code;
    private String loc_code;
    private String curr_comp_code;
    private String curr_comp;
    private String loc_name;
}