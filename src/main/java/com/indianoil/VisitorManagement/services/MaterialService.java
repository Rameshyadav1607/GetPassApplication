package com.indianoil.VisitorManagement.services;

import com.indianoil.VisitorManagement.entity.MaterialEntity;
import com.indianoil.VisitorManagement.repository.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MaterialService {
//    private final MaterialRepository materialRepository;


//    public MaterialService(MaterialRepository materialRepository) {
//        this.materialRepository = materialRepository;
//    }
//    public void update(String material_id, String visitorid){
//         materialRepository.findById(material_id)
//                .map(materialEntity -> {
//                    return materialRepository.save(materialEntity);
//                });
//    }


//    public String save(
//    String material_type,
//    String material_description,
//    String po_number,
//    String vendor_code,
//    String vendor_name)
//    {
//        System.out.println("inside material service " + material_description);
//        MaterialEntity materialEntity= new MaterialEntity();
//        materialEntity.setMaterial_description(material_description);
//        materialEntity.setPo_number(po_number);
//        materialEntity.setMaterial_type(material_type);
//        materialEntity.setVendor_name(vendor_name);
//        materialEntity.setVendor_code(vendor_code);
//        return materialRepository.save(materialEntity).getId();
//    }
}
