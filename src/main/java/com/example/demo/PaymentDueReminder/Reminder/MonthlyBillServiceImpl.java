package com.example.demo.PaymentDueReminder.Reminder;

import com.example.demo.Entities.NormalUser;
import com.example.demo.Entities.Notification;
import com.example.demo.Entities.SocietyAdmin;
import com.example.demo.Entities.User;
import com.example.demo.Enums.NotificationType;
import com.example.demo.Enums.PaymentStatus;
import com.example.demo.Enums.TargetAudience;
import com.example.demo.Enums.UserRole;
import com.example.demo.Repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonthlyBillServiceImpl implements MonthlyBillService {

    @Autowired
    private MonthlyBillRepository monthlyBillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SocietyRepo societyRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SocietyAdminRepository societyAdminRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private NormalUserRepository normalUserRepository;

    @Autowired
    private NotificationRepository notificationRepository;


//  CREATE BILL
@Override
public MonthlyBillDto createMonthlyBill(MonthlyBillDto dto, UserRole creatorRole) {

    // 1️⃣ Duplicate bill check
    monthlyBillRepository.findBySocietyIdAndUserIdAndBillMonth(
            dto.getSocietyId(),
            dto.getUserId(),
            dto.getBillMonth()
    ).ifPresent(b -> {
        throw new RuntimeException("Bill already exists for this month");
    });

    User user;
    NormalUser normalUser = null;
    String userName;

    // 2️⃣ Role based validation
    if (creatorRole == UserRole.SOCIETY_ADMIN) {

        user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserRole() != UserRole.NORMAL_USER) {
            throw new RuntimeException("Bills can be created only for Owner/Tenant");
        }

        normalUser = normalUserRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Normal user details not found"));

        boolean existsInSociety = flatRepository.existsByIdAndSociety_Id(
                dto.getFlatId(),
                dto.getSocietyId()
        );

        if (!existsInSociety) {
            throw new RuntimeException("User does not belong to this society");
        }

        userName = user.getName();
    }
    else if (creatorRole == UserRole.SUPER_ADMIN) {

        SocietyAdmin admin = societyAdminRepository
                .findByIdAndSociety_Id(dto.getUserId(), dto.getSocietyId())
                .orElseThrow(() ->
                        new RuntimeException("Society Admin not found in this society")
                );

        userName = admin.getAdminName();
    }
    else {
        throw new RuntimeException("Invalid creator role");
    }

    // 3️⃣ DTO → Entity
    MonthlyBill bill = modelMapper.map(dto, MonthlyBill.class);

    // 4️⃣ Receiver role (ONLY for Normal Users)
    if (creatorRole == UserRole.SOCIETY_ADMIN && normalUser != null) {
        bill.setReceiverRole(normalUser.getNormalUserType()); // OWNER / TENANT
    }

    // 5️⃣ Mandatory fields
    bill.setCreatedByRole(creatorRole);
    bill.setStatus(PaymentStatus.PENDING);
    bill.setPaidAmount(0.0);
    bill.setDueAmount(dto.getTotalAmount());
    bill.setCreatedAt(LocalDateTime.now());
    bill.setUpdatedAt(LocalDateTime.now());
    bill.setUserName(userName);

    // 6️⃣ Society name
    societyRepo.findById(dto.getSocietyId())
            .ifPresent(s -> bill.setSocietyName(s.getName()));

    MonthlyBill saved = monthlyBillRepository.save(bill);

    // ================== 🔔 CREATE NOTIFICATION ==================
    Notification notification = new Notification();
    notification.setTitle("New Monthly Bill Generated");
    notification.setMessage(
            "Bill of ₹" + saved.getTotalAmount() +
                    " for " + saved.getBillMonth()
    );
    notification.setType(NotificationType.PAYMENT);
    notification.setReferenceId(Long.valueOf(saved.getId()));
    notification.setRead(false);
    notification.setCreatedAt(LocalDateTime.now());

    if (creatorRole == UserRole.SOCIETY_ADMIN) {
        // 🔵 Notify Normal User
        notification.setTargetRole(TargetAudience.NORMAL_USER.name());
        notification.setTargetSocietyId(saved.getSocietyId());
        notification.setTargetSocietyId(saved.getSocietyId());
    } else {
        // 🔴 Notify Society Admin
        notification.setTargetRole(TargetAudience.SOCIETY_ADMIN.name());
        notification.setTargetSocietyId(saved.getSocietyId());
        notification.setSocietyId(saved.getSocietyId());
    }

    notificationRepository.save(notification);
    // ================== END ==================

    return mapToDto(saved);
}

//  GET BILL BY ID
    @Override
    public MonthlyBillDto getBillById(Integer billId) {
        MonthlyBill bill = monthlyBillRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        return mapToDto(bill);
    }


// GET SOCIETY BILLS
    @Override
    public List<MonthlyBillDto> getBillsBySociety(Integer societyId) {

        return monthlyBillRepository
                .findBySocietyIdOrderByBillMonthDesc(societyId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


//  GET USER BILLS
    @Override
    public List<MonthlyBillDto> getBillsByUser(Integer userId) {

        return monthlyBillRepository
                .findByUserIdOrderByBillMonthDesc(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyBillDto> getBillsCreatedBySuperAdmin() {

        return monthlyBillRepository
                .findByCreatedByRoleOrderByBillMonthDesc(UserRole.SUPER_ADMIN)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<MonthlyBillDto> getBillsCreatedBySuperAdminBySociety(Integer societyId) {

        return monthlyBillRepository
                .findByCreatedByRoleAndSocietyIdOrderByBillMonthDesc(
                        UserRole.SUPER_ADMIN,
                        societyId
                )
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<MonthlyBillDto> getBillsCreatedBySocietyAdmin(Integer societyId) {
        return monthlyBillRepository
                .findByCreatedByRoleAndSocietyIdOrderByBillMonthDesc(
                        UserRole.SOCIETY_ADMIN,
                        societyId
                )
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


// UPDATE STATUS AFTER PAYMENT
public MonthlyBillDto updateBillStatus(
        Integer billId,
        Integer societyId,
        Integer updaterUserId,
        UserRole updaterRole
) {
    MonthlyBill bill = monthlyBillRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found"));

    // 🔐 society validation
    if (!bill.getSocietyId().equals(societyId)) {
        throw new RuntimeException("Bill does not belong to this society");
    }

    // 🔐 user-level restriction
    if (updaterRole == UserRole.NORMAL_USER &&
            !bill.getUserId().equals(updaterUserId)) {
        throw new RuntimeException("Cannot update other's bill");
    }

    // ✅ status logic
    if (bill.getDueAmount() <= 0) {
        bill.setStatus(PaymentStatus.COMPLETED);
    } else {
        bill.setStatus(PaymentStatus.PENDING);
    }

    // 🧾 audit
    bill.setLastUpdatedByRole(updaterRole);
    bill.setLastUpdatedByUserId(updaterUserId);
    bill.setStatusUpdatedAt(LocalDateTime.now());

    return mapToDto(monthlyBillRepository.save(bill));
}


    // ❌ DELETE BILL
    @Override
    public void deleteBill(Integer billId) {

        MonthlyBill bill = monthlyBillRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (bill.getPaidAmount() > 0) {
            throw new RuntimeException("Cannot delete bill with payments");
        }

        monthlyBillRepository.delete(bill);
    }

    // ================= HELPER =================

    private MonthlyBillDto mapToDto(MonthlyBill bill) {
        return modelMapper.map(bill, MonthlyBillDto.class);
    }
}
