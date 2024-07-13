package com.indianoil.VisitorManagement.controller;

import com.indianoil.VisitorManagement.entity.EmployeeEntity;
import com.indianoil.VisitorManagement.entity.SettingsEntity;
import com.indianoil.VisitorManagement.interfaces.GetEmployeeByLocCode;
import com.indianoil.VisitorManagement.login.EmployeeLoginModal;
import com.indianoil.VisitorManagement.login.JwtUtils;
import com.indianoil.VisitorManagement.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController

@RequestMapping("/api/v1/employee")
public class EmployeeController{
@Autowired
JwtUtils jwtUtils;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private EmployeeService employeeService;
    @GetMapping("/location/{loc_code}")
    public List<GetEmployeeByLocCode> employeebycode(@PathVariable("loc_code") String loc){
        return employeeService.getemployeebyloc(loc);
    }
    @GetMapping
 public Optional<EmployeeEntity> getbyid(HttpServletRequest request)

    {
        String jwt = jwtUtils.getJwtFromCookies(request);
        int id= Integer.parseInt(jwtUtils.getUserNameFromJwtToken(jwt));
        return employeeService.getEmployee(id);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser( @RequestBody EmployeeLoginModal loginRequest) throws Exception {
        System.out.println(loginRequest.getId());
return employeeService.login(loginRequest);

    }
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        System.out.println("run logout");
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been signed out!");
    }
    @GetMapping("/settings")
    public Optional<SettingsEntity> GetSettings(HttpServletRequest request)
    {
        String jwt = jwtUtils.getJwtFromCookies(request);
        int id= Integer.parseInt(jwtUtils.getUserNameFromJwtToken(jwt));
        return employeeService.getsettings(id);


    }


    @PostMapping("/settings")
    public ResponseEntity<?> updatedsettings(HttpServletRequest request, @RequestBody SettingsEntity settingsEntity)
    {
        String jwt = jwtUtils.getJwtFromCookies(request);
        int id= Integer.parseInt(jwtUtils.getUserNameFromJwtToken(jwt));
        if(employeeService.UpdateSettings(id,settingsEntity)!=null)
            return ResponseEntity.ok("exists");
            else
                return  ResponseEntity.notFound().build();


    }
}

