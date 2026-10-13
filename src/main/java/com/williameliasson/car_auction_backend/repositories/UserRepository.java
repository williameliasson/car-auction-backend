package com.williameliasson.car_auction_backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.williameliasson.car_auction_backend.models.User;

public interface UserRepository extends CrudRepository<User, Integer>{
    Optional<User> findByUsername(String username);
}
