package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.dtos.AuctionCreateRequest;
import com.williameliasson.car_auction_backend.dtos.AuctionDisplay;
import com.williameliasson.car_auction_backend.dtos.BidCreateRequest;
import com.williameliasson.car_auction_backend.dtos.BidDisplay;
import com.williameliasson.car_auction_backend.dtos.ValuationResult;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.services.AuctionService;
import com.williameliasson.car_auction_backend.services.AuthService;

import com.williameliasson.car_auction_backend.services.BidService;
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
    private final BidService bidService;
    private final OpenAIService openAIService;
    private final AuthService authService;
    private final AuctionService auctionService;

    
    AuctionController(AuctionService auctionService, AuthService authService, OpenAIService openAIService, BidService bidService) {
        this.auctionService = auctionService;
        this.authService = authService;
        this.openAIService = openAIService;
        this.bidService = bidService;
        
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
                auction.getMake().getName(),
                auction.getModelYear(),
                auction.getOdometerKm(),
                auction.getValuationMin(),
                auction.getValuationMax(),
                true
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(auctionDisplay);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }
    }
    

    //r
    @GetMapping("")
    public List<AuctionDisplay> getAllAuctions(Authentication authentication) {
        List<Auction> auctions = auctionService.getAllAuctions();
        List<AuctionDisplay> auctionDisplays = new ArrayList<>();
        
        // being authenticated is optional, but gives additional info. in this case, whether the authed user is the owner of any auction
        // if the user is logged in, set the User user variable to the current user. Otherwise, set it to null.
        User user = authentication == null ? null : authService.getUserByUsername(authentication.getName());

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
                    auction.getOdometerKm(),
                    auction.getValuationMin(),
                    auction.getValuationMax(),
                    user == null ? false : auction.getOwner().equals(user) // if the user isnt logged in, default to showing it as not owned by the user. Otherwise, perform a check to see if the user is owner of this auction.
                )
            )
            .toList();
        return auctionDisplays;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDisplay> getById(@PathVariable int id, Authentication authentication) {
        User user = authentication == null ? null : authService.getUserByUsername(authentication.getName());
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
                    auction.getOdometerKm(),
                    auction.getValuationMin(),
                    auction.getValuationMax(),
                    user == null ? false : auction.getOwner().equals(user) // if the user isnt logged in, default to showing it as not owned by the user. Otherwise, perform a check to see if the user is owner of this auction.
                );
        return ResponseEntity.ok().body(auctionDisplay);
    }

    @GetMapping("/{id}/valuation")
    public ResponseEntity<ValuationResult> getValuation(@PathVariable int id) {
        Auction auction = auctionService.getAuctionById(id);
        return ResponseEntity.ok().body(openAIService.getValuation(auction));
    }
    
    
    @PostMapping("/{auctionId}/bids")
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
    

    //u

    //d
}
