package com.indianoil.VisitorManagement.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indianoil.VisitorManagement.entity.VisitorEntity;
import com.indianoil.VisitorManagement.exceptions.VisitorNotFoundException;
import com.indianoil.VisitorManagement.interfaces.GetAllVisitors;
import com.indianoil.VisitorManagement.model.StatusCodeModal;
import com.indianoil.VisitorManagement.model.VisitorResponseData;
import com.indianoil.VisitorManagement.repository.EmployeeRepository;
import com.indianoil.VisitorManagement.repository.MaterialRepository;
import com.indianoil.VisitorManagement.repository.SecurityGuardRepository;
import com.indianoil.VisitorManagement.repository.VisitorRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;




@Service
public class VisitorService {
    int count=103;
    private final ObjectMapper objectMapper = new ObjectMapper();


    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private final VisitorRepository visitorRepository;
    private final MaterialRepository materialRepository;
    private final MaterialService materialService;
    private final SecurityGuardRepository securityGuardRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public int getCount(){
        return this.count++;
    }


    public void SendVisitorSMS(String id,String number, String status) throws IOException, URISyntaxException, InterruptedException {
        String message="";
            String url = "https://sandesh.indianoil.co.in/sandesh/smsOutMessageRequest/outgoingSMS";
            URL urlObj = new URL(url.trim());
            JSONArray ja = new JSONArray();
            JSONObject obj=new JSONObject();
            obj.put("mobile_no",number);

            obj.put("gst_flag","1");





        if(status.equalsIgnoreCase("approved")) {
//
            String personResultAsJsonStr = restTemplate.getForObject("https://api.shrtco.de/v2/shorten?url="+"https://uat.indianoil.co.in/gatepass/"+id,String.class);

            JsonNode root = objectMapper.readTree(personResultAsJsonStr);

            String res= String.valueOf(root.path("result").path("short_link"));
            System.out.println(res);
    message = "Your request for Visitor Pass approved by concerned IOC officer. Please click "+ res.substring(1,res.length()-1) + " to download the visitor pass and show to security - IndianOil";
            obj.put("sms_content",message);
            obj.put("template_id","1107166928222497862");
            obj.put("ref_in_msg_unique_id","1107166928222497862");
}
else if (status.equalsIgnoreCase("denied"))
{
     message = "Your request for Visitor Pass rejected by concerned IOC officer. Please contact concerned officer you intend to meet - IndianOil";
    obj.put("sms_content",message);
    obj.put("template_id","1107166928227681899");
    obj.put("ref_in_msg_unique_id","1107166928227681899");

    }
        System.out.println(message);
        ja.put(obj);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(ja.toString()))
                .uri(urlObj.toURI())

