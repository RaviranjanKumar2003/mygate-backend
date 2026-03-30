package com.example.demo.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public interface FileService {

    // UPLOADING IMAGE
    String uploadImage(String path, MultipartFile file) throws IOException;

    // FETCH IMAGE

    InputStream getResource(String path, String fileName) throws IOException;

    public default void deleteImage(String path, String imageName) throws IOException {
        if (imageName == null) return;

        Path imagePath = Paths.get(path, imageName);
        Files.deleteIfExists(imagePath);
    }



}
