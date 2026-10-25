package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.dtos.OrderDisplay;
import com.williameliasson.car_auction_backend.mappers.OrderMapper;
import com.williameliasson.car_auction_backend.models.Order;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.services.AuthService;
import com.williameliasson.car_auction_backend.services.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderMapper orderMapper;
    private final AuthService authService;
    private final OrderService orderService;

    public OrderController(OrderService orderService, AuthService authService, OrderMapper orderMapper){
        this.orderService = orderService;
        this.authService = authService;
        this.orderMapper = orderMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<OrderDisplay>> getMyOrders(Authentication authentication) {
        try{
            User user = authService.getUserByUsername(authentication.getName());
            List<Order> orders = orderService.getOrdersByUser(user);
            List<OrderDisplay> orderDisplays = orders.stream()
                .map(o -> orderMapper.toOrderDisplay(o))
                .toList();
            return ResponseEntity.ok().body(orderDisplays);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    
}
