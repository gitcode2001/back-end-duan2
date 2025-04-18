package com.example.backend1.service.implement;

import com.example.backend1.dto.TopFoodDTO;
import com.example.backend1.model.OrderDetail;
import com.example.backend1.repository.OrderDetailRepository;
import com.example.backend1.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetail> findAll() {
        return (List<OrderDetail>) orderDetailRepository.findAll();
    }

    @Override
    public OrderDetail findById(Long id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    @Override
    public OrderDetail create(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail update(Long id, OrderDetail orderDetail) {
        return orderDetailRepository.findById(id).map(existing -> {
            existing.setQuantity(orderDetail.getQuantity());
            existing.setPrice(orderDetail.getPrice());
            existing.setFood(orderDetail.getFood());
            existing.setOrder(orderDetail.getOrder());
            return orderDetailRepository.save(existing);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<TopFoodDTO> findTopSoldFoods() {
        return orderDetailRepository.findTopSoldFoods();
    }
}
