package com.williameliasson.car_auction_backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.williameliasson.car_auction_backend.dtos.BidDisplay;
import com.williameliasson.car_auction_backend.exceptions.IllegalBidException;
import com.williameliasson.car_auction_backend.mappers.BidMapper;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.Bid;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.repositories.BidRepository;
import com.williameliasson.car_auction_backend.services.BidService;

@ExtendWith(MockitoExtension.class)
public class BidServiceTests {
    
    @Mock 
    BidMapper bidMapper;
    
    @Mock 
    SimpMessagingTemplate simpMessagingTemplate;
    
    @Mock 
    BidRepository bidRepository;

    @InjectMocks
    BidService bidService;

    @Test 
    void shouldCreateBid(){
        // Arrange
        // setup an auction with one other bid
        User biddingUser = new User();
        biddingUser.setBalance(10_000);

        User otherUser = new User();
        User auctionOwner = new User();
        Auction auction = new Auction();
        auction.setOwner(auctionOwner);
        auction.setDeadline(Instant.now().plusSeconds(86400));
        auction.setWinnerHasPaid(false);
        

        Bid existingHighestBid = new Bid();
        existingHighestBid.setId(1);
        existingHighestBid.setAmount(1000);
        existingHighestBid.setAuction(auction);
        existingHighestBid.setUser(otherUser);
        
        auction.setBids(new ArrayList<>(List.of(existingHighestBid)));

        when(bidMapper.toBidDisplay(any())).thenReturn(new BidDisplay(null, null, null, null));
        
        // this method returns nothing so we use doNothing instead of regular when() like eabove
        doNothing().when(simpMessagingTemplate).convertAndSend(any(String.class), any(BidDisplay.class));

        // Act
        bidService.createBid(biddingUser, existingHighestBid.getAmount() + 1_000, auction); // try to create the new, second bid.
        
        // Assert
        assertEquals(2, auction.getBids().size());
    }

    @Test 
    void shouldNotCreateBidWhenBalanceLowerThanBidAmount(){
        // Arrange
        // setup an auction with one other bid
        User biddingUser = new User();
        biddingUser.setBalance(2_000);

        User otherUser = new User();
        User auctionOwner = new User();
        Auction auction = new Auction();
        auction.setOwner(auctionOwner);
        auction.setDeadline(Instant.now().plusSeconds(86400));
        auction.setWinnerHasPaid(false);
        

        Bid existingHighestBid = new Bid();
        existingHighestBid.setId(1);
        existingHighestBid.setAmount(1000);
        existingHighestBid.setAuction(auction);
        existingHighestBid.setUser(otherUser);
        
        auction.setBids(new ArrayList<>(List.of(existingHighestBid)));

        // Act
        // Assert
        assertThrows(IllegalBidException.class, () -> {
            bidService.createBid(biddingUser, biddingUser.getBalance() + 1_000, auction);// try to create the new, second bid.
        }); 
    }

    @Test 
    void shouldReturnHighestBid(){
        //Arrange   

        Auction auction = new Auction();
        
        // users
        User lowbidder = new User();
        lowbidder.setBalance(1_000);
        
        User highBidder = new User();
        highBidder.setBalance(lowbidder.getBalance() + 1_000);

        // bids by the users
        Bid lowestBid = new Bid();
        lowestBid.setAmount(lowbidder.getBalance());
        lowestBid.setUser(lowbidder);
        lowestBid.setAuction(auction);
        
        Bid highestBid = new Bid();
        highestBid.setAmount(highBidder.getBalance());
        highestBid.setUser(highBidder);
        highestBid.setAuction(auction);

        auction.getBids().add(lowestBid);
        auction.getBids().add(highestBid);

        //act
        Bid returnedHighestBid = bidService.getHighestBid(auction).get();
        
        //assert
        assertEquals(highestBid, returnedHighestBid);

    }
}
