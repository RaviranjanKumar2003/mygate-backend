package com.example.demo.PaymentDueReminder.Reminder;

import com.example.demo.Entities.User;
import com.example.demo.Enums.UserRole;
import com.example.demo.Repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monthly-bills")
public class MonthlyBillController {

    @Autowired
    private MonthlyBillService monthlyBillService;

    @Autowired
    private UserRepository userRepository;



// CREATE MONTHLY BILL
    //1.  Super Admin → bills for Society Admin
   @PostMapping("/super-admin/monthly-bill")
   public ResponseEntity<MonthlyBillDto> createForSocietyAdmin(@RequestBody MonthlyBillDto dto) {
       return ResponseEntity.ok(monthlyBillService.createMonthlyBill(dto, UserRole.valueOf("SUPER_ADMIN")));
   }

    //2.  Society Admin → bills for Owners/Tenants
    @PostMapping("/society-admin/monthly-bill")
    public ResponseEntity<MonthlyBillDto> createForFlatUsers(@RequestBody MonthlyBillDto dto) {
        return ResponseEntity.ok(monthlyBillService.createMonthlyBill(dto, UserRole.valueOf("SOCIETY_ADMIN")));
    }


// GET MONTHLY BILL BY ID
    @GetMapping("/{billId}")
    public ResponseEntity<MonthlyBillDto> getById(@PathVariable Integer billId) {
        return ResponseEntity.ok(monthlyBillService.getBillById(billId));
    }


//  Society bills
    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<MonthlyBillDto>> getBySociety(@PathVariable Integer societyId) {
        return ResponseEntity.ok(
                monthlyBillService.getBillsBySociety(societyId)
        );
    }


//  User bills (Tenant / Owner)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MonthlyBillDto>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(
                monthlyBillService.getBillsByUser(userId)
        );
    }

// GET MONTHLY BILL BY SUPER ADMIN (JO BILL SOCIETY ADMIN KE LIYE CREATE KIYA HAI)
    @GetMapping("/super-admin")
    public List<MonthlyBillDto> getSuperAdminBills() {
        return monthlyBillService.getBillsCreatedBySuperAdmin();
    }
// GET MONTHLY BILL BY SUPER ADMIN (JO BILL SOCIETY ADMIN KE LIYE CREATE KIYA HAI) BY SOCIETY
    @GetMapping("/super-admin/society/{societyId}")
    public List<MonthlyBillDto> getSuperAdminBillsBySociety(
            @PathVariable Integer societyId
    ) {
        return monthlyBillService.getBillsCreatedBySuperAdminBySociety(societyId);
    }

// society admin ke khud ka create kiya huaa bills
    @GetMapping("/society-admin/society/{societyId}")
    public List<MonthlyBillDto> getSocietyAdminBills(
            @PathVariable Integer societyId
    ) {
        return monthlyBillService.getBillsCreatedBySocietyAdmin(societyId);
    }


//  Update status
   @PutMapping("society/{societyId}/billId/{billId}/status")
   public ResponseEntity<MonthlyBillDto> updateStatus(
           @PathVariable Integer billId,
           @PathVariable Integer societyId,
           Authentication authentication
   ) {
       String email = authentication.getName(); // ✅ always works

       // email se user nikaalo
       User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new RuntimeException("User not found"));

       MonthlyBillDto dto = monthlyBillService.updateBillStatus(
               billId,
               societyId,
               user.getId(),
               user.getUserRole()
       );

       return ResponseEntity.ok(dto);
   }



//  Delete bill
    @DeleteMapping("/{billId}")
    public ResponseEntity<Void> delete(@PathVariable Integer billId) {
        monthlyBillService.deleteBill(billId);
        return ResponseEntity.noContent().build();
    }
}
