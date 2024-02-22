package com.harsh.urlshortner.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.harsh.urlshortner.entity.User;
import com.harsh.urlshortner.models.LoginUserDto;
import com.harsh.urlshortner.models.RegisterUserDto;
import com.harsh.urlshortner.repository.UserRepository;

@Service
public class UserAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserAuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user with the provided user details.
     * 
     * @param registerUserDto The user details for registration
     * @return An Optional containing the registered user, or empty if registration fails
     */
    public Optional<User> signUp(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setEmail(registerUserDto.getEmail());
        user.setName(registerUserDto.getName());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        return Optional.ofNullable(userRepository.save(user));
    }

    /**
     * Authenticates a user with the provided login credentials.
     * 
     * @param loginUserDto The login credentials
     * @return An Optional containing the authenticated user, or empty if authentication fails
     */
    public Optional<User> authenticate(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        return userRepository.findByEmail(loginUserDto.getEmail());
    } 


}
