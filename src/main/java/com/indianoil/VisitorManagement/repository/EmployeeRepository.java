package com.indianoil.VisitorManagement.repository;

import com.indianoil.VisitorManagement.entity.EmployeeEntity;
import com.indianoil.VisitorManagement.interfaces.GetEmployeeByLocCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    @Query(value="SELECT emp_name as emp_name,emp_code as emp_code,designation as designation FROM EmployeeEntity WHERE loc_code=:loc and emp_status = 'Active'")
    public List<GetEmployeeByLocCode> findemployeeatloc(@Param("loc") String loc);

    @Query(value="SELECT emp_name FROM EmployeeEntity WHERE emp_code=:iocl")
    public String getemployeename(@Param("iocl") int iocl);
}
