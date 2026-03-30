package com.example.demo.Services.Implementation;

import com.example.demo.Entities.NormalUser;
import com.example.demo.Entities.Society;
import com.example.demo.Entities.Staff;
import com.example.demo.Entities.User;
import com.example.demo.Enums.FlatStatus;
import com.example.demo.Enums.UserRole;
import com.example.demo.Enums.UserStatus;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.*;
import com.example.demo.Repositories.*;
import com.example.demo.Services.UserService;
import com.example.demo.Services.VerificationCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private SocietyRepo societyRepo;

    @Autowired
    private BuildingRepo buildingRepo;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private NormalUserRepository normalUserRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationCodeService verificationCodeService;



//=============================================================================================
private String generateUnique6DigitCode() {

    String code;

    do {
        // 6-digit random number (100000–999999)
        code = String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 1000000)
        );
    } while (userRepository.existsByEntryCode(code));

    return code;
}

// CREATE USERS
   @Override
   @Transactional
   public UserDto createUser(UserDto userDto) throws Exception {

       if (userDto.getSocietyId() == null) {
           throw new BadRequestException("Society ID is required");
       }

       // FETCH SOCIETY
       Society society = societyRepo.findById(userDto.getSocietyId())
               .orElseThrow(() -> new ResourceNotFoundException("Society", "id", userDto.getSocietyId()));

       // CHECK IF USER ALREADY EXISTS (email + society)
       Optional<User> existingUserOpt = userRepository.findByEmailAndSocietyId(userDto.getEmail(), userDto.getSocietyId());

       User user;
       if (existingUserOpt.isPresent()) {
           // UPDATE EXISTING USER
           user = existingUserOpt.get();

           user.setName(userDto.getName() != null ? userDto.getName() : user.getName());
           user.setPassword(userDto.getPassword() != null ? userDto.getPassword() : user.getPassword());
           user.setMobileNumber(userDto.getMobileNumber() != null ? userDto.getMobileNumber() : user.getMobileNumber());
           user.setUserRole(userDto.getUserRole() != null ? userDto.getUserRole() : user.getUserRole());
           user.setUserStatus(userDto.getUserStatus() != null ? userDto.getUserStatus() : user.getUserStatus());

       } else {
           // CREATE NEW USER
           user = new User();
           user.setName(userDto.getName());
           user.setEmail(userDto.getEmail());
           user.setPassword(passwordEncoder.encode(userDto.getPassword()));
           user.setMobileNumber(userDto.getMobileNumber());
           user.setUserRole(userDto.getUserRole());
           user.setUserStatus(userDto.getUserStatus());
           user.setSociety(society);

           String entryCode = generateUnique6DigitCode();
           user.setEntryCode(entryCode);

           user = userRepository.save(user);

           // 🔥 GENERATE QR
           String qrText = "USER:" + user.getId()
                   + "|CODE:" + entryCode
                   + "|ROLE:" + user.getUserRole()
                   + "|SOCIETY:" + society.getId();

           String qrPath = generateQrCode(qrText, user.getId());
           user.setQrCodePath(qrPath);

           // 🔥 UPDATE USER
           user = userRepository.save(user);

//           // 🔹 GENERATE VerificationCode (for guard verification)
//           VerificationCodeDto vCodeDto = verificationCodeService.generateCode(user.getId());
//           System.out.println("Generated VerificationCode for user: " + vCodeDto.getCode());

       }

       // ---------- NORMAL USER ----------
       if (user.getUserRole() == UserRole.NORMAL_USER) {

           if (userDto.getFlatId() == null || userDto.getNormalUserType() == null) {
               throw new BadRequestException("Flat & NormalUserType are required for NORMAL_USER");
           }

           // CHECK IF NormalUser RECORD EXISTS
           NormalUser normalUser = normalUserRepository.findByUser(user).orElse(new NormalUser());
           normalUser.setUser(user);
           normalUser.setEmail(user.getEmail());
           normalUser.setPassword(passwordEncoder.encode(user.getPassword()));

           // FLAT OCCUPANCY CHECK
           Optional<NormalUser> flatOccupied = normalUserRepository
                   .findByFlat_Floor_Building_Society_IdAndBuilding_IdAndFloor_IdAndFlat_Id(
                           userDto.getSocietyId(),
                           userDto.getBuildingId(),
                           userDto.getFloorId(),
                           userDto.getFlatId()
                   );

           if (flatOccupied.isPresent() && !Objects.equals(flatOccupied.get().getUser().getId(), user.getId())) {
               throw new BadRequestException("This flat is already occupied by another user in the same society/building/floor");
           }


           normalUser.setBuilding(buildingRepo.findById(userDto.getBuildingId())
                  .orElseThrow(() -> new ResourceNotFoundException("Building", "id", userDto.getBuildingId())));
           normalUser.setFloor(floorRepository.findById(userDto.getFloorId())
                   .orElseThrow(() -> new ResourceNotFoundException("Floor", "id", userDto.getFloorId())));
           normalUser.setFlat(flatRepository.findById(userDto.getFlatId())
                   .orElseThrow(() -> new ResourceNotFoundException("Flat", "id", userDto.getFlatId())));
           normalUser.setNormalUserType(userDto.getNormalUserType());

           normalUserRepository.save(normalUser);
       }

       // STAFF
       if (user.getUserRole() == UserRole.STAFF) {

           if (userDto.getStaffType() == null || userDto.getStaffLevel() == null) {
               throw new BadRequestException("StaffType & StaffLevel are required for STAFF");
           }

           // CHECK IF STAFF RECORD EXISTS
           Staff staff = staffRepository.findByUser(user).orElse(new Staff());
           staff.setUser(user);
           staff.setStaffType(userDto.getStaffType());
           staff.setStaffLevel(userDto.getStaffLevel());
           staff.setDutyTiming(userDto.getDutyTiming());
           staff.setSalary(userDto.getSalary());
           staff.setEmail(userDto.getEmail());
           staff.setPassword(passwordEncoder.encode(userDto.getPassword()));


           staffRepository.save(staff);
       }

       // RESPONSE
       UserDto response = new UserDto();
       response.setId(user.getId());
       response.setName(user.getName());
       response.setEmail(user.getEmail());
       response.setMobileNumber(user.getMobileNumber());
       response.setUserRole(user.getUserRole());
       response.setUserStatus(user.getUserStatus());
       response.setSocietyId(user.getSociety().getId());
       response.setCreatedAt(user.getCreatedAt());

       response.setEntryCode(user.getEntryCode());
       response.setQrCodePath(user.getQrCodePath());


       return response;
   }

