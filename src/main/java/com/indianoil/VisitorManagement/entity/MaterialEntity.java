package com.indianoil.VisitorManagement.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Data
@Table(name="Material",schema = "suraksha")
public class MaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="material_id")
    private int id;
    private String material_type;
    private String material_description;
    private String po_number;
    private String vendor_code;
    private String vendor_name;

}
