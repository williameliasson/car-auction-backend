package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.services.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity getMyOrders(Authentication authentication) {
        
    }
    
}
