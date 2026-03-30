package com.example.demo.ECommerce.EControllers;

import com.example.demo.ECommerce.Dtos.OrderAddressDto;
import com.example.demo.ECommerce.EServices.OrderAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class OrderAddressController {

    private final OrderAddressService service;

    public OrderAddressController(OrderAddressService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderAddressDto> addAddress(@RequestBody OrderAddressDto dto) {
        return ResponseEntity.ok(service.addAddress(dto));
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<OrderAddressDto>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserAddresses(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderAddressDto> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAddressById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderAddressDto> updateAddress(
            @PathVariable Long id,
            @RequestBody OrderAddressDto dto
    ) {
        return ResponseEntity.ok(service.updateAddress(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        service.deleteAddress(id);
        return ResponseEntity.ok("Deleted");
    }
}