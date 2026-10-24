package com.williameliasson.car_auction_backend.services;

import com.williameliasson.car_auction_backend.dtos.AuctionCreateRequest;
import com.williameliasson.car_auction_backend.dtos.ValuationResult;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.Bid;
import com.williameliasson.car_auction_backend.models.CarMake;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.repositories.AuctionRepository;

import com.williameliasson.car_auction_backend.repositories.CarMakeRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service 
public class AuctionService {
    private final OpenAIService openAIService;
    private final CarMakeService carMakeService;
    private final AuctionRepository auctionRepository;

    AuctionService(AuctionRepository auctionRepository, CarMakeRepository carMakeRepository, CarMakeService carMakeService, OpenAIService openAIService){
        this.auctionRepository = auctionRepository;
        this.carMakeService = carMakeService;
        this.openAIService = openAIService;
    }

    public Auction createAuction(User user, AuctionCreateRequest auctionCreateRequest){
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

    public String getBidderHash(Bid bid){
        Auction auction = bid.getAuction();
        User user = bid.getUser();

        return getBidderHash(auction, user);
    }
    
    public String getBidderHash(Auction auction, User user){
        // src: https://www.baeldung.com/java-objects-hash-vs-objects-hashcode
        String hash = String.valueOf(Objects.hash(auction.getId().toString() + "-" + user.getId().toString()));
        return hash;
    }

    public void endAuction(Auction auction){
        // check if winner has already paid, if not, continue

        // get highest bid of the auction

        // subtract bid amount from bidder's balance

        // add bid amount to seller's balance

        // mark auction as paid

    }
}
