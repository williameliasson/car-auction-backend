package com.williameliasson.car_auction_backend.services;

import java.time.Instant;
import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.Bid;
import com.williameliasson.car_auction_backend.models.User;

@Service 
public class BidService {
    
    private final AuctionService auctionService;

    BidService(AuctionService auctionService){
        this.auctionService = auctionService;
    }

    public void createBid(User user, Integer amount, Integer auctionId){
        
        Auction auction = auctionService.getAuctionById(auctionId);
        
        // check if user is the owner of the auction
        if (auction.getOwner().equals(user)){
            throw new IllegalArgumentException("Cannot bid on your own auction");
        }

        // check if auction is past deadline
        if (auction.getDeadline().isBefore(Instant.now())){
            throw new IllegalArgumentException("Cannot bid on already finished auction");
        }

        // if the auction is ended prematurely and winner pays, no bids should be able to be placed
        if (auction.isWinnerHasPaid()){
            throw new IllegalArgumentException("Cannot bid on finished auction, winner has already paid");
        }

        // check if user has available balance greather than or equal to the requested amount to bid
        




        // Unsure about this check
        // if (getHighestBid(auctionId).getUser().equals(user)){
        //     throw new IllegalArgumentException("Cannot outbid yourself");
        // }

    }

    public Bid getHighestBid(Integer auctionId){
        Auction auction = auctionService.getAuctionById(auctionId);
        
        // src: https://www.geeksforgeeks.org/java/stream-max-method-java-examples/
        // get highest bid
        Bid highestBid = auction.getBids().stream()
            .max(Comparator.comparing(bid -> bid.getAmount()))
            .get();
        return highestBid;

    }
}
