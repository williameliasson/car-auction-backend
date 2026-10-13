package com.williameliasson.car_auction_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.williameliasson.car_auction_backend.models.CarMake;

public interface CarMakeRepository extends CrudRepository<CarMake, Integer>{
}
