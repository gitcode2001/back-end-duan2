package com.example.backend1.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Order.Status status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime changedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "changed_by")
    private User changedBy;
}
