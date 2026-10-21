package com.williameliasson.car_auction_backend.controllers;

import com.williameliasson.car_auction_backend.services.AuthService;

import com.williameliasson.car_auction_backend.services.BidService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.RestController;

import com.williameliasson.car_auction_backend.dtos.LoginRequest;
import com.williameliasson.car_auction_backend.dtos.RegisterRequest;
import com.williameliasson.car_auction_backend.dtos.UserDTO;
import com.williameliasson.car_auction_backend.models.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController 
public class AuthController {
    
    private final BidService bidService;
    private final SecurityContextRepository securityContextRepository;
    private final AuthService authService;

    AuthController(AuthService authService, SecurityContextRepository securityContextRepository, BidService bidService){
        this.authService = authService;
        this.securityContextRepository = securityContextRepository;
        this.bidService = bidService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request, HttpServletResponse response) {    
        try {
            Authentication auth = authService.register(registerRequest);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
    
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

      try {
            // authenticate
            Authentication auth = authService.login(loginRequest);
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        if (authentication == null) return ResponseEntity.badRequest().build();
        try {
            User user = authService.getUserByUsername(authentication.getName());
            return ResponseEntity.ok().body(new UserDTO(user.getUsername(), user.getEmail(), user.getBalance(), bidService.getAvailableBalance(user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        
    }
    
    
}
