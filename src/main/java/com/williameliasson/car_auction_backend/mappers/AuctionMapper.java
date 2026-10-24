package com.williameliasson.car_auction_backend.mappers;

import com.williameliasson.car_auction_backend.services.AuctionService;
import org.springframework.stereotype.Component;

import com.williameliasson.car_auction_backend.dtos.AuctionDisplay;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.User;


@Component 
public class AuctionMapper {
    
    private final BidMapper bidMapper;

    AuctionMapper(BidMapper bidMapper, AuctionService auctionService){
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
            bidMapper.getBidderHash(auction, auction.getOwner()),
            user == null ? "" : bidMapper.getBidderHash(auction, user),
            auction.getBids().stream().map(
                bid -> {
                    return bidMapper.toBidDisplay(bid);
                }
            ).toList()
        );

    }
}
