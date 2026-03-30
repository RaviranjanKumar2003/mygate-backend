package com.example.demo.Controllers;

import com.example.demo.Payloads.VisitorLogDto;
import com.example.demo.Services.VisitorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/visitorLogs")
public class VisitorLogController {

    @Autowired
    private VisitorLogService visitorLogService;


// CREATE
    @PostMapping
    @PreAuthorize("hasRole('SECURITY')")
    public ResponseEntity<VisitorLogDto> createLog(@Valid @RequestBody VisitorLogDto dto){
        return new ResponseEntity<>(visitorLogService.createVisitorLog(dto), HttpStatus.CREATED);
    }


// Get All Log By id
    @GetMapping("/{logId}")
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY')")
    public ResponseEntity<VisitorLogDto> getLog(@PathVariable Integer logId){
        return ResponseEntity.ok(visitorLogService.getVisitorLogById(logId));
    }


// Get All Logs
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY')")
    public ResponseEntity<List<VisitorLogDto>> getAllLogs(){
        return ResponseEntity.ok(visitorLogService.getAllVisitorLogs());
    }


// update logs
    @PutMapping("/{logId}")
    @PreAuthorize("hasRole('SECURITY')")
    public ResponseEntity<VisitorLogDto> updateLog(@PathVariable Integer logId,
                                                   @RequestBody VisitorLogDto dto){
        return ResponseEntity.ok(visitorLogService.updateVisitorLog(logId,dto));
    }


// delete logs
    @DeleteMapping("/{logId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteLog(@PathVariable Integer logId){
        visitorLogService.deleteVisitorLog(logId);
        return ResponseEntity.ok("Visitor log deleted successfully");
    }
}