// GENERATE QR
private String generateQrCode(String text, Integer userId) throws Exception {

    int width = 300;
    int height = 300;

    BitMatrix matrix = new MultiFormatWriter()
            .encode(text, BarcodeFormat.QR_CODE, width, height);

    // 🔥 DIRECTORY PATH
    String dirPath = "uploads/qr";
    File dir = new File(dirPath);

    // 🔥 CREATE FOLDER IF NOT EXISTS
    if (!dir.exists()) {
        dir.mkdirs();
    }

    // 🔥 FILE PATH
    String filePath = dirPath + "/user_" + userId + ".png";
    Path path = Paths.get(filePath);

    MatrixToImageWriter.writeToPath(matrix, "PNG", path);

    return filePath;
}




    //==================================================================================================
// GET ALL USERS (STAFF + NORMAL USER) IN A SOCIETY
   @Override
   public List<UserProfileDto> getAllUsersOfSociety(Integer societyId) {

       List<User> users = userRepository.findBySocietyId(societyId);

       return users.stream().map(user -> {

           UserProfileDto dto = new UserProfileDto();
           dto.setId(user.getId());
           dto.setName(user.getName());
           dto.setEmail(user.getEmail());
           dto.setMobileNumber(user.getMobileNumber());
           dto.setUserRole(user.getUserRole());
           dto.setUserStatus(user.getUserStatus());
           dto.setCreatedAt(user.getCreatedAt());
           dto.setPassword(null); // hide password

           //  NORMAL USER
           if (user.getUserRole() == UserRole.NORMAL_USER) {
                  normalUserRepository.findByUser(user).ifPresent(nu -> {
                   dto.setNormalUserType(nu.getNormalUserType());
                   dto.setBuildingId(nu.getBuilding().getId());
                   dto.setFloorId(nu.getFloor().getId());
                   dto.setFlatId(nu.getFlat().getId());
               });
           }

           //  STAFF
           if (user.getUserRole() == UserRole.STAFF) {
               staffRepository.findByUser(user).ifPresent(staff -> {
                   dto.setStaffType(staff.getStaffType());
                   dto.setStaffLevel(staff.getStaffLevel());
                   dto.setDutyTiming(staff.getDutyTiming());
                   dto.setSalary(staff.getSalary());
               });
           }

           return dto;

       }).collect(Collectors.toList());
   }



