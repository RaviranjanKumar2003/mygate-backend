package com.example.demo.ECommerce.EServices;

import com.example.demo.ECommerce.Dtos.CartDto;

import java.util.List;

public interface CartService {

    CartDto getCartByBuyer(Long buyerId);

    CartDto addProductToCart(Long buyerId, Long productId, int quantity, Long societyId);

    CartDto updateProductQuantity(Long buyerId, Long productId, int quantity);

    CartDto removeProductFromCart(Long buyerId, Long productId);

    void clearCart(Long buyerId);

    List<CartDto> getAllCarts();
}