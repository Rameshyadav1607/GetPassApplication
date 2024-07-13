package com.indianoil.VisitorManagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name="visitor",schema = "suraksha")
public class VisitorEntity {
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
    private String name;
    private String type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String time;


    private int age;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate pass_validity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="fk_material_id")
    private MaterialEntity Material;
    private String loc_code;
    private String image_base64;
    private byte[] image;
    private String fileType;

    private String gate;
    private String latestTransactionalID;
    private String updated_by;
    private String pass_update_remarks;

}
