package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.mappers.BidMapper;
import com.williameliasson.car_auction_backend.services.AuctionService;
import com.williameliasson.car_auction_backend.services.AuthService;
import com.williameliasson.car_auction_backend.services.BidService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.williameliasson.car_auction_backend.dtos.BidCreateRequest;
import com.williameliasson.car_auction_backend.dtos.BidDisplay;
import com.williameliasson.car_auction_backend.models.Bid;
import com.williameliasson.car_auction_backend.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController 
public class BidController {
    private final BidMapper bidMapper;
    private final AuctionService auctionService;
    private final BidService bidService;
    private final AuthService authService;

    BidController(AuthService authService, BidService bidService, AuctionService auctionService, BidMapper bidMapper){
        this.authService = authService;
        this.bidService = bidService;
        this.auctionService = auctionService;
        this.bidMapper = bidMapper;
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

    @GetMapping("/auctions/{auctionId}/bids")
    public ResponseEntity<List<BidDisplay>> getAllBids(@PathVariable int auctionId) {
        List<Bid> bids = auctionService.getAuctionById(auctionId).getBids();
        List<BidDisplay> bidDisplays = bids.stream()
            .map(bid -> bidMapper.toBidDisplay(bid))
            .toList();
        return ResponseEntity.ok().body(bidDisplays);
    }
    
}
