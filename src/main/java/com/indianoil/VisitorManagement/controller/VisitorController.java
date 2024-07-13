package com.indianoil.VisitorManagement.controller;

import com.indianoil.VisitorManagement.entity.VisitorEntity;
import com.indianoil.VisitorManagement.exceptions.VisitorNotFoundException;
import com.indianoil.VisitorManagement.interfaces.GetAllVisitors;
import com.indianoil.VisitorManagement.model.StatusCodeModal;
import com.indianoil.VisitorManagement.model.VisitorResponseData;
import com.indianoil.VisitorManagement.services.VisitorService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController


@RequestMapping("/api/v1/visitor")
public class VisitorController {

    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    public List<GetAllVisitors> getAll() throws Exception {

        return visitorService.findAll();
    }


    //getvisitorbyid
    @GetMapping("{id}")
    public VisitorResponseData getVisitorById(@PathVariable(value = "id") String visitorId) throws Exception {
        VisitorResponseData visitorResponseData= visitorService.findbyid(visitorId);


    return visitorResponseData;
    }


    @PostMapping
    public StatusCodeModal uploadData(@RequestBody VisitorEntity visitor) throws IOException, URISyntaxException, InterruptedException {
        return visitorService.create(visitor);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(value = "fileId") String fileId) throws Exception {
        return visitorService.downloadurl(fileId);
    }

    @PostMapping("{id}")
    public ResponseEntity<StatusCodeModal> update(@RequestBody VisitorEntity visitor) {
        Optional<VisitorEntity> updated = visitorService.update( visitor);
        StatusCodeModal statusCodeModal=new StatusCodeModal();
        if(updated!=null) {

            statusCodeModal.setStatus_code(200);
            statusCodeModal.setStatus("Visitor "+ updated.get().getId()+ " is updated");
        }
        else
        {
            statusCodeModal.setStatus_code(400);
            statusCodeModal.setStatus("Visitor "+updated.get().getId()+" was not updated");
    }
        return ResponseEntity.ok().body(statusCodeModal);
    }

    //getpendingvisitor requests
    @GetMapping("/visitorstatusbyIOCL/{status}:{iocl}")
    public List<GetAllVisitors> GetVisitorStatusByIOCL(@PathVariable(value = "iocl") String iocl, @PathVariable(value = "status") String status) throws Exception {
        return visitorService.findvisitorbyiocl(iocl, status);
    }


    @PostMapping("/status/{id}")
    public ResponseEntity ChangeStatus(@PathVariable(value = "id") String id, @RequestBody String status) {
        return visitorService.changestatus(id, status);
    }

    @GetMapping("/visitorstatusbyloc/{status}:{loc}")
    public List<GetAllVisitors> GetVisitorStatusByLoc(@PathVariable(value = "loc") String loc, @PathVariable(value = "status") String status) {
        return visitorService.getvisitorstatusbyloc(loc, status);
    }

    @GetMapping("/latestvisitorstatusbyloc/{status}:{loc}")
    public List<GetAllVisitors> GetLatestVisitorStatusByLoc(@PathVariable(value = "loc") String loc, @PathVariable(value = "status") String status) {
        return visitorService.getlatestvisitorstatusbyloc(loc, status);
    }

    @PostMapping("/passupdate")
    public String UpdatePassValidity(@RequestBody VisitorEntity visitor) {
        return visitorService.updatepassvalidity(visitor);
    }
//    @GetMapping("getvisitorcount?loc={loc}")
//    public List<GetVisitortypeCount> CountofVisitor(@PathVariable(value = "loc") String loc){
//        return  visitorService.getcount(loc);
//
//    }

@GetMapping("/searchphone/{num}")
    public ResponseEntity getbyphonenumber(@PathVariable(value = "num") String num){
       GetAllVisitors getAllVisitors= visitorService.getbyphonenumber(num);
       if(getAllVisitors!=null)
           return ResponseEntity.ok(getAllVisitors);
           else{
               return  ResponseEntity.notFound().build();
       }
}

    @GetMapping("/{gate}/{loc}")
    public List<GetAllVisitors> getvisitorinout(@PathVariable(value = "loc") String loc, @PathVariable(value = "gate") String gate) {

        System.out.println(loc);
        System.out.println(gate);
        return visitorService.GetVisitorInOut(loc, gate);
    }
    @GetMapping("/{gate}/{loc}/{id}")
    public List<GetAllVisitors> getvisitorinoutbyemployee(@PathVariable(value = "loc") String loc, @PathVariable(value = "gate") String gate,@PathVariable(value = "id") String emp_id) {

        System.out.println(loc);
        System.out.println(gate);
        return visitorService.GetVisitorInOutByEmployee(loc, gate,emp_id);
    }
    @GetMapping("piestats/{loc}")
    public int[] getpiedata(@PathVariable(value = "loc") String loc) {
        return visitorService.GetPieStats(loc);
    }
    @GetMapping("piestats/{loc}/{id}")
    public int[] getpiedatabyid(@PathVariable(value = "loc") String loc,@PathVariable(value = "id") String emp_code) {
        return visitorService.GetPieStatsByEmp(loc,emp_code);
    }
    @GetMapping("barstats/{loc}")
    public int[] getbardata(@PathVariable(value = "loc") String loc) {
        return visitorService.GetBarStats(loc);
    }
    @GetMapping("barstats/{loc}/{id}")
    public int[] getbardatabyid(@PathVariable(value = "loc") String loc,@PathVariable(value = "id") String emp_code) {
        return visitorService.GetBarStatsById(loc,emp_code);
    }
    @GetMapping("today/{gate}/{loc}")
    public List<GetAllVisitors> getvisitorinouttoday(@PathVariable(value = "loc") String loc, @PathVariable(value = "gate") String gate) {
        return visitorService.GetVisitorInOutToday(loc, gate);
    }
    @GetMapping("today/{gate}/{loc}/{id}")
    public List<GetAllVisitors> getvisitorinouttodaybyemployee(@PathVariable(value = "loc") String loc, @PathVariable(value = "gate") String gate ,@PathVariable(value = "id") String emp_id) {
        return visitorService.GetVisitorInOutTodayByEmployee(loc, gate,emp_id);
    }
    @ExceptionHandler(value = VisitorNotFoundException.class)
    public ResponseEntity<Object> exception(VisitorNotFoundException visitorNotFoundException)
    {
        System.out.println("inside exception");
        StatusCodeModal statusCodeModal=new StatusCodeModal();
        statusCodeModal.setStatus("Visitor not found.");
        statusCodeModal.setStatus_code(500);
        return  ResponseEntity.ok(statusCodeModal);
    }
}
