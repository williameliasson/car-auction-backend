package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.dtos.AuctionCreateRequest;
import com.williameliasson.car_auction_backend.dtos.AuctionDisplay;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.services.AuctionService;
import com.williameliasson.car_auction_backend.services.AuthService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController 
@RequestMapping("/auctions")
public class AuctionController {
    private final AuthService authService;
    private final AuctionService auctionService;

    
    AuctionController(AuctionService auctionService, AuthService authService) {
        this.auctionService = auctionService;
        this.authService = authService;
        
    }
    
    //c
    @PostMapping("")
    public ResponseEntity<Void> createAuction(@Valid @RequestBody AuctionCreateRequest auctionCreateRequest, Authentication authentication) {
        try {
            User user = authService.getUserByUsername(authentication.getName());
            auctionService.createAuction(user, auctionCreateRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    

    //r
    @GetMapping("")
    public List<AuctionDisplay> getAllAuctions() {
        List<Auction> auctions = auctionService.getAllAuctions();
        List<AuctionDisplay> auctionDisplays = new ArrayList<>();
        auctionDisplays = auctions.stream()
            .map(auction -> 
                new AuctionDisplay(
                    auction.getId(),
                    auction.getTitle(),
                    auction.getDescription(),
                    auction.getPlate(),
                    auction.getDeadline(),
                    // auction.getMakeId()
                    auction.getMake().getName(),
                    auction.getModelYear(),
                    auction.getOdometerKm()
                )
            )
            .toList();
        return auctionDisplays;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDisplay> getById(@PathVariable int id) {
        Auction auction = auctionService.getAuctionById(id);
        AuctionDisplay auctionDisplay = new AuctionDisplay(
                    auction.getId(),
                    auction.getTitle(),
                    auction.getDescription(),
                    auction.getPlate(),
                    auction.getDeadline(),
                    // auction.getMakeId()
                    auction.getMake().getName(),
                    auction.getModelYear(),
                    auction.getOdometerKm()
                );
        return ResponseEntity.ok().body(auctionDisplay);
    }
    

    //u

    //d
}
