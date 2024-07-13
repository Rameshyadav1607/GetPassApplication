package com.indianoil.VisitorManagement.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="otp",schema = "suraksha")
public class OtpEntity {
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
    private String emp_code;
    private int otp;
    private LocalDateTime created_at;
    private LocalDateTime expiration_at;


}
