package com.indianoil.VisitorManagement.repository;

import com.indianoil.VisitorManagement.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
    @Query(value="SELECT u  FROM AdminEntity u WHERE emp_code =:id")
    public AdminEntity getEmployeerole(@Param("id") int id);

}
