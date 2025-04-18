package com.example.backend1.controller;

import com.example.backend1.dto.TopFoodDTO;
import com.example.backend1.model.OrderDetail;
import com.example.backend1.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
@CrossOrigin("*")
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;

    @Autowired
    public OrderDetailController(IOrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails() {
        List<OrderDetail> details = orderDetailService.findAll();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable Long id) {
        OrderDetail detail = orderDetailService.findById(id);
        return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<OrderDetail> createOrderDetail(@RequestBody OrderDetail orderDetail) {
        return ResponseEntity.ok(orderDetailService.create(orderDetail));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetail> updateOrderDetail(@PathVariable Long id, @RequestBody OrderDetail orderDetail) {
        OrderDetail updated = orderDetailService.update(id, orderDetail);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable Long id) {
        orderDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top-sold")
    public ResponseEntity<List<TopFoodDTO>> getTopSoldFoods() {
        List<TopFoodDTO> topFoods = orderDetailService.findTopSoldFoods();
        return topFoods.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(topFoods);
    }
}
