package com.williameliasson.car_auction_backend.services;

import com.williameliasson.car_auction_backend.models.Auction;
import com.williameliasson.car_auction_backend.repositories.AuctionRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service 
public class AuctionService {
    private final AuctionRepository auctionRepository;

    AuctionService(AuctionRepository auctionRepository){
        this.auctionRepository = auctionRepository;
    }

    public List<Auction> getAllAuctions(){
        return auctionRepository.findAll();
    }
}
