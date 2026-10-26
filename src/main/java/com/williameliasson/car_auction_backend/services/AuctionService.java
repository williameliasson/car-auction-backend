package com.williameliasson.car_auction_backend.services;

import com.williameliasson.car_auction_backend.dtos.AuctionCreateRequest;
import com.williameliasson.car_auction_backend.dtos.ValuationResult;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.Bid;
import com.williameliasson.car_auction_backend.models.CarMake;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.repositories.AuctionRepository;

import com.williameliasson.car_auction_backend.repositories.CarMakeRepository;

import com.williameliasson.car_auction_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service 
public class AuctionService {
    private final UserRepository userRepository;
    private final BidService bidService;
    private final OpenAIService openAIService;
    private final CarMakeService carMakeService;
    private final AuctionRepository auctionRepository;

    AuctionService(AuctionRepository auctionRepository, CarMakeRepository carMakeRepository, CarMakeService carMakeService, OpenAIService openAIService, BidService bidService, UserRepository userRepository){
        this.auctionRepository = auctionRepository;
        this.carMakeService = carMakeService;
        this.openAIService = openAIService;
        this.bidService = bidService;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 10_000)
    public void endAuctionsPastDeadline(){
        // get all where the winner has not paid yet, and the deadline is before now.
        List<Auction> auctions = auctionRepository.findAllByDeadlineBeforeAndWinnerHasPaid(Instant.now(), false);
        auctions.forEach(a -> endAuction(a));

    }

    public Auction createAuction(User user, AuctionCreateRequest auctionCreateRequest){
        
        // Check validity of request
        if (auctionCreateRequest.title().isBlank()){
            throw new IllegalArgumentException("Cannot have blank title on auction");
        }
        if (auctionCreateRequest.deadline().isBefore(Instant.now())){
            throw new IllegalArgumentException("Cannot have deadline before now on new auction");
        }

        Auction auction = new Auction();
        
        auction.setOwner(user);
        auction.setTitle(auctionCreateRequest.title());
        auction.setDescription(auctionCreateRequest.description());
        auction.setPlate(auctionCreateRequest.plate());
        auction.setDeadline(auctionCreateRequest.deadline());

        CarMake carMake = carMakeService.getCarMakeById(auctionCreateRequest.make_id());
        auction.setMake(carMake);

        auction.setModelYear(auctionCreateRequest.modelYear());
        
        ValuationResult valuationResult = openAIService.getValuation(auction);
        auction.setValuationMin(valuationResult.valuationMin());
        auction.setValuationMax(valuationResult.valuationMax());

        auction.setWinnerHasPaid(false);
        auction.setOdometerKm(auctionCreateRequest.odometerKm());

        auctionRepository.save(auction);
        return auction;
    }

    public List<Auction> getAllAuctions(){
        return auctionRepository.findAll();
    }

    public Auction getAuctionById(int id){
        Optional<Auction> maybeAuction = auctionRepository.findById(id);
        if (!maybeAuction.isPresent()){
            throw new IllegalArgumentException("Auction not found");
        }
        return maybeAuction.get();
    }

    @Transactional 
    public void endAuction(Auction auction){
        // check if winner has already paid, if not, continue
        if (auction.isWinnerHasPaid()){
            throw new IllegalArgumentException("Winner has already paid, cannot end auction");
        }

        // get highest bid of the auction
        Optional<Bid> maybeHighestBid = bidService.getHighestBid(auction);

        // if there is at least one bid, do the transfer.
        if (maybeHighestBid.isPresent()){
            Bid highestBid = maybeHighestBid.get();
            User seller = auction.getOwner();
            User buyer = highestBid.getUser();
    
            // subtract bid amount from bidder's balance
            buyer.setBalance(buyer.getBalance() - highestBid.getAmount()); // should probably have a dedicated method for transfers
    
            // add bid amount to seller's balance
            seller.setBalance(seller.getBalance() + highestBid.getAmount());
            userRepository.save(buyer);
            userRepository.save(seller);
        }

        // mark auction as paid
        auction.setWinnerHasPaid(true);
        auctionRepository.save(auction);
    }
}
