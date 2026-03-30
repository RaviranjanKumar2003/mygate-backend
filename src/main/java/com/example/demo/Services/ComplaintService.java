package com.example.demo.Services;

import com.example.demo.Enums.ComplaintStatus;
import com.example.demo.Payloads.ComplaintDto;

import java.util.List;

public interface ComplaintService {


// CREATE COMPLAINT
    ComplaintDto createComplaint(ComplaintDto dto);

// GET COMPLAINT
    List<ComplaintDto> getComplaints(Integer societyId,Integer userId, String role);


// Delete complaint method
    boolean deleteComplaint(Integer complaintId, Integer userId, String role);


// UPDATE COMPLAINT STATUS
     ComplaintDto updateComplaintStatus(
           Integer complaintId,
           ComplaintStatus status,
           String role
    );

// UPDATE COMPLAINT
    ComplaintDto updateComplaint(
            Integer complaintId,
            ComplaintDto dto,
            Integer userId,
            String role
    );



}
