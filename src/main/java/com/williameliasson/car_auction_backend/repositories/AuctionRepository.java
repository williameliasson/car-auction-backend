package com.williameliasson.car_auction_backend.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.williameliasson.car_auction_backend.models.Auction;

public interface AuctionRepository extends CrudRepository<Auction, Integer>{
    List<Auction> findAll();
    List<Auction> findAllByDeadlineBeforeAndWinnerHasPaid(Instant now, boolean winnerHasPaid);
}
