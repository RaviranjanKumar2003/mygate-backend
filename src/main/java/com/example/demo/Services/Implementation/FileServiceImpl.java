package com.example.demo.Services.Implementation;

import com.example.demo.Services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {


    // Upload Image
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        // File name
        String originalName = file.getOriginalFilename();

        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalName.substring(originalName.lastIndexOf(".")));

        // ✅ CORRECT PATH BUILDING
        Path uploadPath = Paths.get(path);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("UPLOAD PATH = " + filePath.toAbsolutePath());

        return fileName;
    }


    // Get Image
    @Override
    public InputStream getResource(String path, String fileName)
            throws IOException {

        Path fullPath = Paths.get(path).resolve(fileName);

        System.out.println("FULL PATH = " + fullPath.toAbsolutePath());

        if (!Files.exists(fullPath)) {
            throw new FileNotFoundException("File not found: " + fullPath);
        }

        return Files.newInputStream(fullPath);
    }



}
