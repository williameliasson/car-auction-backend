package com.williameliasson.car_auction_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.williameliasson.car_auction_backend.models.Bid;

public interface BidRepository extends CrudRepository<Bid, Integer> {
}
