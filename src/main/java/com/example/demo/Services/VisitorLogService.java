package com.example.demo.Services;

import com.example.demo.Payloads.VisitorLogDto;

import java.util.List;

public interface VisitorLogService {


// CREATE VISITOR LOG
    VisitorLogDto createVisitorLog(VisitorLogDto dto);


// GET VISITOR LOG BY ID
    VisitorLogDto getVisitorLogById(Integer id);


// GET ALL VISITOR LOG
    List<VisitorLogDto> getAllVisitorLogs();


// UPDATE VISITOR LOG
    VisitorLogDto updateVisitorLog(Integer id, VisitorLogDto dto);


// DELETE VISITOR LOG
    void deleteVisitorLog(Integer logId);


}
