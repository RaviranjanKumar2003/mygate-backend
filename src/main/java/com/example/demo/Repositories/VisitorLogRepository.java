package com.example.demo.Repositories;

import com.example.demo.Entities.VisitorLog;
import com.example.demo.Enums.VisitorLogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorLogRepository extends JpaRepository<VisitorLog, Integer> {

    // Find all logs by status
    List<VisitorLog> findByVisitorLogStatus(VisitorLogStatus status);

    // Find all logs for a specific visitor
    List<VisitorLog> findByVisitorId(Integer visitorId);

    // Find all logs for a specific flat
    List<VisitorLog> findByFlatId(Integer flatId);


}
