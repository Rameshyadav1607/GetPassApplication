package com.indianoil.VisitorManagement.controller;

import com.indianoil.VisitorManagement.exceptions.VisitorNotFoundException;
import com.indianoil.VisitorManagement.model.StatusCodeModal;
import com.indianoil.VisitorManagement.services.PassScanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController


@RequestMapping("/api/v1/passscan")
public class PassScanController {
    private PassScanService passScanService;

    public PassScanController(PassScanService passScanService) {
        this.passScanService = passScanService;
    }

    @PostMapping
    public ResponseEntity<StatusCodeModal> passcheck(@RequestBody String visitor)
    {
        StatusCodeModal statusCodeModal= passScanService.save(visitor);
      return  ResponseEntity.ok(statusCodeModal);

    }
    @PostMapping("/scanexit")
    public ResponseEntity<StatusCodeModal> scan_at_exit(@RequestBody String visitor)
        {
            System.out.println("inside contoller scanexit");
        return ResponseEntity.ok(passScanService.scanexit(visitor));
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
