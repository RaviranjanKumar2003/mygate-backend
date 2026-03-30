package com.example.demo.Services;

import com.example.demo.Enums.VisitorStatus;
import com.example.demo.Enums.VisitorType;
import com.example.demo.Payloads.VisitorDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VisitorService {

// CREATE VISITOR
    VisitorDto createVisitor(VisitorDto visitorDto);


// GET VISITOR BY ID
    VisitorDto getVisitorById(Integer societyId, Integer visitorId);

// GET VISITORS BY STATUS
   List<VisitorDto> getVisitorsBySocietyAndStatus(Integer societyId, VisitorStatus status);

// GET VISITORS BY Type
    List<VisitorDto> getVisitorsBySocietyAndVisitorsType(Integer societyId, VisitorType visitorType);

// GET ALL VISITOR In A Society
    List<VisitorDto> getAllVisitors(Integer societyId);


    List<VisitorDto> getVisitorsForOwnerTenant(
            Integer societyId,
            Integer buildingId,
            Integer floorId,
            Integer flatId
    );


    List<VisitorDto> getVisitorsForOwnerTenantByStatus(
            Integer societyId,
            Integer buildingId,
            Integer floorId,
            Integer flatId,
            VisitorStatus status
    );


//  UPDATE VISITOR
    VisitorDto updateVisitor(Integer societyId,Integer visitorId,VisitorDto dto);

// UPDATE VISITORS STATUS
   VisitorDto updateVisitorStatus(Integer societyId, Integer visitorId, VisitorStatus status);


//  DELETE VISITOR
    void deactivateVisitor(Integer id, Integer societyId);


// SEARCH VISITOR WITH NAME,MOBILE NUMBER,ID
    List<VisitorDto> searchVisitors(Integer societyId, String keyword);

    VisitorDto updateVisitorLogo(Integer visitorId, MultipartFile image);

// COUNT VISITOR BY TODAY DATE SOCIETY WISE
    long getTodayVisitorCount(Long societyId);

// COUNT VISITOR BY TODAY DATE FLAT WISE
    long getTodayVisitorCountByFlat(Long societyId, Long flatId);

}
