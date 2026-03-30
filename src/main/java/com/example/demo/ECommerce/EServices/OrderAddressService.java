package com.example.demo.ECommerce.EServices;

import com.example.demo.ECommerce.Dtos.OrderAddressDto;

import java.util.List;

public interface OrderAddressService {

    OrderAddressDto addAddress(OrderAddressDto dto);

    List<OrderAddressDto> getUserAddresses(Long userId);

    public OrderAddressDto getAddressById(Long id);

    OrderAddressDto updateAddress(Long id, OrderAddressDto dto);

    void deleteAddress(Long id);


}