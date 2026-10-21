package com.williameliasson.car_auction_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.williameliasson.car_auction_backend.models.Bid;
import java.util.List;
import com.williameliasson.car_auction_backend.models.User;


public interface BidRepository extends CrudRepository<Bid, Integer> {
    List<Bid> findAllByUser(User user);
}
