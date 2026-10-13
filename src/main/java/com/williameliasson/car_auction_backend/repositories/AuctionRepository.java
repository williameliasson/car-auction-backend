package com.williameliasson.car_auction_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.williameliasson.car_auction_backend.models.Auction;

public interface AuctionRepository extends CrudRepository<Auction, Integer>{
}
