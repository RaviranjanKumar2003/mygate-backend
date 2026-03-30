package com.example.demo.ECommerce.EServices.ESImpl;

import com.example.demo.ECommerce.Dtos.CartDto;
import com.example.demo.ECommerce.ERepositories.CartRepository;
import com.example.demo.ECommerce.EServices.CartService;
import com.example.demo.ECommerce.Eentities.Cart;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    private CartDto mapToDto(Cart cart) {
        return new CartDto(cart.getId(), cart.getBuyerId(), cart.getSocietyId(), cart.getProductIds(), cart.getQuantities());
    }

    private Cart mapToEntity(CartDto dto) {
        return new Cart(dto.getId(), dto.getBuyerId(), dto.getSocietyId(), dto.getProductIds(), dto.getQuantities());
    }

//
    @Override
    public CartDto getCartByBuyer(Long buyerId) {
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElse(new Cart(null, buyerId, null, new ArrayList<>(), new ArrayList<>()));
        return mapToDto(cart);
    }

//
    @Override
    public CartDto addProductToCart(Long buyerId, Long productId, int quantity, Long societyId) {
        // Try to get existing cart for this buyer AND society
        Cart cart = cartRepository.findByBuyerIdAndSocietyId(buyerId, societyId)
                .orElse(new Cart(null, buyerId, societyId, new ArrayList<>(), new ArrayList<>()));

        List<Long> productIds = cart.getProductIds();
        List<Integer> quantities = cart.getQuantities();

        if (productIds.contains(productId)) {
            int index = productIds.indexOf(productId);
            quantities.set(index, quantities.get(index) + quantity);
        } else {
            productIds.add(productId);
            quantities.add(quantity);
        }

        cart.setProductIds(productIds);
        cart.setQuantities(quantities);

        Cart saved = cartRepository.save(cart);
        return mapToDto(saved);
    }

//
    @Override
    public CartDto updateProductQuantity(Long buyerId, Long productId, int quantity) {
        Cart cart = cartRepository.findByBuyerId(buyerId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<Long> productIds = cart.getProductIds();
        List<Integer> quantities = cart.getQuantities();

        if (productIds.contains(productId)) {
            int index = productIds.indexOf(productId);
            quantities.set(index, quantity);
        } else {
            throw new RuntimeException("Product not found in cart");
        }

        cart.setQuantities(quantities);
        Cart updated = cartRepository.save(cart);
        return mapToDto(updated);
    }

//
    @Override
    public CartDto removeProductFromCart(Long buyerId, Long productId) {
        Cart cart = cartRepository.findByBuyerId(buyerId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<Long> productIds = cart.getProductIds();
        List<Integer> quantities = cart.getQuantities();

        if (productIds.contains(productId)) {
            int index = productIds.indexOf(productId);
            productIds.remove(index);
            quantities.remove(index);
        }

        cart.setProductIds(productIds);
        cart.setQuantities(quantities);
        Cart updated = cartRepository.save(cart);
        return mapToDto(updated);
    }

//
    @Override
    public void clearCart(Long buyerId) {
        Cart cart = cartRepository.findByBuyerId(buyerId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setProductIds(new ArrayList<>());
        cart.setQuantities(new ArrayList<>());
        cartRepository.save(cart);
    }

//
    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        List<CartDto> dtos = new ArrayList<>();
        for (Cart c : carts) {
            dtos.add(mapToDto(c));
        }
        return dtos;
    }
}