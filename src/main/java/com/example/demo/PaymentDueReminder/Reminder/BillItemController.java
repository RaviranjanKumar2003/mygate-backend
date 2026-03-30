package com.example.demo.PaymentDueReminder.Reminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill-items")
public class BillItemController {

    @Autowired
    private BillItemService billItemService;


// Add item to bill
   @PostMapping("/society/{societyId}")
   public ResponseEntity<BillItemDto> addBillItem(
           @PathVariable Integer societyId,
           @RequestBody BillItemDto dto
   ) {
       return ResponseEntity.ok(billItemService.createBillItem(dto, societyId));
   }

    //  Get items of a bill
    @GetMapping("/bill/{billId}")
    public ResponseEntity<List<BillItemDto>> getByBill(@PathVariable Integer billId) {
        return ResponseEntity.ok(
                billItemService.getItemsByMonthlyBill(billId)
        );
    }

    // ✏ Update item
    @PutMapping("/{itemId}")
    public ResponseEntity<BillItemDto> update(
            @PathVariable Integer itemId,
            @RequestBody BillItemDto dto
    ) {
        return ResponseEntity.ok(
                billItemService.updateBillItem(itemId, dto)
        );
    }

    //  Delete item
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Integer itemId) {
        billItemService.deleteBillItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
