package com.williameliasson.car_auction_backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.williameliasson.car_auction_backend.dtos.RegisterRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
public class AuthController {
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        
        return ;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String entity) {

        return ;
    }
    
}
