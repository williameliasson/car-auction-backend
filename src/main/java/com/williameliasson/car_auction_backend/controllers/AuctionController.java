package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.dtos.AuctionDisplay;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.services.AuctionService;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController 
@RequestMapping("/auctions")
public class AuctionController {
    private final AuctionService auctionService;

    
    AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }
    
    //c

    //r
    @GetMapping("")
    public List<AuctionDisplay> getAllAuctions() {
        List<Auction> auctions = auctionService.getAllAuctions();
        List<AuctionDisplay> auctionDisplays = new ArrayList<>();
        auctions.stream()
            .map(auction -> 
                new AuctionDisplay(
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

    //u

    //d
}
