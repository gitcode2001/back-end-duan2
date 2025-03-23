package com.example.backend1.service;



import com.example.backend1.dto.OrderDTO;
import com.example.backend1.model.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<OrderDTO> getAllOrders();
    Optional<OrderDTO> getOrderById(Long id);
    Order createOrder(Order order);
    Optional<Order> updateOrder(Long id, Order updatedOrder);
    boolean deleteOrder(Long id);
}
