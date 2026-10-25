package com.williameliasson.car_auction_backend.services;

import com.williameliasson.car_auction_backend.repositories.OrderRepository;
import com.williameliasson.car_auction_backend.repositories.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.williameliasson.car_auction_backend.dtos.OrderCreateRequest;
import com.williameliasson.car_auction_backend.models.Order;
import com.williameliasson.car_auction_backend.models.User;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service 
@Validated 
public class OrderService {
    
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    OrderService(OrderRepository orderRepository, UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional 
    public void createOrder(@Valid OrderCreateRequest orderCreateRequest){
        Order order = new Order();
        User user = userRepository.findById(orderCreateRequest.userId()).orElseThrow(() -> new IllegalArgumentException("No user found"));
        
        order.setAmountSek(orderCreateRequest.amountSek());
        order.setUser(user);
        order.setPaid(false);
        
        orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user){
        return orderRepository.findByUser(user);
    }
}
