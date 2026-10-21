package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.repositories.CarMakeRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williameliasson.car_auction_backend.dtos.CarMakeDisplay;
import com.williameliasson.car_auction_backend.models.CarMake;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("/carmakes")
public class CarMakeController {
    
    private final CarMakeRepository carMakeRepository;

    CarMakeController(CarMakeRepository carMakeRepository){
        this.carMakeRepository = carMakeRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<CarMakeDisplay>> getAllCarMakes() {
        List<CarMake> carMakes = carMakeRepository.findAll();
        List<CarMakeDisplay> carMakeDisplays = carMakes.stream()
            .map(carMake -> {
                return new CarMakeDisplay(
                    carMake.getId(),
                    carMake.getName()
                );
            }).toList();
        return ResponseEntity.ok().body(carMakeDisplays);
    }
    
}
