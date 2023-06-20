package maruszewski.com.bookstore.service;

import lombok.RequiredArgsConstructor;
import maruszewski.com.bookstore.dtos.OrderStatusDto;
import maruszewski.com.bookstore.errors.ResourceNotFoundException;
import maruszewski.com.bookstore.models.Orders;
import maruszewski.com.bookstore.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Page<Orders> getOrder(Pageable page) {
        return orderRepository.findAll(page);
    }

    public Orders getSingleOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    public Orders updateSingleOrder(Long id, OrderStatusDto orderStatusDto) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        order.setStatus(orderStatusDto.getStatus());

        return orderRepository.save(order);
    }

    public void deleteSingleOrder(long id) {
        orderRepository.deleteById(id);
    }
}

