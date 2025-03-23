package com.example.backend1.service.implement;


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
        return orderDetailRepository.findAll();
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
        OrderDetail existing = orderDetailRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        // Cập nhật các trường cần thiết
        existing.setQuantity(orderDetail.getQuantity());
        existing.setPrice(orderDetail.getPrice());
        existing.setFood(orderDetail.getFood());
        existing.setOrder(orderDetail.getOrder());
        return orderDetailRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
