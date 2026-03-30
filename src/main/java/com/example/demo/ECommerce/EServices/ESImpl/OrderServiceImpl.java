package com.example.demo.ECommerce.EServices.ESImpl;

import com.example.demo.ECommerce.Dtos.OrderDto;
import com.example.demo.ECommerce.ERepositories.OrderRepository;
import com.example.demo.ECommerce.ERepositories.OrderAddressRepository;
import com.example.demo.ECommerce.EServices.OrderService;
import com.example.demo.ECommerce.Eentities.Order;
import com.example.demo.ECommerce.Eentities.OrderAddress;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderAddressRepository addressRepository; // ✅ ADD THIS

    // ✅ Constructor update
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderAddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
    }

    /* ================= DTO MAPPING ================= */

    private OrderDto mapToDto(Order order) {
        OrderDto dto = new OrderDto(
                order.getId(),
                order.getBuyerId(),
                order.getSocietyId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate(),
                order.getProductIds(),
                order.getQuantities()
        );

        dto.setPaymentMethod(order.getPaymentMethod());

        // ✅ optional: return addressId
        if (order.getDeliveryAddress() != null) {
            dto.setAddressId(order.getDeliveryAddress().getId());
        }

        return dto;
    }

    private Order mapToEntity(OrderDto dto) {
        Order order = new Order();

        order.setBuyerId(dto.getBuyerId());
        order.setSocietyId(dto.getSocietyId());
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(dto.getStatus());
        order.setPaymentMethod(dto.getPaymentMethod());

        order.setOrderDate(
                dto.getOrderDate() != null ? dto.getOrderDate() : LocalDateTime.now()
        );
        order.setProductIds(dto.getProductIds());
        order.setQuantities(dto.getQuantities());

        // ✅ 🔥 IMPORTANT FIX (existing address use karo)
        if (dto.getAddressId() != null) {
            OrderAddress address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new RuntimeException("Address not found"));
            order.setDeliveryAddress(address);
        }

        return order;
    }

    /* ================= SERVICES ================= */

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = mapToEntity(orderDto);
        Order saved = orderRepository.save(order);
        return mapToDto(saved);
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order updated = orderRepository.save(order);

        return mapToDto(updated);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<OrderDto> getOrdersByBuyer(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersBySociety(Long societyId) {
        return orderRepository.findBySocietyId(societyId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}