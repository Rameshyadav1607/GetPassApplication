package com.indianoil.VisitorManagement.repository;

import com.indianoil.VisitorManagement.entity.PassScanEntity;
import com.indianoil.VisitorManagement.interfaces.GetPassIn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface PassScanRepository extends JpaRepository<PassScanEntity,String> {
    @Query(value="SELECT id, inTime, outTime, visitor FROM PassScanEntity WHERE gate='in' and date=:date and visitor=(SELECT id FROM VisitorEntity WHERE status='approved' and loc_code=:loc )")
    public List<GetPassIn> findIn(@Param("date") LocalDate date, @Param("loc") String loc);
}
