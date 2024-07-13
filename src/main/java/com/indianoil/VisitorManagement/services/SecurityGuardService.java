package com.indianoil.VisitorManagement.services;

import com.indianoil.VisitorManagement.entity.OtpEntity;
import com.indianoil.VisitorManagement.exceptions.SecurityGuardNotFoundException;
import com.indianoil.VisitorManagement.login.JwtUtils;
import com.indianoil.VisitorManagement.login.SecurityLoginModal;
import com.indianoil.VisitorManagement.model.StatusCodeModal;
import com.indianoil.VisitorManagement.repository.OtpRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.indianoil.VisitorManagement.entity.SecurityGuardEntity;
import com.indianoil.VisitorManagement.interfaces.SecurityGuardGetAll;
import com.indianoil.VisitorManagement.model.SecurityGuardResponseData;
import com.indianoil.VisitorManagement.repository.SecurityGuardRepository;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class SecurityGuardService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private final SecurityGuardRepository securityGuardRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RestTemplate restTemplate;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    @Autowired
    private final OtpRepository otpRepository;

    public SecurityGuardService(SecurityGuardRepository securityGuardRepository, OtpRepository otpRepository) {
        this.securityGuardRepository = securityGuardRepository;
        this.otpRepository = otpRepository;
    }

    public List<SecurityGuardGetAll> findAll(String id) {

        if(id==null)
            return securityGuardRepository.findAllProjectedBy();
        else
            return securityGuardRepository.findByLocationCode(id);
    }

    public SecurityGuardResponseData find(String id) {
        Optional<SecurityGuardEntity> securityGuard = securityGuardRepository.findById(id);
        SecurityGuardResponseData securityGuardResponseData = modelMapper.map(securityGuard, SecurityGuardResponseData.class);

        String downloadURL= ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/securityguard/download/")
                .path(securityGuard.get().getId())
                .toUriString();
        securityGuardResponseData.  setDownloadURL(downloadURL);
        return securityGuardResponseData;
    }

    public StatusCodeModal create(SecurityGuardEntity securityGuardEntity) throws IOException {
        byte[] image = Base64.getDecoder().decode(new String(securityGuardEntity.getImage_base64().substring(securityGuardEntity.getImage_base64().indexOf(",") + 1)).getBytes(StandardCharsets.UTF_8));
        securityGuardEntity.setImage(image);
        securityGuardEntity.setName(securityGuardEntity.getFirstName()+" "+securityGuardEntity.getLastName());
        securityGuardEntity.setImage_base64(null);
        securityGuardEntity.setStatus(true);
        SecurityGuardEntity securityGuard=null;
        StatusCodeModal statusCodeModal = new StatusCodeModal();
        try {
            securityGuard = securityGuardRepository.save(securityGuardEntity);
        } catch (RuntimeException e){
            statusCodeModal.setStatus_code(500);
            statusCodeModal.setStatus("Security Guard with phone number "+securityGuardEntity.getMobileNumber() + " already exists");
            return statusCodeModal;

        }

        if (securityGuard.getId() != null) {
            statusCodeModal.setStatus_code(200);
            statusCodeModal.setStatus("Security Guard added to database with id: " + securityGuard.getId());
        } else {
            statusCodeModal.setStatus_code(400);
            statusCodeModal.setStatus("Visitor was not added to database. Try again");
        }
        return statusCodeModal;
//        SecurityGuardEntity securityGuard = new SecurityGuardEntity();
//        securityGuard.setFirstName(firstName);
//        securityGuard.setLastName(lastName);
//        securityGuard.setEmail(email);
//        securityGuard.setMobileNumber(mobileNumber);
//        securityGuard.setLocationCode(locationCode);
//        securityGuard.setStatus(true);
//        byte[] image=file.getBytes();
//        String fileType=file.getContentType();
//        securityGuard.setFiletype(fileType);
//        securityGuard.setImage(image);
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        securityGuard.setFilename(fileName);
//        securityGuard.setStatus(true);
//        return securityGuardRepository.save(securityGuard).getId();
    }

    public String update(String id, MultipartFile file, String firstName, String lastName, String email, String mobileNumber, String locationCode) throws IOException{
        SecurityGuardEntity updated = new SecurityGuardEntity();
        updated.setId(id);
        updated.setFirstName(firstName);
        updated.setLastName(lastName);
        updated.setEmail(email);
        updated.setMobileNumber(mobileNumber);
        updated.setLocationCode(locationCode);
        updated.setStatus(true);
        byte[] image = file.getBytes();
        String fileType=file.getContentType();
        updated.setFileType(fileType);
        updated.setImage(image);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        return securityGuardRepository.save(updated).getId();

    }

    public void delete(String id,boolean status) {
        System.out.println(status);
        Optional<Object> inactive = securityGuardRepository.findById(id).map(value -> {
            value.setStatus(status);
            securityGuardRepository.save(value);
            return null;
        });
    }

    //Download file

    public ResponseEntity<ByteArrayResource> downloadurl(String fileId){
        Optional<SecurityGuardEntity> attachment = securityGuardRepository.findById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.get().getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" )
                .body(new ByteArrayResource(attachment.get().getImage()));
    }

    public SecurityGuardResponseData findbynumber(String num) throws SecurityGuardNotFoundException {
        SecurityGuardEntity securityGuard=securityGuardRepository.findByMobileNumber(num) ;
        if(securityGuard!=null)
        {
        SecurityGuardResponseData securityGuardResponseData = modelMapper.map(securityGuard, SecurityGuardResponseData.class);
        String downloadURL="";
        downloadURL= ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/securityguard/download/")
                .path(securityGuard.getId())
                .toUriString();
        securityGuardResponseData.setDownloadURL(downloadURL);
            return  securityGuardResponseData;
        }
        else {
            return new SecurityGuardResponseData();
        }





    }


    public ResponseEntity<?> login(SecurityLoginModal employeeLoginModal) throws SecurityGuardNotFoundException {
      SecurityGuardEntity securityGuard = securityGuardRepository.findByMobileNumber(employeeLoginModal.getId());

        if(securityGuard==null)
            return ResponseEntity.notFound().build();
        else {

            OtpEntity otpEntity=otpRepository.findlastbyid(securityGuard.getId());

            if(otpEntity.getExpiration_at().isAfter(LocalDateTime.now()) && otpEntity.getOtp()==Integer.parseInt(employeeLoginModal.getPassword()))
            {
                System.out.print("Running in if");
            SecurityGuardResponseData securityGuardResponseData = modelMapper.map(securityGuard, SecurityGuardResponseData.class);

            String downloadURL="";
            downloadURL= ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/securityguard/download/")
                    .path(securityGuard.getId())
                    .toUriString();
            securityGuardResponseData.setDownloadURL(downloadURL);
            ResponseCookie jwtCookie = jwtUtils.generateSecurityJwtCookie(securityGuard);
            System.out.println("jwt" + jwtCookie);
            String role = securityGuard.getRole();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(securityGuardResponseData);
        }
            else {
                System.out.print("Running in else");
                return ResponseEntity.notFound().build();
            }

    }}

    public ResponseEntity<?> logout() {

        System.out.println("run logout");
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been signed out!");
    }
    @Transactional
    public SecurityGuardEntity loadbyId(String username) throws UsernameNotFoundException {
        SecurityGuardEntity securityGuard = securityGuardRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + username));

        return securityGuard;
    }

    public ResponseEntity<?> sendotp(String num) throws IOException, URISyntaxException, InterruptedException {
        SecurityGuardResponseData securityGuardResponseData=findbynumber(num);

        if(securityGuardResponseData.getId()!=null) {
            SendOtp(securityGuardResponseData);
            return ResponseEntity.ok().body("sent otp");
        }
else {
    return ResponseEntity.notFound().build();
        }

    }
    public void SendOtp(SecurityGuardResponseData securityGuardEntity) throws IOException, URISyntaxException, InterruptedException {
        Random rand = new Random();
        OtpEntity otpEntity=new OtpEntity();
        otpEntity.setEmp_code(securityGuardEntity.getId());
        otpEntity.setOtp(ThreadLocalRandom.current().nextInt(100000,1000000));
        otpEntity.setCreated_at(LocalDateTime.now());
        otpEntity.setExpiration_at(otpEntity.getCreated_at().plusMinutes(2));
        otpRepository.save(otpEntity);
        String message ="OTP for Gatepass Application login is "+otpEntity.getOtp()+", which expires in 120 seconds -IndianOil.";
        String url = "https://sandesh.indianoil.co.in/sandesh/smsPrioOutMessageRequest/priooutgoingSMS";
        URL urlObj = new URL(url.trim());
        System.out.println(urlObj.toURI());

        JSONArray ja = new JSONArray();
        JSONObject obj=new JSONObject();
        obj.put("mobile_no",String.valueOf(securityGuardEntity.getMobileNumber()));
        obj.put("sms_content",message.toString());
        obj.put("gst_flag","1");
        obj.put("template_id","1107167213257427106");
        obj.put("ref_in_msg_unique_id","1107167213257427106");
        ja.put(obj);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(ja.toString()))
                .uri(urlObj.toURI())

                .setHeader("authenticationToken","1085779bb2f0935b728713bc33e7d0ab")
                .setHeader("Content-Type","application/json")
                .setHeader("appId","GatePass")
                .setHeader("appCategory","GatePass_OTP")
                .setHeader("msgType","ENG")
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(otpEntity.getOtp());

    }

}

