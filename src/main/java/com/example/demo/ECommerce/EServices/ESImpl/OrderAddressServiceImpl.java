package com.example.demo.ECommerce.EServices.ESImpl;

import com.example.demo.ECommerce.Dtos.OrderAddressDto;
import com.example.demo.ECommerce.ERepositories.OrderAddressRepository;
import com.example.demo.ECommerce.EServices.OrderAddressService;
import com.example.demo.ECommerce.Eentities.OrderAddress;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderAddressServiceImpl implements OrderAddressService {

    private final OrderAddressRepository repo;

    public OrderAddressServiceImpl(OrderAddressRepository repo) {
        this.repo = repo;
    }


    @Override
    public OrderAddressDto addAddress(OrderAddressDto dto) {
        OrderAddress addr = new OrderAddress();
        addr.setUserId(dto.getUserId());
        addr.setName(dto.getName());
        addr.setLine1(dto.getLine1());
        addr.setLine2(dto.getLine2());
        addr.setCity(dto.getCity());
        addr.setState(dto.getState());
        addr.setZip(dto.getZip());
        addr.setCountry(dto.getCountry());
        addr.setPhone(dto.getPhone());

        OrderAddress saved = repo.save(addr);
        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public List<OrderAddressDto> getUserAddresses(Long userId) {
        return repo.findByUserId(userId).stream().map(addr -> {
            OrderAddressDto dto = new OrderAddressDto();
            dto.setId(addr.getId());
            dto.setUserId(addr.getUserId());
            dto.setName(addr.getName());
            dto.setLine1(addr.getLine1());
            dto.setLine2(addr.getLine2());
            dto.setCity(addr.getCity());
            dto.setState(addr.getState());
            dto.setZip(addr.getZip());
            dto.setCountry(addr.getCountry());
            dto.setPhone(addr.getPhone());
            return dto;
        }).toList();
    }

    public OrderAddressDto getAddressById(Long id) {
        OrderAddress addr = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        OrderAddressDto dto = new OrderAddressDto();
        dto.setId(addr.getId());
        dto.setName(addr.getName());
        dto.setLine1(addr.getLine1());
        dto.setCity(addr.getCity());
        dto.setState(addr.getState());
        dto.setPhone(addr.getPhone());
        return dto;
    }

    @Override
    public OrderAddressDto updateAddress(Long id, OrderAddressDto dto) {
        OrderAddress addr = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        addr.setName(dto.getName());
        addr.setLine1(dto.getLine1());
        addr.setLine2(dto.getLine2());
        addr.setCity(dto.getCity());
        addr.setState(dto.getState());
        addr.setZip(dto.getZip());
        addr.setCountry(dto.getCountry());
        addr.setPhone(dto.getPhone());

        OrderAddress updated = repo.save(addr);

        OrderAddressDto res = new OrderAddressDto();
        res.setId(updated.getId());
        res.setUserId(updated.getUserId());
        res.setName(updated.getName());
        res.setLine1(updated.getLine1());
        res.setLine2(updated.getLine2());
        res.setCity(updated.getCity());
        res.setState(updated.getState());
        res.setZip(updated.getZip());
        res.setCountry(updated.getCountry());
        res.setPhone(updated.getPhone());

        return res;
    }

    @Override
    public void deleteAddress(Long id) {
        repo.deleteById(id);
    }
}