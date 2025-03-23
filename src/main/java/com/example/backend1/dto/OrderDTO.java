package com.example.backend1.dto;

import com.example.backend1.model.Order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private Double totalPrice;
    private Order.Status status;
    private Order.PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
    private Long userId;

    public OrderDTO(Order order) {
        if (order != null) {
            this.id = order.getId();
            this.totalPrice = order.getTotalPrice();
            this.status = order.getStatus();
            this.paymentMethod = order.getPaymentMethod();
            this.createdAt = order.getCreatedAt();
            this.userId = (order.getUser() != null) ? order.getUser().getId() : null; // Kiểm tra null
        }
    }
}