// GET SOCIETY SUMMARY
    @Override
    public UserCountResponse getSocietyUserSummary(Integer societyId) {
        UserCountResponse response = new UserCountResponse();

        response.setTotalUsers(userRepository.countBySocietyId(societyId));

        Map<String, Long> roleWise = new HashMap<>();
        for (UserRole role : UserRole.values()) {
            roleWise.put(role.name(), userRepository.countBySocietyIdAndUserRole(societyId, role));
        }

        Map<String, Long> statusWise = new HashMap<>();
        for (UserStatus status : UserStatus.values()) {
            statusWise.put(status.name(), userRepository.countBySocietyIdAndUserStatus(societyId, status));
        }

        response.setRoleWiseCount(roleWise);
        response.setStatusWiseCount(statusWise);

        return response;
    }


// GET USER IN A SOCIETY BY ROLE
    @Override
    public List<UserDto> getUsersOfSocietyByRole(Integer societyId, UserRole role) {
        return userRepository.findBySocietyIdAndUserRole(societyId, role)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


// GET USER BY STATUS
    @Override
    public List<UserDto> getUsersOfSocietyByStatus(Integer societyId, UserStatus status) {
        return userRepository.findBySocietyIdAndUserStatus(societyId, status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


// GET USER BY ID
@Override
public UserDto getUserById(Integer userId, Integer societyId) {

    User user = userRepository
            .findByIdAndSociety_Id(userId, societyId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User", "id", userId)
            );

    // ✅ USE EXISTING COMPLETE MAPPER
    UserDto dto = mapToDto(user);

    // ✅ ENTRY QR EXTRA
    dto.setEntryCode(user.getEntryCode());
    dto.setQrCodePath(user.getQrCodePath());

    return dto;
}




    // ==================== GET BY Flat ID ====================
    @Override
    public NormalUserProfileDto getUserByFlatId(Integer flatId) {

        NormalUser normalUser = normalUserRepository.findByFlatId(flatId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "NormalUser", "flatId", flatId));

        User user = normalUser.getUser();

        NormalUserProfileDto dto = new NormalUserProfileDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUserStatus(user.getUserStatus());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setNormalUserType(normalUser.getNormalUserType());
        dto.setImageUrl(user.getImageURL());

        return dto;
    }




// UPDATE

    @Override
    public UserDto updateUser(Integer userId, UserDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getMobileNumber() != null) user.setMobileNumber(dto.getMobileNumber());
        if (dto.getUserStatus() != null) user.setUserStatus(dto.getUserStatus());
        if (dto.getUserRole() != null) user.setUserRole(dto.getUserRole());
        if (dto.getImageURL() != null) user.setImageURL(dto.getImageURL());

        return mapToDto(userRepository.save(user));
    }



// DELETE USERS
    @Override
    @Transactional
    public ApiResponse deleteUser(Integer userId, Integer societyId, Integer buildingId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getSociety().getId().equals(societyId)) {
            throw new RuntimeException("User does not belong to this society");
        }

        // 🔥 NORMAL USER CASE
        if (user.getUserRole() == UserRole.NORMAL_USER) {

            normalUserRepository.findByUser(user).ifPresent(normalUser -> {

                // 🔥 RESET FLAT STATUS
                if (normalUser.getFlat() != null) {
                    normalUser.getFlat().setFlatStatus(FlatStatus.VACANT);
                    flatRepository.save(normalUser.getFlat());
                }

                normalUserRepository.delete(normalUser);
            });
        }

        // STAFF
        if (user.getUserRole() == UserRole.STAFF) {
            staffRepository.findByUser(user).ifPresent(staffRepository::delete);
        }

        // 🔥 DELETE USER
        userRepository.delete(user);

        return new ApiResponse("User deleted successfully, flat set to VACANT", true);
    }




