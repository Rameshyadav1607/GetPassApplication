package com.indianoil.VisitorManagement.repository;

import com.indianoil.VisitorManagement.entity.VisitorEntity;
import com.indianoil.VisitorManagement.interfaces.GetAllVisitors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository

public interface VisitorRepository extends JpaRepository<VisitorEntity,String>{
    Optional<VisitorEntity> findById(String id);
    List<GetAllVisitors> findAllProjectedBy();
    @Query(value="SELECT id,name,date,time,type FROM suraksha.visitor WHERE reporting_officer=:iocl and status =:status ORDER BY id DESC",nativeQuery = true )
    public List<GetAllVisitors> FindVisitorRequests(@Param("iocl") String iocl, @Param("status")String status);
    @Query(value="SELECT id,name,date,time,type FROM suraksha.visitor WHERE loc_code=:loc and status =:status ORDER BY id DESC",nativeQuery = true)
    public List<GetAllVisitors> FindVisitorRequestsByLOC(@Param("loc") String loc, @Param("status") String status);
    @Query(value="SELECT id,name,type,date,time FROM suraksha.visitor WHERE loc_code=:loc and status=:status ORDER BY id DESC LIMIT 5",nativeQuery = true)
    public List<GetAllVisitors>  FindLatestVisitorRequestsByLOC(@Param("loc") String loc,@Param("status")String status);
    @Query(value="SELECT id,name,type,date,time FROM suraksha.visitor WHERE loc_code=:loc and gate=:gate ORDER BY id DESC",nativeQuery = true)
    public List<GetAllVisitors>  findvisitorsinout(@Param("loc") String loc,@Param("gate")String gate);
    @Query(value="SELECT id,name,type,date,time FROM suraksha.visitor WHERE date=:date and loc_code=:loc and gate=:gate ORDER BY id DESC LIMIT 10",nativeQuery = true)
    public List<GetAllVisitors>  findvisitorsinouttoday(@Param("loc") String loc,@Param("gate")String gate,@Param("date") LocalDate date);
    @Query(value="select count(id) from suraksha.visitor where type=:type and loc_code=:loc",nativeQuery = true)
    public int CountNumberOfVisitors(@Param("loc") String loc,@Param("type")String type);
    @Query(value="select count(id) from suraksha.visitor where type=:type and loc_code=:loc and reporting_officer=:empid",nativeQuery = true)
    public int CountNumberOfVisitors(@Param("loc") String loc,@Param("type")String type ,@Param("empid")String empid);

    @Query(value="select count(id) FROM suraksha.visitor WHERE EXTRACT(MONTH FROM date)=:month and EXTRACT(YEAR FROM date)=:year and loc_code=:loc ",nativeQuery = true)
    public int CountvisitorByMonth(@Param("loc") String loc,@Param("month") int month,@Param("year")int year);
    @Query(value="select count(id) FROM suraksha.visitor WHERE EXTRACT(MONTH FROM date)=:month and EXTRACT(YEAR FROM date)=:year and loc_code=:loc and reporting_officer=:empid ",nativeQuery = true)
    public int CountvisitorByMonth(@Param("loc") String loc,@Param("month") int month,@Param("year")int year,@Param("empid") String empId);
    GetAllVisitors findByNumber(String num);

    Optional<Object> findByEmail(String email);
    @Query(value="SELECT id,name,type,date,time FROM suraksha.visitor WHERE date=:date and loc_code=:loc and gate=:gate and reporting_officer=:empid ORDER BY id DESC LIMIT 10",nativeQuery = true)
    List<GetAllVisitors> findvisitorsinouttodaybyemployee(@Param("loc") String loc, @Param("gate") String gate,@Param("date") LocalDate now,@Param("empid") String empId);
    @Query(value="SELECT id,name,type,date,time FROM suraksha.visitor WHERE loc_code=:loc and gate=:gate and reporting_officer=:empid ORDER BY id DESC",nativeQuery = true)
    List<GetAllVisitors> findvisitorsinoutByEmployee(@Param("loc") String loc, @Param("gate") String gate, @Param("empid") String empId);
}
