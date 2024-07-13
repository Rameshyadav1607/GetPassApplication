package com.indianoil.VisitorManagement.repository;

import com.indianoil.VisitorManagement.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity,String> {
    @Query(value = "select * from suraksha.otp WHERE emp_code=:id ORDER BY id DESC LIMIT 1",nativeQuery = true)
    OtpEntity findlastbyid(@Param("id") String id);


}
