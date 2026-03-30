package com.example.demo.Controllers;

import com.example.demo.Entities.User;
import com.example.demo.Enums.UserRole;
import com.example.demo.Enums.UserStatus;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.*;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.FileService;
import com.example.demo.Services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Value("${project.image}")
    private String path;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;



// CREATE
   @PostMapping
   public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto)
           throws Exception {

       UserDto response = userService.createUser(dto);
       return new ResponseEntity<>(response, HttpStatus.CREATED);
   }



// GET ALL USER IN SOCIETY
    @GetMapping("/society/{societyId}")
    public List<UserProfileDto> getAllUsersOfSociety(@PathVariable Integer societyId) {
        return userService.getAllUsersOfSociety(societyId);
    }



// GET USERS SUMMARY IN SOCIETY
    @GetMapping("/society/{societyId}/summary")
    public UserCountResponse getSocietyUserSummary(@PathVariable Integer societyId) {
        return userService.getSocietyUserSummary(societyId);
    }


// GET ALL USER IN SOCIETY BY ROLE
    @GetMapping("/society/{societyId}/role/{role}")
    public List<UserDto> getUsersOfSocietyByRole(@PathVariable Integer societyId,
                                                 @PathVariable UserRole role) {
        return userService.getUsersOfSocietyByRole(societyId, role);
    }


// GET ALL USERS IN A SOCIETY BY STATUS
    @GetMapping("/society/{societyId}/status/{status}")
    public List<UserDto> getUsersOfSocietyByStatus(@PathVariable Integer societyId,
                                                   @PathVariable UserStatus status) {
        return userService.getUsersOfSocietyByStatus(societyId, status);
    }

// GET USER BY ID
   @GetMapping("/society/{societyId}/user/{userId}")
   public ResponseEntity<UserDto> getUserById(
           @PathVariable Integer societyId,
           @PathVariable Integer userId
   ) {
       return ResponseEntity.ok(
               userService.getUserById(userId, societyId)
       );
   }




// GET ALL USER IN FLAT BY ID

    @GetMapping("/flat/{flatId}")
    public ResponseEntity<NormalUserProfileDto> getUserByFlat(
            @PathVariable Integer flatId) {

        return ResponseEntity.ok(
                userService.getUserByFlatId(flatId)
        );
    }




// UPDATE USER
    @PutMapping("/society/{societyId}/user/{userId}")
    public UserDto updateUser(@PathVariable Integer userId, @RequestBody UserDto dto) {
        return userService.updateUser(userId, dto);
    }



// DELETE
@DeleteMapping("/society/{societyId}/user/{userId}")
public ResponseEntity<ApiResponse> deleteUser(
        @PathVariable Integer userId,
        @PathVariable Integer societyId,
        @RequestParam(required = false) Integer buildingId) {

    ApiResponse response = userService.deleteUser(userId, societyId, buildingId);
    return ResponseEntity.ok(response);
}





// ==================== SEARCH ====================
    @GetMapping("/society/{societyId}/search/email")
    public List<UserDto> searchUsersByEmail(@PathVariable Integer societyId,
                                            @RequestParam String email) {
        return userService.searchUsersByEmailInSociety(societyId, email);
    }

    @GetMapping("/society/{societyId}/search/name")
    public List<UserDto> searchUsersByName(@PathVariable Integer societyId,
                                           @RequestParam String keyword) {
        return userService.searchUserByName(societyId, keyword);
    }








// USERS IMAGE UPLOAD

    // ================== mapToDto ==================
    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setUserStatus(user.getUserStatus());

        if (user.getSociety() != null) {
            dto.setSocietyName(user.getSociety().getName());
        }

        if (user.getBuilding() != null) {
            dto.setBuildingName(user.getBuilding().getName());
        }

        dto.setImageURL(user.getImageURL());
        return dto;
    }

    @PostMapping("society/{societyId}/image/upload/{userId}")
    public ResponseEntity<UserDto> uploadUserImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer societyId,
            @PathVariable Integer userId
    ) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId));

        if (user.getImageURL() != null) {
            fileService.deleteImage(path, user.getImageURL());
        }

        String fileName = fileService.uploadImage(path, image);
        user.setImageURL(fileName);

        userRepository.save(user);

        return ResponseEntity.ok(mapToDto(user));
    }


// GET USERS IMAGE
    @GetMapping("/image/get/user/{userId}")
    public void downloadUserImage(
            @PathVariable Integer userId,
            HttpServletResponse response
    ) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", userId)
                );

        // ✅ DB me full path hai → sirf file name nikalo
        String imagePath = user.getImageURL();
        String imageName = Paths.get(imagePath).getFileName().toString();

        InputStream resource = fileService.getResource(path, imageName);

        String contentType = URLConnection.guessContentTypeFromName(imageName);
        response.setContentType(
                contentType != null ? contentType : "application/octet-stream"
        );

        StreamUtils.copy(resource, response.getOutputStream());
    }


// GET QR & ENTRY CODE
    @GetMapping("/society/{societyId}/qr/{userId}")
    public ResponseEntity<Resource> getUserQr(@PathVariable Integer userId) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Path path = Paths.get(user.getQrCodePath());

        if (!Files.exists(path)) {
            throw new RuntimeException("QR not found");
        }

        Resource resource = new UrlResource(path.toUri()); // ✅ correct

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"user_qr.png\"")
                .body(resource);
    }



}
