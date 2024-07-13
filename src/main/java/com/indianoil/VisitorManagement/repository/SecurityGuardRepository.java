package com.indianoil.VisitorManagement.repository;


import com.indianoil.VisitorManagement.entity.SecurityGuardEntity;
import com.indianoil.VisitorManagement.interfaces.SecurityGuardGetAll;

import java.util.List;
import java.util.Optional;

import com.indianoil.VisitorManagement.model.SecurityGuardResponseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository  
public interface SecurityGuardRepository extends JpaRepository<SecurityGuardEntity, String> {
        Optional<SecurityGuardEntity> findById(String id);

        List<SecurityGuardGetAll> findAllProjectedBy();

        List<SecurityGuardGetAll> findByLocationCode(String locationCode);


        @Query(value = "SELECT CONCAT(first_name,' ',last_name) FROM SecurityGuardEntity WHERE id=:id")
        public String getsecurityguardname(@Param("id") String id);

        public SecurityGuardEntity findByMobileNumber(String num);
}