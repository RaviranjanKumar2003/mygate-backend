package com.example.demo.ECommerce.EServices;

import com.example.demo.ECommerce.Dtos.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto updateOrderStatus(Long orderId, String status);

    void deleteOrder(Long orderId);

    OrderDto getOrderById(Long orderId);

    List<OrderDto> getOrdersByBuyer(Long buyerId);

    List<OrderDto> getOrdersBySociety(Long societyId);

    List<OrderDto> getAllOrders();
}