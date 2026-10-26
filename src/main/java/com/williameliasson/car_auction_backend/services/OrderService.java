package com.williameliasson.car_auction_backend.services;

import com.williameliasson.car_auction_backend.repositories.OrderRepository;
import com.williameliasson.car_auction_backend.repositories.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.williameliasson.car_auction_backend.models.Order;
import com.williameliasson.car_auction_backend.models.User;

import jakarta.transaction.Transactional;

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
    public Order createOrder(Integer amountSek, User user){
        Order order = new Order();
        
        order.setAmountSek(amountSek);
        order.setUser(user);
        order.setPaid(false);
        
        orderRepository.save(order);
        return order;
    }

    public List<Order> getOrdersByUser(User user){
        return orderRepository.findByUser(user);
    }

    public Order getOrderById(Integer id){
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No order found"));
    }

    @Transactional 
    public void markAsPaid(Order order){
        User user = order.getUser();
        
        user.setBalance(user.getBalance() + order.getAmountSek());
        order.setPaid(true);
        System.out.println("marking as paid order #" + order.getId().toString());
        
        userRepository.save(user);
        orderRepository.save(order);
    }
}
