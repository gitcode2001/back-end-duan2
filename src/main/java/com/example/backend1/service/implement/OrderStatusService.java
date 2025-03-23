package com.example.backend1.service.implement;

import com.example.backend1.model.OrderStatus;
import com.example.backend1.repository.OrderStatusRepository;
import com.example.backend1.service.IOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderStatusService implements IOrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    @Autowired
    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }
    @Override
    public List<OrderStatus> findAll() {
        return orderStatusRepository.findAll();
    }
    @Override
    public OrderStatus findById(Long id) {
        return orderStatusRepository.findById(id).orElse(null);
    }
    @Override
    public OrderStatus create(OrderStatus orderStatus) {
        return orderStatusRepository.save(orderStatus);
    }
    @Override
    public OrderStatus update(Long id, OrderStatus orderStatus) {
        OrderStatus existing = orderStatusRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setStatus(orderStatus.getStatus());
        existing.setChangedBy(orderStatus.getChangedBy());
        existing.setChangedAt(orderStatus.getChangedAt());
        existing.setOrder(orderStatus.getOrder());
        return orderStatusRepository.save(existing);
    }
    @Override
    public void delete(Long id) {
        orderStatusRepository.deleteById(id);
    }
}
