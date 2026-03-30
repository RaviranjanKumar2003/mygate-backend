package com.example.demo.Services.Implementation;

import com.example.demo.Entities.FileData;
import com.example.demo.Payloads.FileDataDto;
import com.example.demo.Repositories.FileDataRepository;
import com.example.demo.Services.FileDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileDataServiceImpl implements FileDataService {

    @Autowired
    private FileDataRepository repository;

    @Override
    public FileDataDto uploadFile(MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path path = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        FileData data = new FileData();

        data.setFileName(file.getOriginalFilename());
        data.setFileUrl("/uploads/" + fileName);
        data.setFileType(file.getContentType());
        data.setFileSize(file.getSize());
        data.setUploadedAt(LocalDateTime.now());

        FileData saved = repository.save(data);

        return new FileDataDto(
                saved.getId(),
                saved.getFileName(),
                saved.getFileUrl(),
                saved.getFileType(),
                saved.getFileSize(),
                saved.getUploadedAt()
        );
    }
}