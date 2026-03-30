package com.example.demo.PaymentDueReminder.Reminder;

import com.example.demo.Enums.UserRole;

import java.util.List;

public interface MonthlyBillService {

    //  Create monthly bill (manual / auto)
    MonthlyBillDto createMonthlyBill(MonthlyBillDto dto, UserRole creatorRole);


    //  Get single bill
    MonthlyBillDto getBillById(Integer billId);


    //  Get bills of society
    List<MonthlyBillDto> getBillsBySociety(Integer societyId);


    //  Get bills of user (Tenant / Owner)
    List<MonthlyBillDto> getBillsByUser(Integer userId);

    // 🔹 Super Admin created bills
    List<MonthlyBillDto> getBillsCreatedBySuperAdmin();

    // 🔹 (Optional) society-wise
    List<MonthlyBillDto> getBillsCreatedBySuperAdminBySociety(Integer societyId);

    List<MonthlyBillDto> getBillsCreatedBySocietyAdmin(Integer societyId);


    //  Update status (after payment)
    MonthlyBillDto updateBillStatus(Integer billId,Integer societyId,Integer userId,UserRole updaterRole);


    //  Delete bill (only if no payment)
    void deleteBill(Integer billId);


}
