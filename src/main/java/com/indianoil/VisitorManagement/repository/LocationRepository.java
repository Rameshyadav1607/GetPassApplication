package com.indianoil.VisitorManagement.repository;


import com.indianoil.VisitorManagement.entity.EmployeeEntity;
import com.indianoil.VisitorManagement.interfaces.Divisions;
import com.indianoil.VisitorManagement.interfaces.Locations;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<EmployeeEntity, Integer> {
    @Query(value="SELECT DISTINCT curr_comp_code,curr_comp FROM cem_genpsqldev.cem",nativeQuery = true)
    public List<Divisions> findAllDivisions();

    @Query(value="SELECT DISTINCT loc_code, loc_name FROM cem_genpsqldev.cem WHERE curr_comp_code=:divCode and loc_name is not NULL",nativeQuery = true)
    public List<Locations> findAllLocations(@Param("divCode") String divCode);
}
