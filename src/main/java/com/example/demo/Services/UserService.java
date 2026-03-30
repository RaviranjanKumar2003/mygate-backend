package com.example.demo.Services;

import com.example.demo.Enums.UserRole;
import com.example.demo.Enums.UserStatus;
import com.example.demo.Payloads.*;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

// CREATE
    UserDto createUser(UserDto userDto) throws Exception;



//==============================  GET USERS ===========================================

    //=========== SOCIETY LEVEL
    // 1.
    List<UserProfileDto> getAllUsersOfSociety(Integer societyId);
    // 2.
    UserCountResponse getSocietyUserSummary(Integer societyId);
    // 3.
    List<UserDto> getUsersOfSocietyByRole(Integer societyId, UserRole role);
    // 4.
    List<UserDto> getUsersOfSocietyByStatus(Integer societyId, UserStatus status);


    UserDto getUserById(Integer userId, Integer societyId);



// UPDATE & DELETE
    UserDto updateUser(Integer userId, UserDto dto);



// DELETE USER
    ApiResponse deleteUser(Integer userId, Integer societyId, Integer buildingId);



    // ==================== PAGINATION & SORT ====================
    List<UserDto> getUsersOfSocietyPaged(Integer societyId, int page, int size, String sortBy, String sortDir);

    // Search users by email in a society
    List<UserDto> searchUsersByEmailInSociety(Integer societyId, String emailKeyword);


// SEARCH USER BY NAME
    List<UserDto> searchUserByName(Integer societyId, String keyword);


    NormalUserProfileDto getUserByFlatId(Integer flatId);


    // ==================== IMAGE ====================
    UserDto updateUserImage(Integer userId, MultipartFile image);


}
