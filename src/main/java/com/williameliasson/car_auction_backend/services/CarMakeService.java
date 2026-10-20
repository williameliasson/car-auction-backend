package com.williameliasson.car_auction_backend.services;

import com.williameliasson.car_auction_backend.models.CarMake;
import com.williameliasson.car_auction_backend.repositories.CarMakeRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service 
public class CarMakeService {
    private final CarMakeRepository carMakeRepository;

    CarMakeService(CarMakeRepository carMakeRepository){
        this.carMakeRepository = carMakeRepository;
    }

    public CarMake getCarMakeById(Integer id){
        Optional<CarMake> maybeCarMake = carMakeRepository.findById(id);
        
        if (maybeCarMake.isPresent()){
            return maybeCarMake.get();
        } else {
            throw new IllegalArgumentException("Car make not found");
        }
    }
}
