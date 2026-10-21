package com.williameliasson.car_auction_backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.williameliasson.car_auction_backend.models.CarMake;

public interface CarMakeRepository extends CrudRepository<CarMake, Integer>{
    List<CarMake> findAll();
}
