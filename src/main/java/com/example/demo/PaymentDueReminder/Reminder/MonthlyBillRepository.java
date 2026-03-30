package com.example.demo.PaymentDueReminder.Reminder;

import com.example.demo.Enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface MonthlyBillRepository extends JpaRepository<MonthlyBill, Integer> {

    // 📄 Get all bills of a society
    List<MonthlyBill> findBySocietyIdOrderByBillMonthDesc(Integer societyId);

    // 📄 Get bills of a user (tenant/owner)
    List<MonthlyBill> findByUserIdOrderByBillMonthDesc(Integer userId);

    // 🔍 Check if bill already exists for month
    Optional<MonthlyBill> findBySocietyIdAndUserIdAndBillMonth(
            Integer societyId,
            Integer userId,
            String billMonth
    );

    // SOCIETY ADMIN
    List<MonthlyBill> findByCreatedByRoleAndSocietyIdOrderByBillMonthDesc(
            UserRole role,
            Integer societyId
    );

    // 📄 Get pending bills (for reminder / dashboard)
    List<MonthlyBill> findByStatus(String status);


    // 🔹 Super Admin created bills (for Society Admins)
    List<MonthlyBill> findByReceiverRoleOrderByBillMonthDesc(UserRole receiverRole);


    // 🔹 (Optional) Society-wise Super Admin bills
    List<MonthlyBill> findBySocietyIdAndReceiverRoleOrderByBillMonthDesc(
            Integer societyId,
            UserRole receiverRole
    );


    // 🔹 Bills created by SUPER_ADMIN
    List<MonthlyBill> findByCreatedByRoleOrderByBillMonthDesc(UserRole createdByRole);


}
