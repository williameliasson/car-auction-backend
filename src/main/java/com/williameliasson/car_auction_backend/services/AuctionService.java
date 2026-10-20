package com.williameliasson.car_auction_backend.services;

import com.williameliasson.car_auction_backend.dtos.AuctionCreateRequest;
import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.models.CarMake;
import com.williameliasson.car_auction_backend.models.User;
import com.williameliasson.car_auction_backend.repositories.AuctionRepository;

import com.williameliasson.car_auction_backend.repositories.CarMakeRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service 
public class AuctionService {
    private final CarMakeService carMakeService;
    private final AuctionRepository auctionRepository;

    AuctionService(AuctionRepository auctionRepository, CarMakeRepository carMakeRepository, CarMakeService carMakeService){
        this.auctionRepository = auctionRepository;
        this.carMakeService = carMakeService;
    }

    public void createAuction(User user, AuctionCreateRequest auctionCreateRequest){
        Auction auction = new Auction();
        
        auction.setOwner(user);
        auction.setTitle(auctionCreateRequest.title());
        auction.setDescription(auctionCreateRequest.description());
        auction.setPlate(auctionCreateRequest.plate());
        auction.setDeadline(auctionCreateRequest.deadline());

        CarMake carMake = carMakeService.getCarMakeById(auctionCreateRequest.make_id());
        auction.setMake(carMake);

        auction.setModelYear(auctionCreateRequest.modelYear());
        
        // Todo: add openai request result here. replace the nulls
        auction.setValuationMin(null);
        auction.setValuationMax(null);

        auction.setWinnerHasPaid(false);
        auction.setOdometerKm(auction.getOdometerKm());

        auctionRepository.save(auction);
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
}
