package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.services.AuthService;
import com.williameliasson.car_auction_backend.services.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.williameliasson.car_auction_backend.dtos.BidCreateRequest;
import com.williameliasson.car_auction_backend.dtos.BidDisplay;
import com.williameliasson.car_auction_backend.models.User;

@RestController 
public class BidController {
    private final BidService bidService;
    private final AuthService authService;

    BidController(AuthService authService, BidService bidService){
        this.authService = authService;
        this.bidService = bidService;
    }

    @PostMapping("/auctions/{auctionId}/bids")
    public ResponseEntity<BidDisplay> createBid(@PathVariable int auctionId, @RequestBody BidCreateRequest bidCreateRequest, Authentication authentication) {
        try {
            User user = authService.getUserByUsername(authentication.getName());
            bidService.createBid(user, bidCreateRequest.amount(), auctionId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e){
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }
    }
}
