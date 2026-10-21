package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.dtos.AuctionCreateRequest;
import com.williameliasson.car_auction_backend.dtos.AuctionDisplay;
import com.williameliasson.car_auction_backend.dtos.ValuationResult;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.services.AuctionService;
import com.williameliasson.car_auction_backend.services.AuthService;

import com.williameliasson.car_auction_backend.services.OpenAIService;
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
import org.springframework.web.bind.annotation.RequestParam;





@RestController 
@RequestMapping("/auctions")
public class AuctionController {
    private final OpenAIService openAIService;
    private final AuthService authService;
    private final AuctionService auctionService;

    
    AuctionController(AuctionService auctionService, AuthService authService, OpenAIService openAIService) {
        this.auctionService = auctionService;
        this.authService = authService;
        this.openAIService = openAIService;
        
    }
    
    //c
    @PostMapping("")
    public ResponseEntity<AuctionDisplay> createAuction(@Valid @RequestBody AuctionCreateRequest auctionCreateRequest, Authentication authentication) {
        try {
            User user = authService.getUserByUsername(authentication.getName());
            Auction auction = auctionService.createAuction(user, auctionCreateRequest);
            AuctionDisplay auctionDisplay = new AuctionDisplay(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getPlate(),
                auction.getDeadline(),
                "Volvo temp temp",
                auction.getModelYear(),
                auction.getOdometerKm()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(auctionDisplay);
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

    @GetMapping("/{id}/valuation")
    public ResponseEntity<ValuationResult> getValuation(@PathVariable int id) {
        Auction auction = auctionService.getAuctionById(id);
        return ResponseEntity.ok().body(openAIService.getValuation(auction));
    }
    
    

    //u

    //d
}
