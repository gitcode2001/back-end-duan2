package com.example.backend1.service;



import com.example.backend1.model.Cart;

import java.util.List;

public interface ICartService {
    List<Cart> getAllCarts();
    Cart getCartById(Long id);
    Cart saveCart(Cart cart);
    void deleteCart(Long id);
    boolean checkoutCart(Long userId);

    List<Cart> getCartsByUserId(Long userId);
}
