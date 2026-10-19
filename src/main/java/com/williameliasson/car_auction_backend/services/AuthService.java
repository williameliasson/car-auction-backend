package com.williameliasson.car_auction_backend.services;

import com.williameliasson.car_auction_backend.repositories.UserRepository;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.williameliasson.car_auction_backend.dtos.LoginRequest;
import com.williameliasson.car_auction_backend.dtos.RegisterRequest;
import com.williameliasson.car_auction_backend.models.User;

@Service 
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Authentication register(RegisterRequest registerRequest){
        // check if the username is taken
        Optional<User> existingUser = userRepository.findByUsername(registerRequest.username());
        if (existingUser.isPresent()) throw new IllegalArgumentException("Username taken");

        // create the User object, and persist it
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        userRepository.save(user);

        // login the user
        return login(new LoginRequest(registerRequest.username(), registerRequest.password()));
    }

    public Authentication login(LoginRequest loginRequest){
        try {
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (Exception e){
            throw new IllegalArgumentException("Incorrect credentials");
        }
    }

    public User getUserByUsername(String username){
        Optional<User> maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isPresent()){
            return maybeUser.get();
        } else {
            throw new IllegalArgumentException("No user found");
        }
    }
}
