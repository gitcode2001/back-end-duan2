package com.example.backend1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "delivery_info")
public class DeliveryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false, length = 100)
    private String receiverName;

    @Column(nullable = false, length = 15)
    private String receiverPhone;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String deliveryAddress;

    private LocalDateTime estimatedDelivery;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    public enum DeliveryStatus {
        PENDING, SHIPPING, DELIVERED
    }
}
