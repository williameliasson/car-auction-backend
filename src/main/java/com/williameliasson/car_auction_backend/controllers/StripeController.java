package com.williameliasson.car_auction_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williameliasson.car_auction_backend.dtos.StripeCheckoutRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController 
@RequestMapping("/stripe")
public class StripeController {

    @PostMapping("/checkout")
    public String postMethodName(@RequestBody StripeCheckoutRequest stripeCheckoutRequest) {
        
        return "";
    }

    @PostMapping("/webhook")
    public String webhook(String payload, @RequestHeader ("Stripe-Signature") String sigHeader) {
        
        return "";
    }
    
    
}