                .setHeader("authenticationToken","1085779bb2f0935b728713bc33e7d0ab")
                .setHeader("Content-Type","application/json")
                .setHeader("appId","GatePass")
                .setHeader("appCategory","GatePass_TRAN")
                .setHeader("msgType","ENG")
                .build();
        System.out.println(obj);

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
    }

    public void SendEmployeeSMS(String number,String tempid ) throws IOException, URISyntaxException, InterruptedException {
        String personResultAsJsonStr = restTemplate.getForObject("https://api.shrtco.de/v2/shorten?url="+"https://uat.indianoil.co.in/gatepass/dashboard/request",String.class);
        JsonNode root = objectMapper.readTree(personResultAsJsonStr);
        String res= String.valueOf(root.path("result").path("short_link"));
        System.out.println(res);
        String url = "https://sandesh.indianoil.co.in/sandesh/smsOutMessageRequest/outgoingSMS";
        URL urlObj = new URL(url.trim());
        JSONArray ja = new JSONArray();
        JSONObject obj=new JSONObject();
        obj.put("mobile_no",number);

        obj.put("gst_flag","1");
        obj.put("template_id",tempid);
        obj.put("ref_in_msg_unique_id",tempid);


//
      String  message = "There is a visitor request for your approval. Please click "+res.substring(1,res.length()-1)+" to approve/reject. - IndianOil";
        System.out.println(message);
        obj.put("sms_content",message);
        ja.put(obj);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(ja.toString()))
                .uri(urlObj.toURI())

                .setHeader("authenticationToken","1085779bb2f0935b728713bc33e7d0ab")
                .setHeader("Content-Type","application/json")
                .setHeader("appId","GatePass")
                .setHeader("appCategory","GatePass_TRAN")
                .setHeader("msgType","ENG")
                .build();
        System.out.println(request);

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);


    }
    public VisitorService(VisitorRepository visitorRepository, MaterialRepository materialRepository, MaterialService materialService, SecurityGuardRepository securityGuardRepository, EmployeeRepository employeeRepository ) {
        this.visitorRepository = visitorRepository;
        this.materialRepository = materialRepository;
        this.materialService = materialService;
        this.securityGuardRepository = securityGuardRepository;
        this.employeeRepository = employeeRepository;

    }

    public List<GetAllVisitors> findAll() {
        return visitorRepository.findAllProjectedBy();
    }

    public VisitorResponseData findbyid(String id)  throws VisitorNotFoundException  {
        VisitorEntity visitor = visitorRepository.findById(id).orElseThrow(VisitorNotFoundException::new);
        DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");


        VisitorResponseData visitorResponseData = modelMapper.map(visitor, VisitorResponseData.class);
        visitorResponseData.setDate(FOMATTER.format(visitor.getDate()));
        visitorResponseData.setPass_validity(FOMATTER.format(visitor.getPass_validity()));

        visitorResponseData.setReporting_officer(employeeRepository.getemployeename(Integer.parseInt(visitor.getReporting_officer())));
        visitorResponseData.setSecurity_guard(securityGuardRepository.getsecurityguardname((visitor.getSecurity_guard())));
        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/visitor/download/")
                .path(visitor.getId())
                .toUriString();
        visitorResponseData.setDownloadURL(downloadURL);
        return visitorResponseData;

    }

    public StatusCodeModal  create(VisitorEntity visitor) throws IOException, URISyntaxException, InterruptedException {

        byte[] image= Base64.getDecoder().decode(new String(visitor.getImage_base64().substring(visitor.getImage_base64().indexOf(",") + 1)).getBytes(StandardCharsets.UTF_8));
        visitor.setImage(image);
        visitor.setImage_base64(null);
        visitor.setGate("-");
        visitor.setStatus("pending");
        System.out.println(visitor.getStatus());
        VisitorEntity visitorEntity=visitorRepository.save(visitor);

        StatusCodeModal statusCodeModal=new StatusCodeModal();
        if(visitorEntity!=null) {
            statusCodeModal.setStatus_code(200);
            statusCodeModal.setStatus("Visitor added to database with id: " + visitorEntity.getId());
            System.out.println(employeeRepository.findById( Integer.parseInt(visitor.getReporting_officer())).get().getMobile_no());
            SendEmployeeSMS(employeeRepository.findById( Integer.parseInt(visitor.getReporting_officer())).get().getMobile_no(),"1107166928232185415");
        }
        else { statusCodeModal.setStatus_code(400);
            statusCodeModal.setStatus("Visitor was not added to database. Try again");
        }
        return statusCodeModal;
    }

    public Optional<VisitorEntity> update(VisitorEntity visitor) {
        System.out.println("updating");
        return visitorRepository.findById(visitor.getId())
                .map(visitorentity -> {
                    visitorentity.setPass_update_remarks(visitor.getPass_update_remarks());
                    visitorentity.setPass_validity(visitor.getPass_validity());
                    visitorentity.setStatus("pending");
                    return visitorRepository.save(visitorentity);
                });

    }

    public void delete(String id) {
        visitorRepository.deleteById(id);
    }

    public ResponseEntity downloadurl(String fileId) {
        Optional<VisitorEntity> attachment = visitorRepository
                .findById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.get().getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + "\"")
                .body(new ByteArrayResource(attachment.get().getImage()));
    }

    public List<GetAllVisitors> findvisitorbyiocl(String iocl, String status) {
        return visitorRepository.FindVisitorRequests(iocl, status);
    }

    public ResponseEntity changestatus(String id, String status) {
        visitorRepository.findById(id)
                .map(visitorentity -> {
                    visitorentity.setStatus(status);

                    try {
                        SendVisitorSMS(visitorentity.getId(),visitorentity.getNumber(),status);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return visitorRepository.save(visitorentity);
                });

        return ResponseEntity.ok(status);

    }

    public List<GetAllVisitors> getvisitorstatusbyloc(String loc, String status) {
        return visitorRepository.FindVisitorRequestsByLOC(loc, status);
    }

    public List<GetAllVisitors> getlatestvisitorstatusbyloc(String loc, String status) {

        return visitorRepository.FindLatestVisitorRequestsByLOC(loc, status);
    }

    public String updatepassvalidity(VisitorEntity visitor) {
         visitorRepository.findById(visitor.getId())
                .map(visitorentity -> {

                    visitorentity.setPass_update_remarks(visitor.getPass_update_remarks());
                    visitorentity.setPass_validity(visitor.getPass_validity());
                    visitorentity.setStatus("pending");
                    return visitorRepository.save(visitorentity);
                });
        return null;
    }

    public GetAllVisitors getbyphonenumber(String num) {
        return visitorRepository.findByNumber(num);
    }

    public List<GetAllVisitors> GetVisitorInOut(String loc, String gate) {
        return visitorRepository.findvisitorsinout(loc,gate);
    }
    public List<GetAllVisitors> GetVisitorInOutToday(String loc, String gate) {
        System.out.println(loc+gate);
        return visitorRepository.findvisitorsinouttoday(loc,gate,LocalDate.now());
    }
    public int[] GetPieStats(String loc)
    {

        int[] data=new int[9];
       data[0]=visitorRepository.CountNumberOfVisitors(loc,"Normal");
        data[1]=visitorRepository.CountNumberOfVisitors(loc,"IOCL");
        data[2]=visitorRepository.CountNumberOfVisitors(loc,"Foreign");
        data[3]=visitorRepository.CountNumberOfVisitors(loc,"Maintainance");
        data[4]=visitorRepository.CountNumberOfVisitors(loc,"Temporary");

        data[5]=visitorRepository.CountNumberOfVisitors(loc,"Material Delivery");
        data[6]=visitorRepository.CountNumberOfVisitors(loc,"Material Pickup");
        data[7]=visitorRepository.CountNumberOfVisitors(loc,"Contractor Material Delivery");
        data[8]=visitorRepository.CountNumberOfVisitors(loc,"Contractor Material Pickup");
return data;
    }

    public int[] GetBarStats(String loc) {

        LocalDate localDate=LocalDate.now();

        int[] data=new int[12];
        data[0]=visitorRepository.CountvisitorByMonth(loc,1,localDate.getYear());
        data[1]=visitorRepository.CountvisitorByMonth(loc,2,localDate.getYear());
        data[2]=visitorRepository.CountvisitorByMonth(loc,3,localDate.getYear());
        data[3]=visitorRepository.CountvisitorByMonth(loc,4,localDate.getYear());
        data[4]=visitorRepository.CountvisitorByMonth(loc,5,localDate.getYear());

        data[5]=visitorRepository.CountvisitorByMonth(loc,6,localDate.getYear());
        data[6]=visitorRepository.CountvisitorByMonth(loc,7,localDate.getYear());
        data[7]=visitorRepository.CountvisitorByMonth(loc,8,localDate.getYear());
        data[8]=visitorRepository.CountvisitorByMonth(loc,9,localDate.getYear());
        data[9]=visitorRepository.CountvisitorByMonth(loc,10,localDate.getYear());
        data[10]=visitorRepository.CountvisitorByMonth(loc,11,localDate.getYear());
        data[11]=visitorRepository.CountvisitorByMonth(loc,12,localDate.getYear());
        return data;
    }


    public List<GetAllVisitors> GetVisitorInOutTodayByEmployee(String loc, String gate, String empId) {
        return visitorRepository.findvisitorsinouttodaybyemployee(loc,gate,LocalDate.now(),empId);
    }

    public List<GetAllVisitors> GetVisitorInOutByEmployee(String loc, String gate, String empId) {
        return visitorRepository.findvisitorsinoutByEmployee(loc,gate,empId);
    }

    public int[] GetPieStatsByEmp(String loc, String empCode) {
        {

            int[] data=new int[9];
            data[0]=visitorRepository.CountNumberOfVisitors(loc,"Normal",empCode);
            data[1]=visitorRepository.CountNumberOfVisitors(loc,"IOCL",empCode);
            data[2]=visitorRepository.CountNumberOfVisitors(loc,"Foreign",empCode);
            data[3]=visitorRepository.CountNumberOfVisitors(loc,"Maintainance",empCode);
            data[4]=visitorRepository.CountNumberOfVisitors(loc,"Temporary",empCode);

            data[5]=visitorRepository.CountNumberOfVisitors(loc,"Material Delivery",empCode);
            data[6]=visitorRepository.CountNumberOfVisitors(loc,"Material Pickup",empCode);
            data[7]=visitorRepository.CountNumberOfVisitors(loc,"Contractor Material Delivery",empCode);
            data[8]=visitorRepository.CountNumberOfVisitors(loc,"Contractor Material Pickup",empCode);
            return data;
        }

    }

    public int[] GetBarStatsById(String loc, String empCode) {
        LocalDate localDate=LocalDate.now();

        int[] data=new int[12];
        data[0]=visitorRepository.CountvisitorByMonth(loc,1,localDate.getYear(),empCode);
        data[1]=visitorRepository.CountvisitorByMonth(loc,2,localDate.getYear(),empCode);
        data[2]=visitorRepository.CountvisitorByMonth(loc,3,localDate.getYear(),empCode);
        data[3]=visitorRepository.CountvisitorByMonth(loc,4,localDate.getYear(),empCode);
        data[4]=visitorRepository.CountvisitorByMonth(loc,5,localDate.getYear(),empCode);

        data[5]=visitorRepository.CountvisitorByMonth(loc,6,localDate.getYear(),empCode);
        data[6]=visitorRepository.CountvisitorByMonth(loc,7,localDate.getYear(),empCode);
        data[7]=visitorRepository.CountvisitorByMonth(loc,8,localDate.getYear(),empCode);
        data[8]=visitorRepository.CountvisitorByMonth(loc,9,localDate.getYear(),empCode);
        data[9]=visitorRepository.CountvisitorByMonth(loc,10,localDate.getYear(),empCode);
        data[10]=visitorRepository.CountvisitorByMonth(loc,11,localDate.getYear(),empCode);
        data[11]=visitorRepository.CountvisitorByMonth(loc,12,localDate.getYear(),empCode);
        return data;
    }
}


