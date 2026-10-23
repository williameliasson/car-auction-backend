package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.dtos.UserRewardRequest;
import com.williameliasson.car_auction_backend.services.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/users")
public class UserController {
    

    private final AuthService authService;

    UserController(AuthService authService){
        this.authService = authService;
    }

    // This is only for testing in development, disable in production
    @PostMapping("/{username}/reward") // not super REST-like I know
    public ResponseEntity<String> rewardUserBalance(@PathVariable String username, @RequestBody UserRewardRequest userRewardRequest) {
        try {
            authService.rewardUserBalance(username, userRewardRequest.amount());

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }

    }
    
}
