package com.example.demo.ECommerce.EControllers;

import com.example.demo.ECommerce.Dtos.CartDto;
import com.example.demo.ECommerce.EServices.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

//
    @GetMapping("/{buyerId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long buyerId) {
        return ResponseEntity.ok(cartService.getCartByBuyer(buyerId));
    }

//
    @PostMapping("/{buyerId}/add")
    public ResponseEntity<CartDto> addProduct(
            @PathVariable Long buyerId,
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam Long societyId  // <-- add this
    ) {
        return ResponseEntity.ok(cartService.addProductToCart(buyerId, productId, quantity, societyId));
    }

//
    @PutMapping("/{buyerId}/update")
    public ResponseEntity<CartDto> updateProduct(@PathVariable Long buyerId, @RequestParam Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateProductQuantity(buyerId, productId, quantity));
    }

//
    @DeleteMapping("/{buyerId}/remove")
    public ResponseEntity<CartDto> removeProduct(@PathVariable Long buyerId, @RequestParam Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(buyerId, productId));
    }

//
    @DeleteMapping("/{buyerId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long buyerId) {
        cartService.clearCart(buyerId);
        return ResponseEntity.ok("Cart cleared successfully");
    }

//
    @GetMapping
    public ResponseEntity<List<CartDto>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }
}