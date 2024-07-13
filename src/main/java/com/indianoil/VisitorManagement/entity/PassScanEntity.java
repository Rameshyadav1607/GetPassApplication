package com.indianoil.VisitorManagement.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@Table(name="pass_scan",schema = "suraksha")
public class PassScanEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(
            name = "system-uuid",
            strategy = "uuid",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "202200000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private String id;
    private String visitor;
    private String inTime;
    private String outTime;
    private LocalDate date;
    private String gate;
    private String activity;
    private String remarks;
}
