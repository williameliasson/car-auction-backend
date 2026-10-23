package com.williameliasson.car_auction_backend.mappers;

import com.williameliasson.car_auction_backend.services.AuctionService;
import org.springframework.stereotype.Component;

import com.williameliasson.car_auction_backend.dtos.BidDisplay;
import com.williameliasson.car_auction_backend.models.Bid;

@Component 
public class BidMapper {
    
    private final AuctionService auctionService;

    BidMapper(AuctionService auctionService){
        this.auctionService = auctionService;
    }

    public BidDisplay toBidDisplay(Bid bid){
        return new BidDisplay(
            bid.getAuction().getId(),
            bid.getId(),
            auctionService.getBidderHash(bid),
            bid.getAmount()
        );
    }
}
