package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Flat;
import com.example.demo.Entities.Visitor;
import com.example.demo.Entities.VisitorLog;
import com.example.demo.Enums.VisitorLogStatus;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.VisitorLogDto;
import com.example.demo.Repositories.FlatRepository;
import com.example.demo.Repositories.VisitorLogRepository;
import com.example.demo.Repositories.VisitorRepository;
import com.example.demo.Services.VisitorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitorLogServiceImpl implements VisitorLogService {

    @Autowired
    private VisitorLogRepository visitorLogRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    private VisitorLogStatus visitorLogStatus;

    @Autowired
    private FlatRepository flatRepo;

    @Autowired
    private ModelMapper mapper;


// CREATE VISITOR LOG
    @Override
    public VisitorLogDto createVisitorLog(VisitorLogDto dto) {

        Visitor visitor = visitorRepository.findById(dto.getVisitorId())
                .orElseThrow(() -> new ResourceNotFoundException("Visitor","id",dto.getVisitorId()));

        Flat flat = flatRepo.findById(dto.getFlatId())
                .orElseThrow(() -> new ResourceNotFoundException("Flat","id",dto.getFlatId()));

        VisitorLog log = new VisitorLog();
        log.setVisitor(visitor);
        log.setFlat(flat);
        log.setInTime(dto.getInTime());
        log.setOutTime(null);
        log.setVisitorLogStatus(dto.getVisitorLogStatus() != null ? dto.getVisitorLogStatus() : VisitorLogStatus.PENDING);

        VisitorLog saved = visitorLogRepository.save(log);

        return mapper.map(saved, VisitorLogDto.class);
    }



// GET VISITOR LOG BY ID
    @Override
    public VisitorLogDto getVisitorLogById(Integer logId) {
        VisitorLog log = visitorLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("VisitorLog","id",logId));
        return mapper.map(log, VisitorLogDto.class);
    }


// GET ALL VISITOR LOGS
    @Override
    public List<VisitorLogDto> getAllVisitorLogs() {
        return visitorLogRepository.findAll()
                .stream()
                .map(log -> mapper.map(log, VisitorLogDto.class))
                .toList();
    }


// UPDATE VISITOR LOG
@Override
@Transactional
public VisitorLogDto updateVisitorLog(Integer logId, VisitorLogDto dto) {

    VisitorLog log = visitorLogRepository.findById(logId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("VisitorLog","id",logId));

    //  already exited
    if (log.getOutTime() != null) {
        throw new IllegalStateException("Visitor already checked out");
    }

    //  optional updates
    if (dto.getVisitorLogStatus() != null) {
        log.setVisitorLogStatus(dto.getVisitorLogStatus());
    }

    //  EXIT TIME → backend decides
    log.setOutTime(LocalDateTime.now());

    VisitorLog updated = visitorLogRepository.save(log);
    return mapper.map(updated, VisitorLogDto.class);
}



    // DELETE VISITOR LOG
    @Override
    public void deleteVisitorLog(Integer logId) {
        VisitorLog log = visitorLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("VisitorLog","id",logId));
        visitorLogRepository.delete(log);
    }
}
