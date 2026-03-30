package com.example.demo.Controllers;

import com.example.demo.Payloads.FileResponse;
import com.example.demo.Services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    // ================= UPLOAD IMAGE =================
    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUpload(
            @RequestParam("image") MultipartFile image
    ) {
        String fileName;
        try {
            fileName = fileService.uploadImage(path, image);
        } catch (IOException e) {
            return new ResponseEntity<>(
                    new FileResponse(null, "Image upload failed"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return new ResponseEntity<>(
                new FileResponse(fileName, "Image uploaded successfully"),
                HttpStatus.OK
        );
    }

    // ================= SERVE IMAGE =================
    @GetMapping("/profiles/{imageName}")
    public void downloadImage(
            @PathVariable String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = null;

        try {
            resource = fileService.getResource(path, imageName);
        } catch (FileNotFoundException e) {
            //  try lowercase as fallback
            try {
                resource = fileService.getResource(path, imageName.toLowerCase());
            } catch (FileNotFoundException ex) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Image not found");
                return;
            }
        }

        String contentType = URLConnection.guessContentTypeFromName(imageName);
        response.setContentType(
                contentType != null ? contentType : "application/octet-stream"
        );

        StreamUtils.copy(resource, response.getOutputStream());
    }

}
