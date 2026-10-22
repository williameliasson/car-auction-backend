package com.williameliasson.car_auction_backend.mappers;

import org.springframework.stereotype.Component;

import com.williameliasson.car_auction_backend.dtos.AuctionDisplay;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.User;


@Component 
public class AuctionMapper {
    
    private final BidMapper bidMapper;

    AuctionMapper(BidMapper bidMapper){
        this.bidMapper = bidMapper;
    }

    public AuctionDisplay toAuctionDisplay(Auction auction, User user) {
        return new AuctionDisplay(
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
            user == null ? false : auction.getOwner().equals(user), // if the user isnt logged in, default to showing it as not owned by the user. Otherwise, perform a check to see if the user is owner of this auction.
            auction.getBids().stream().map(bid -> bidMapper.toBidDisplay(bid)).toList()
        );

    }
}
