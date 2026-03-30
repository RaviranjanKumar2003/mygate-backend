package com.example.demo.PaymentDueReminder.Reminder;

import java.util.List;

public interface BillItemService {

    // ➕ Add item to bill
    BillItemDto createBillItem(BillItemDto dto, Integer societyId);

    // 📄 Get all items of a bill
    List<BillItemDto> getItemsByMonthlyBill(Integer monthlyBillId);

    // ✏️ Update item
    BillItemDto updateBillItem(Integer itemId, BillItemDto dto);

    // ❌ Delete item
    void deleteBillItem(Integer itemId);

}
