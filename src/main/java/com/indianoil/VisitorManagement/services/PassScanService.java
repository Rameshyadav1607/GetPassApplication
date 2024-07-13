package com.indianoil.VisitorManagement.services;

import com.indianoil.VisitorManagement.entity.PassScanEntity;
import com.indianoil.VisitorManagement.entity.VisitorEntity;
import com.indianoil.VisitorManagement.exceptions.VisitorNotFoundException;
import com.indianoil.VisitorManagement.model.StatusCodeModal;

import com.indianoil.VisitorManagement.repository.PassScanRepository;
import com.indianoil.VisitorManagement.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PassScanService {
    private final VisitorRepository visitorRepository;
    @Autowired
    private final PassScanRepository passScanRepository;

    public PassScanService(VisitorRepository visitorRepository, PassScanRepository passScanRepository) {
        this.visitorRepository = visitorRepository;
        this.passScanRepository = passScanRepository;
    }
    public Boolean checkStatus(String status)
    {
        return status.equals("approved");
    }
    public boolean passvaliditycheck(LocalDate scandate,LocalDate date){
    if(scandate.isBefore(date) || scandate.equals(date))
        return true;
    else
        return false;
    }


    public void updateTransactionID(Optional<VisitorEntity> visitor, String gate, String id)
    {
        visitor.map(visitorentity -> {
        visitorentity.setGate(gate);
        visitorentity.setLatestTransactionalID(id);
        return visitorRepository.save(visitorentity);
    });

    }
public  boolean visitorType(String type)
{
    if(type.equalsIgnoreCase("foreign")|| type.equalsIgnoreCase("normal"))
    return true;
    else
        return false;
}
    public StatusCodeModal save(String visitor) throws VisitorNotFoundException {
        VisitorEntity visitorEntity=visitorRepository.findById(visitor).orElseThrow(VisitorNotFoundException::new);
        StatusCodeModal statusCodeModal=new StatusCodeModal();
        if(visitorEntity.getGate().equals("out") && visitorType(visitorEntity.getType()))
        {
            statusCodeModal.setStatus("Visitor Pass already used. Make a new pass request");
            statusCodeModal.setStatus_code(400);
            return statusCodeModal;
        }


        PassScanEntity passScanEntity=new PassScanEntity();
        passScanEntity.setGate("in");
        passScanEntity.setVisitor(visitor);
        String pattern = "hh:mm a";
        LocalTime time=LocalTime.now();
        String inTime= time.format(DateTimeFormatter.ofPattern(pattern));
        passScanEntity.setInTime(inTime);

passScanEntity.setDate(LocalDate.now());


        if(visitorEntity.getGate().equals("in")) {
            statusCodeModal.setStatus_code(400);
            statusCodeModal.setStatus("Visitor already in");
            return statusCodeModal;
        }
        else {


            if (checkStatus(visitorEntity.getStatus())) {
                if (passvaliditycheck(passScanEntity.getDate(), visitorEntity.getPass_validity())) {
                    statusCodeModal.setStatus_code(200);
                    statusCodeModal.setStatus("Allowed at In Gate at " + inTime+ " " + passScanEntity.getDate());
                    passScanEntity.setActivity("allowed");
                    passScanEntity.setRemarks("Allowed at In Gate at " + inTime+ " " + passScanEntity.getDate());
                } else {
                    statusCodeModal.setStatus_code(400);
                    statusCodeModal.setStatus("Not Allowed at In Gate at " + inTime +" " + passScanEntity.getDate() +" due to expiration of validity");
                    passScanEntity.setActivity("Pass validity expired");
                    passScanEntity.setRemarks("Not Allowed at In Gate at " + inTime +" " + passScanEntity.getDate() );
                }
            }
            else {statusCodeModal.setStatus_code(400);
                statusCodeModal.setStatus("Visitor request is "+visitorEntity.getStatus());
                return statusCodeModal;
            }
        }
        PassScanEntity result= passScanRepository.save(passScanEntity) ;
        if(statusCodeModal.getStatus_code()==200)
        updateTransactionID(Optional.of(visitorEntity),"in",result.getId());
        return statusCodeModal;


    }

    public StatusCodeModal scanexit(String visitor) throws VisitorNotFoundException {


        StatusCodeModal statusCodeModal=new StatusCodeModal();
    VisitorEntity visitorEntity= visitorRepository.findById(visitor).orElseThrow(VisitorNotFoundException::new);

        if (visitorEntity.getStatus().equals("approved"))
        {System.out.println(visitorEntity.getGate());
        if(visitorEntity.getGate().equals("out"))
        {
            System.out.println("inside if");
            statusCodeModal.setStatus_code(400);
            statusCodeModal.setStatus("Visitor already out. Make a new entry.");
            return (statusCodeModal);
        }
        else if(visitorEntity.getGate().equals("-"))
        {
            statusCodeModal.setStatus_code(400);
            statusCodeModal.setStatus("Visitor not scanned at entry gate.");
            return statusCodeModal;
        }
        else if(visitorEntity.getGate().equals("in")){
            System.out.println("inside else");

            Optional<PassScanEntity> passScanEntity= passScanRepository.findById(visitorEntity.getLatestTransactionalID());
            passScanEntity.map(passScan -> {
                passScan.setGate("out");
                String pattern = "hh:mm a";
                LocalTime time=LocalTime.now();
                String outTime= time.format(DateTimeFormatter.ofPattern(pattern));
                passScan.setOutTime(outTime);
                System.out.println(passScan.getOutTime());
            if (visitorEntity.getStatus().equals("approved")) {
                passScan.setDate(LocalDate.now());
                if (passvaliditycheck( passScan.getDate() ,visitorEntity.getPass_validity())) {
                    statusCodeModal.setStatus_code(200);
                    statusCodeModal.setStatus("Visitor Allowed at Out gate at "+LocalDate.now() + " " + outTime);
                    passScan.setActivity("allowed");
                    passScan.setRemarks("Allowed at " + passScan.getGate() + " Gate");
                } else {
                    statusCodeModal.setStatus_code(400);
                    statusCodeModal.setStatus("Not allowed at " + passScan.getGate() + " Gate due to expired pass validity" );
                    passScan.setActivity("not allowed");
                    passScan.setRemarks("Not allowed at " + passScan.getGate() + " Gate");
                }
        }
            else {
                statusCodeModal.setStatus_code(400);
                statusCodeModal.setStatus("Visitor request is not approved" );
                passScan.setActivity("not allowed");
                passScan.setRemarks("Visitor is not approved");
            }
            if(statusCodeModal.getStatus_code()==200)
             updateTransactionID(Optional.of(visitorEntity),"out",passScan.getId());

                return statusCodeModal ;
    });
}

    }
        else {
            statusCodeModal.setStatus_code(400);
            statusCodeModal.setStatus("Visitor request is not approved" );
        }
        return statusCodeModal;

}}
