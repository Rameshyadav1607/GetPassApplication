package com.indianoil.VisitorManagement.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Data
@Table(name="settings",schema = "suraksha")
public class SettingsEntity {
    @Id
    private String loc_code;
    private String[] designations;
}
