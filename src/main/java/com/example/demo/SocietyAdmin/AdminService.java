package com.example.demo.SocietyAdmin;

import com.example.demo.Entities.User;
import com.example.demo.Enums.UserRole;
import com.example.demo.Enums.UserStatus;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.UserDto;
import com.example.demo.Repositories.StaffRepository;
import com.example.demo.Repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper mapper;


    public AdminService(UserRepository userRepository, ModelMapper mapper, StaffRepository staffRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.staffRepository = staffRepository;
    }



//============================ ROLE PENDING USERS ============================

//  Get all PENDING (IN) users
    public List<UserDto> getPendingUsers() {
        return userRepository.findByUserRole(UserRole.PENDING)
                .stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }


//  Approve user
    @Transactional
    public UserDto approveUser(Integer userId, UserRole role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", userId));

        if (user.getUserRole() != UserRole.PENDING) {
            throw new IllegalStateException("User is not in PENDING Role");
        }

        user.setUserRole(role);
        user.setUserStatus(UserStatus.ACTIVE);

        return mapper.map(userRepository.save(user), UserDto.class);
    }


//  Deactivate user
    @Transactional
    public UserDto deactivateUser(Integer userId) {

        User user = userRepository.findById(userId).orElseThrow(() ->new ResourceNotFoundException("User", "id", userId));

        user.setUserStatus(UserStatus.INACTIVE);

        return mapper.map(userRepository.save(user), UserDto.class);
    }
}


//============================ STATUS PENDING STAFF ============================

