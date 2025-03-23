package com.example.backend1.service.implement;

import com.example.backend1.model.Cart;
import com.example.backend1.repository.CartRepository;
import com.example.backend1.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    @Override
    public Cart saveCart(Cart cart) {
        try {
            System.out.println("🛒 Đang lưu giỏ hàng: " + cart);
            return cartRepository.save(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    // ✅ Thêm chức năng thanh toán giỏ hàng
    public boolean checkoutCart(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        if (carts.isEmpty()) {
            return false; // Không có sản phẩm trong giỏ hàng
        }

        // Xử lý thanh toán (giả lập)
        for (Cart cart : carts) {
            System.out.println("✅ Thanh toán sản phẩm: " + cart.getFood().getName());
        }

        // Xóa giỏ hàng sau khi thanh toán thành công
        cartRepository.deleteAll(carts);
        return true;
    }

    @Override
    public List<Cart> getCartsByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
