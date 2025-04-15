package com.example.backend1.service.implement;

import com.example.backend1.dto.OrderDTO;
import com.example.backend1.model.Order;
import com.example.backend1.repository.OrderRepository;
import com.example.backend1.service.IOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> getOrderById(Long id) {
        return orderRepository.findById(id).map(OrderDTO::new);
    }

    @Override
    public Order createOrder(Order order) {
        if (order == null || order.getUser() == null || order.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu đơn hàng không hợp lệ!");
        }
        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Không thể lưu đơn hàng, vui lòng thử lại!", e);
        }
    }

    @Override
    public Optional<Order> updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id).map(order -> {
            if (updatedOrder.getTotalPrice() != null) {
                order.setTotalPrice(updatedOrder.getTotalPrice());
            }
            if (updatedOrder.getStatus() != null) {
                order.setStatus(updatedOrder.getStatus());
            }
            if (updatedOrder.getPaymentMethod() != null) {
                order.setPaymentMethod(updatedOrder.getPaymentMethod());
            }
            return orderRepository.save(order);
        });
    }

    @Override
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
