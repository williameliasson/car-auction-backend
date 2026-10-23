package com.williameliasson.car_auction_backend.services;

import com.williameliasson.car_auction_backend.mappers.BidMapper;
import com.williameliasson.car_auction_backend.repositories.BidRepository;

import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.Bid;
import com.williameliasson.car_auction_backend.models.User;

@Service 
public class BidService {
    
    private final BidMapper bidMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final BidRepository bidRepository;
    private final AuctionService auctionService;

    BidService(AuctionService auctionService, BidRepository bidRepository, SimpMessagingTemplate messagingTemplate, BidMapper bidMapper){
        this.auctionService = auctionService;
        this.bidRepository = bidRepository;
        this.messagingTemplate = messagingTemplate;
        this.bidMapper = bidMapper;
    }

    @Transactional 
    public void createBid(User user, Integer amount, Integer auctionId){
        
        Auction auction = auctionService.getAuctionById(auctionId);
        Optional<Bid> highestBid = getHighestBid(auctionId);
        
        // Run all checks

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

        // check the amount being positive nonzero
        if (amount <= 0){
            throw new IllegalArgumentException("Bid amount must be greater than 0");
        }

        // check amount being greater than the current highest bid
        if (highestBid.isPresent() && amount <= highestBid.get().getAmount()){
            throw new IllegalArgumentException("Bid amount must be larger than current highest bid");
        }

        // check if user has available balance to the requested amount to bid
        if (getAvailableBalance(user) - amount < 0){
            throw new IllegalArgumentException("Cannot bid more than your total available balance");
        }
        
        // Unsure about this check. Having it included simplifies the situation where the user wants to outbid oneself,
        // as you would need to have a separate method for getting the available balance for each combination of user and auction.
        if (highestBid.isPresent() && highestBid.get().getUser().equals(user)){
            throw new IllegalArgumentException("Cannot outbid yourself");
        }

        // Create the Bid object and persist it
        Bid bid = new Bid();
        bid.setUser(user);
        bid.setAuction(auction);
        bid.setAmount(amount);
        bid.setCreatedAt(Instant.now());
        broadcastNewBid(bid);
        bidRepository.save(bid);
    }

    public Optional<Bid> getHighestBid(Integer auctionId){
        Auction auction = auctionService.getAuctionById(auctionId);
        
        // src: https://www.geeksforgeeks.org/java/stream-max-method-java-examples/
        // get highest bid
        Optional<Bid> highestBid = auction.getBids().stream()
            .max(Comparator.comparing(bid -> bid.getAmount()));
        return highestBid;

    }

    // get list of bids that User user has placed, on ongoing auctions, where User user has the currently highest bid
    public List<Bid> getActiveHighestBids(User user){
        List<Bid> bids = bidRepository.findAllByUser(user);
        bids = bids.stream()
            .filter(bid -> {
                Auction auction = bid.getAuction();
                Optional<Bid> highestBid = getHighestBid(auction.getId());
                if (auction.isWinnerHasPaid() || // winner already paid
                    (highestBid.isPresent() && !highestBid.get().equals(bid) ) // a highest bid even exists AND this bid is not it
                    // auction.getDeadline().isBefore(Instant.now()) // past deadline
                ){
                    return false;
                }
                return true;
            }
            )
            .toList();
        return bids;
    }

    public Integer getAvailableBalance(User user){
        Integer lockedBalance = getActiveHighestBids(user).stream().mapToInt(bid -> bid.getAmount()).sum();
        return user.getBalance() - lockedBalance;
    }

    public void broadcastNewBid(Bid bid){
        messagingTemplate.convertAndSend("/topic/auction/" + bid.getAuction().getId() + "/newBid", bidMapper.toBidDisplay(bid));
    }
}
