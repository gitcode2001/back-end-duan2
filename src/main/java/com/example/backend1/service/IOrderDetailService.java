package com.example.backend1.service;

import com.example.backend1.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    List<OrderDetail> findAll();
    OrderDetail findById(Long id);
    OrderDetail create(OrderDetail orderDetail);
    OrderDetail update(Long id, OrderDetail orderDetail);
    void delete(Long id);
}
