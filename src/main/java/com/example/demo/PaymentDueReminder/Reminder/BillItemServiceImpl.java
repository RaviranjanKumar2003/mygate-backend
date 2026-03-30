package com.example.demo.PaymentDueReminder.Reminder;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BillItemServiceImpl implements BillItemService {


    @Autowired
    private BillItemRepository billItemRepository;

    @Autowired
    private MonthlyBillRepository monthlyBillRepository;

    @Autowired
    private ModelMapper modelMapper;



// CREATE ITEM
   @Override
public BillItemDto createBillItem(BillItemDto dto, Integer societyId) {

    MonthlyBill bill = monthlyBillRepository.findById(dto.getMonthlyBillId())
            .orElseThrow(() -> new RuntimeException("Monthly Bill not found"));

    // 🔒 Society validation
    if (!bill.getSocietyId().equals(societyId)) {
        throw new RuntimeException("Unauthorized society access");
    }

    BillItem item = new BillItem();
    item.setTitle(dto.getTitle());
    item.setDescription(dto.getDescription());
    item.setAmount(dto.getAmount());
    item.setMonthlyBill(bill);

    BillItem saved = billItemRepository.save(item);
    recalculateBillTotal(bill);

    return mapToDto(saved);
}

    // GET ITEMS BY BILL
    @Override
    public List<BillItemDto> getItemsByMonthlyBill(Integer monthlyBillId) {

        List<BillItem> items =
                billItemRepository.findByMonthlyBillId(monthlyBillId);

        return items.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // UPDATE ITEM
    @Override
    public BillItemDto updateBillItem(Integer itemId, BillItemDto dto) {

        BillItem item = billItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Bill item not found"));

        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setAmount(dto.getAmount());

        BillItem updated = billItemRepository.save(item);

        //  Recalculate bill total
        recalculateBillTotal(item.getMonthlyBill());

        return mapToDto(updated);
    }

    //  DELETE ITEM
    @Override
    public void deleteBillItem(Integer itemId) {

        BillItem item = billItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Bill item not found"));

        MonthlyBill bill = item.getMonthlyBill();

        billItemRepository.delete(item);

        //  Recalculate bill total
        recalculateBillTotal(bill);
    }

    // ================== HELPER METHODS ==================

    private void recalculateBillTotal(MonthlyBill bill) {

        Double total = billItemRepository
                .sumAmountByMonthlyBillId(bill.getId());

        bill.setTotalAmount(total != null ? total : 0);
        monthlyBillRepository.save(bill);
    }

    private BillItemDto mapToDto(BillItem item) {
        BillItemDto dto = modelMapper.map(item, BillItemDto.class);
        dto.setMonthlyBillId(item.getMonthlyBill().getId());
        return dto;
    }
}
