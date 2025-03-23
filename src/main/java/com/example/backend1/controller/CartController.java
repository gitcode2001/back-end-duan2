package com.example.backend1.controller;

import com.example.backend1.model.Cart;
import com.example.backend1.model.Food;
import com.example.backend1.model.User;
import com.example.backend1.repository.FoodRepository;
import com.example.backend1.repository.UserRepository;
import com.example.backend1.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private ICartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    // API lấy tất cả giỏ hàng
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        if (carts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(carts);
    }

    // API lấy giỏ hàng theo id
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Cart cart = cartService.getCartById(id);
        return cart != null ? ResponseEntity.ok(cart) : ResponseEntity.notFound().build();
    }

    // API tạo giỏ hàng mới
    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        if (cart.getUser() == null || cart.getFood() == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findById(cart.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Food food = foodRepository.findById(cart.getFood().getId())
                .orElseThrow(() -> new RuntimeException("Food không tồn tại"));

        cart.setUser(user);
        cart.setFood(food);

        Cart savedCart = cartService.saveCart(cart);
        return ResponseEntity.ok(savedCart);
    }

    // API cập nhật giỏ hàng theo id
    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long id, @RequestBody Cart cart) {
        Cart existingCart = cartService.getCartById(id);
        if (existingCart != null) {
            existingCart.setQuantity(cart.getQuantity());
            existingCart.setNote(cart.getNote());
            Cart updatedCart = cartService.saveCart(existingCart);
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.notFound().build();
    }

    // API xóa giỏ hàng theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        Cart existingCart = cartService.getCartById(id);
        if (existingCart != null) {
            cartService.deleteCart(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // API thanh toán giỏ hàng theo userId
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<String> checkoutCart(@PathVariable Long userId) {
        boolean success = cartService.checkoutCart(userId);
        if (success) {
            return ResponseEntity.ok("🛒 Thanh toán giỏ hàng thành công!");
        } else {
            return ResponseEntity.badRequest().body("⚠️ Giỏ hàng trống hoặc không tồn tại!");
        }
    }

    // API lấy giỏ hàng theo userId (sửa mapping thành /api/carts/user)
    @GetMapping("/user")
    public ResponseEntity<List<Cart>> getCartsByUserId(@RequestParam Long userId) {
        List<Cart> carts = cartService.getCartsByUserId(userId);
        if (carts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(carts);
    }
}
