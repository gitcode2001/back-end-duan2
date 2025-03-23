package com.example.backend1.controller;


import com.example.backend1.model.OrderStatus;
import com.example.backend1.service.IOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order-status")
public class OrderStatusController {

    private final IOrderStatusService orderStatusService;

    @Autowired
    public OrderStatusController(IOrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @GetMapping
    public ResponseEntity<List<OrderStatus>> getAllOrderStatus() {
        List<OrderStatus> statuses = orderStatusService.findAll();
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatus> getOrderStatusById(@PathVariable Long id) {
        OrderStatus status = orderStatusService.findById(id);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }

    @PostMapping
    public ResponseEntity<OrderStatus> createOrderStatus(@RequestBody OrderStatus orderStatus) {
        OrderStatus created = orderStatusService.create(orderStatus);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderStatus> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus orderStatus) {
        OrderStatus updated = orderStatusService.update(id, orderStatus);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable Long id) {
        orderStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
