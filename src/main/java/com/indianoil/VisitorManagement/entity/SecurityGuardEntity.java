package com.indianoil.VisitorManagement.entity;

import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import javax.persistence.*;


@Entity
@Data
@Table(name="security_guard",schema = "suraksha")

public class SecurityGuardEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    @Column(unique=true)
    private String mobileNumber;
    private String locationCode;
    private int officer;
    private boolean status;
    private byte[] image;
    private String image_base64;
    private String fileType;
    private String role;
    public SecurityGuardEntity() {

    }
}

