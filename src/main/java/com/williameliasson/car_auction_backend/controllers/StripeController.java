package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.services.OrderService;
import com.williameliasson.car_auction_backend.services.StripeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williameliasson.car_auction_backend.dtos.CheckoutResponse;
import com.williameliasson.car_auction_backend.dtos.StripeCheckoutRequest;
import com.williameliasson.car_auction_backend.models.Order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController 
@RequestMapping("/stripe")
public class StripeController {

    private final OrderService orderService;
    private final StripeService stripeService;

    StripeController(StripeService stripeService, OrderService orderService){
        this.stripeService = stripeService;
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> createCheckout(@RequestBody StripeCheckoutRequest stripeCheckoutRequest) {
        try {
            Order order = orderService.getOrderById(stripeCheckoutRequest.orderId());
            String checkoutUrl = stripeService.createCheckoutSession(order);
            return ResponseEntity.ok().body(new CheckoutResponse(checkoutUrl));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(String payload, @RequestHeader ("Stripe-Signature") String sigHeader) {
        try {
            stripeService.handleWebhook(payload, sigHeader);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
