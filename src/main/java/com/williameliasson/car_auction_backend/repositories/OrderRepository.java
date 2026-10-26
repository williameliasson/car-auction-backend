package com.williameliasson.car_auction_backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.williameliasson.car_auction_backend.models.Order;
import com.williameliasson.car_auction_backend.models.User;

public interface OrderRepository extends CrudRepository<Order, Integer>{
    List<Order> findByUser(User user);
}