/* ========================================== PAGINATION & SORT ================================*/
    @Override
    public List<UserDto> getUsersOfSocietyPaged(Integer societyId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return userRepository.findBySocietyIdWithPage(societyId, PageRequest.of(page, size, sort))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ==================== SEARCH ====================
    @Override
    public List<UserDto> searchUsersByEmailInSociety(Integer societyId, String emailKeyword) {
        return userRepository.findBySocietyIdAndEmailContainingIgnoreCase(societyId, emailKeyword)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> searchUserByName(Integer societyId, String keyword) {
        return userRepository.searchUsersInSociety(societyId, UserStatus.ACTIVE, keyword)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



/* ====================================== IMAGE ======================================*/
    @Override
    public UserDto updateUserImage(Integer userId, MultipartFile image) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // MultipartFile -> String (path / URL)
        String imageUrl = saveFile(image);

        // ✅ CORRECT setter
        user.setImageURL(imageUrl);

        userRepository.save(user);
        return mapToDto(user);
    }

    private String saveFile(MultipartFile file) {

        try {
            String uploadDir = "uploads/profile-images/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.write(filePath, file.getBytes());

            // ✅ ONLY FILE NAME SAVE
            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Image save failed");
        }
    }




/*===================================== HELPER METHOD ==================================================*/
    private UserDto mapToDto(User user) {

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setImageURL(user.getImageURL());

        // ✅ SOCIETY
        if (user.getSociety() != null) {
            dto.setSocietyId(user.getSociety().getId());
            dto.setSocietyName(user.getSociety().getName());
        }

        dto.setUserRole(user.getUserRole());
        dto.setUserStatus(user.getUserStatus());

        // ================= NORMAL USER =================
        if (user.getUserRole() == UserRole.NORMAL_USER) {

            NormalUser normalUser = normalUserRepository.findByUser(user)
                    .orElse(null);

            if (normalUser != null) {

                dto.setNormalUserType(normalUser.getNormalUserType());

                // BUILDING
                if (normalUser.getBuilding() != null) {
                    dto.setBuildingId(normalUser.getBuilding().getId());
                    dto.setBuildingName(normalUser.getBuilding().getName());
                }

                // FLOOR
                if (normalUser.getFloor() != null) {
                    dto.setFloorId(normalUser.getFloor().getId());
                    dto.setFloorNumber(normalUser.getFloor().getFloorNumber());
                }

                // FLAT
                if (normalUser.getFlat() != null) {
                    dto.setFlatId(normalUser.getFlat().getId());
                    dto.setFlatNumber(normalUser.getFlat().getFlatNumber());
                }
            }
        }

        // ================= STAFF =================
        if (user.getUserRole() == UserRole.STAFF) {

            Staff staff = staffRepository.findByUser(user).orElse(null);

            if (staff != null) {
                dto.setStaffType(staff.getStaffType());
                dto.setStaffLevel(staff.getStaffLevel());
                dto.setDutyTiming(staff.getDutyTiming());
                dto.setSalary(staff.getSalary());
            }
        }

        return dto;
    }



    private User mapToEntity(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setMobileNumber(dto.getMobileNumber());
        user.setUserStatus(dto.getUserStatus());
        user.setUserRole(dto.getUserRole());
        user.setImageURL(dto.getImageURL());
        return user;
    }

}
