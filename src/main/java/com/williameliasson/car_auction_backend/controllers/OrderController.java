package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.dtos.OrderCreateRequest;
import com.williameliasson.car_auction_backend.dtos.OrderDisplay;
import com.williameliasson.car_auction_backend.mappers.OrderMapper;
import com.williameliasson.car_auction_backend.models.Order;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.services.AuthService;
import com.williameliasson.car_auction_backend.services.OrderService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @GetMapping("/{id}")
    public ResponseEntity<OrderDisplay> getOrderById(@PathVariable int id, Authentication authentication) {
        try {
            User user = authService.getUserByUsername(authentication.getName());
            Order order = orderService.getOrderById(id);
            if (!order.getUser().equals(user)){
                throw new IllegalArgumentException("Requesting user is not owner of order");
            }
            return ResponseEntity.ok().body(orderMapper.toOrderDisplay(order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    

    @PostMapping("")
    public ResponseEntity<OrderDisplay> createOrder(@Valid @RequestBody OrderCreateRequest orderCreateRequest, Authentication authentication) {
        try {
            User user = authService.getUserByUsername(authentication.getName());
            Order order = orderService.createOrder(orderCreateRequest.amountSek(), user);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toOrderDisplay(order));

        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    
    
}
