package com.example.backend1.controller;

import com.example.backend1.model.Cart;
import com.example.backend1.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private ICartService cartService;

    // Lấy tất cả giỏ hàng
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    // Lấy thông tin giỏ hàng theo id
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Cart cart = cartService.getCartById(id);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Tạo mới giỏ hàng
    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody Cart cart) {
        try {
            Cart savedCart = cartService.saveCart(cart);
            return ResponseEntity.ok(savedCart);
        } catch (IllegalArgumentException e) {
            // Nếu thiếu thông tin bắt buộc, ví dụ food là null
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi tạo giỏ hàng.");
        }
    }

    // Xóa giỏ hàng theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    // Lấy giỏ hàng theo userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Cart>> getCartsByUserId(@PathVariable Long userId) {
        List<Cart> carts = cartService.getCartsByUserId(userId);
        return ResponseEntity.ok(carts);
    }

    // Thanh toán giỏ hàng của một user
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<String> checkoutCart(@PathVariable Long userId) {
        boolean success = cartService.checkoutCart(userId);
        if (success) {
            return ResponseEntity.ok("Checkout successful");
        } else {
            return ResponseEntity.badRequest().body("Checkout failed: No carts found for user");
        }
    }
}
