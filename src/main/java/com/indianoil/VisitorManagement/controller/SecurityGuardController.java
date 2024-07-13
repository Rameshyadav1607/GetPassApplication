package com.indianoil.VisitorManagement.controller;

import com.indianoil.VisitorManagement.entity.VisitorEntity;
import com.indianoil.VisitorManagement.exceptions.SecurityGuardNotFoundException;
import com.indianoil.VisitorManagement.exceptions.VisitorNotFoundException;
import com.indianoil.VisitorManagement.login.EmployeeLoginModal;
import com.indianoil.VisitorManagement.login.SecurityLoginModal;
import com.indianoil.VisitorManagement.model.StatusCodeModal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.indianoil.VisitorManagement.entity.SecurityGuardEntity;
import com.indianoil.VisitorManagement.interfaces.SecurityGuardGetAll;
import com.indianoil.VisitorManagement.model.SecurityGuardResponseData;
import com.indianoil.VisitorManagement.services.SecurityGuardService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

@RestController

@RequestMapping("api/v1/securityguard")
@Controller
public class SecurityGuardController {

    private final SecurityGuardService securityGuardService;

    public SecurityGuardController(SecurityGuardService securityGuardService) {
        this.securityGuardService = securityGuardService;
    }

    @GetMapping
    public ResponseEntity<List<SecurityGuardGetAll>> findAll(@RequestParam(required = false) String locationCode) {
        List<SecurityGuardGetAll> securityGuards = securityGuardService.findAll(locationCode);
        return ResponseEntity.ok().body(securityGuards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecurityGuardResponseData> find(@PathVariable("id") String id) {
        SecurityGuardResponseData securityGuard = securityGuardService.find(id);
        return ResponseEntity.ok().body(securityGuard);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> find(@RequestBody SecurityLoginModal employeeLoginModal) {

      return    securityGuardService.login(employeeLoginModal);
        ////otp validation

        ////


    }
    @PostMapping("/sendotp")
    public ResponseEntity<?> sendotp(@RequestBody String num) throws IOException, URISyntaxException, InterruptedException {
        System.out.println(num);
        return securityGuardService.sendotp(num);

    }
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
return securityGuardService.logout();
    }

//
//d
//    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<String> create(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam int mobileNumber, @RequestParam String locationCode, @RequestParam("file")MultipartFile file) throws IOException {
//        String createdId = securityGuardService.create(file, firstName, lastName, email, mobileNumber, locationCode);
//        return ResponseEntity.ok().body("Created Security Guard:"+createdId);
//    }
    @PostMapping
    public StatusCodeModal create(@RequestBody SecurityGuardEntity securityGuard) throws IOException {
        System.out.println(securityGuard);
        return securityGuardService.create(securityGuard);
    }

    @PostMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> update(
            @PathVariable("id") String id,
            @RequestParam("file")MultipartFile file, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String mobileNumber, @RequestParam String locationCode) throws Exception{
        String updatedId = securityGuardService.update(id, file, firstName, lastName, email, mobileNumber, locationCode);
        return ResponseEntity.ok().body("Upated Security Guard:"+updatedId);
    }

    @PostMapping("status/{id}")
    public ResponseEntity<SecurityGuardEntity> delete(@PathVariable("id") String id,@RequestBody String status) {
        System.out.println(status);
        if(status.equalsIgnoreCase("true"))
        securityGuardService.delete(id,true);
        else
            securityGuardService.delete(id,false);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(value = "fileId") String fileId) throws Exception {
        return securityGuardService.downloadurl(fileId);
    }
    @GetMapping("/number/{num}")
    public SecurityGuardResponseData FindByNumber(@PathVariable("num") String num){

        return securityGuardService.findbynumber(num);
    }
    @ExceptionHandler(value = SecurityGuardNotFoundException.class)
    public ResponseEntity<Object> exception(SecurityGuardNotFoundException securityGuardNotFoundException)
    {
        System.out.println("inside exception");
        StatusCodeModal statusCodeModal=new StatusCodeModal();
        statusCodeModal.setStatus("Security Guard with same phone number already exists.");
        statusCodeModal.setStatus_code(500);
        return  ResponseEntity.ok(statusCodeModal);
    }

}
