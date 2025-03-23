package com.example.backend1.service;

import com.example.backend1.model.OrderStatus;

import java.util.List;

public interface IOrderStatusService {
    List<OrderStatus> findAll();
    OrderStatus findById(Long id);
    OrderStatus create(OrderStatus orderStatus);
    OrderStatus update(Long id, OrderStatus orderStatus);
    void delete(Long id);
}
