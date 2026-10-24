package com.williameliasson.car_auction_backend.mappers;

import com.williameliasson.car_auction_backend.services.AuctionService;
import org.springframework.stereotype.Component;

import com.williameliasson.car_auction_backend.dtos.AuctionDisplay;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.User;


@Component 
public class AuctionMapper {
    
    private final AuctionService auctionService;
    private final BidMapper bidMapper;

    AuctionMapper(BidMapper bidMapper, AuctionService auctionService){
        this.bidMapper = bidMapper;
        this.auctionService = auctionService;
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
            auctionService.getBidderHash(auction, auction.getOwner()),
            user == null ? "" : auctionService.getBidderHash(auction, user),
            auction.getBids().stream().map(bid -> bidMapper.toBidDisplay(bid)).toList()
        );

    }
}
