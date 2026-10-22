package com.williameliasson.car_auction_backend.mappers;

import com.williameliasson.car_auction_backend.services.BidService;
import org.springframework.stereotype.Component;

import com.williameliasson.car_auction_backend.dtos.BidDisplay;
import com.williameliasson.car_auction_backend.models.Bid;

@Component 
public class BidMapper {
    
    private final BidService bidService;

    BidMapper(BidService bidService){
        this.bidService = bidService;
    }

    public BidDisplay toBidDisplay(Bid bid){
        return new BidDisplay(
            bid.getAuction().getId(),
            bid.getId(),
            bidService.getBidderHash(bid),
            bid.getAmount()
        );
    }
}
