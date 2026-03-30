package com.example.demo.Services;

import com.example.demo.Payloads.FileDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileDataService {

    FileDataDto uploadFile(MultipartFile file) throws IOException;

}