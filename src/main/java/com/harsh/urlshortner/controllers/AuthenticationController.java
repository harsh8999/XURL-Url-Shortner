package com.harsh.urlshortner.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harsh.urlshortner.entity.User;
import com.harsh.urlshortner.models.LoginResponse;
import com.harsh.urlshortner.models.LoginUserDto;
import com.harsh.urlshortner.models.RegisterUserDto;
import com.harsh.urlshortner.security.service.JwtService;
import com.harsh.urlshortner.service.UserAuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    
    private final JwtService jwtService;
    private final UserAuthenticationService userAuthenticationService;

    public AuthenticationController(JwtService jwtService, UserAuthenticationService userAuthenticationService) {
        this.jwtService = jwtService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserDto entity) {
        Optional<User> optionalUser = userAuthenticationService.signUp(entity);
        return optionalUser.map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
                            .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto entity) {
        Optional<User> optionalUser = userAuthenticationService.authenticate(entity);
        // Check if the user is authenticated
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String jwtToken = jwtService.generateToken(user);
            
            // Create and return the login response with JWT token and expiration time
            LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getJwtExpiration());
            return ResponseEntity.ok().body(loginResponse);
        }
        // If user authentication fails, return unauthorized status
        return ResponseEntity.badRequest().build();
    }

}
