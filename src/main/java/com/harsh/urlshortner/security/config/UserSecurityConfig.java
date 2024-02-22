package com.harsh.urlshortner.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.harsh.urlshortner.repository.UserRepository;


/**
 * Configuration class for Spring Security.
 */
@Configuration
public class UserSecurityConfig {
    
    private final UserRepository userRepository;

    public UserSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Defines a UserDetailsService bean that retrieves user details from the UserRepository.
     * 
     * @return The UserDetailsService bean
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not Found!!!"));
    }

    /**
     * Defines a PasswordEncoder bean for encoding and verifying passwords.
     * 
     * @return The PasswordEncoder bean
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines an AuthenticationManager bean for managing authentication processes.
     * 
     * @param configuration The AuthenticationConfiguration
     * @return The AuthenticationManager bean
     * @throws Exception If an error occurs while configuring the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Defines an AuthenticationProvider bean for authenticating users using user details and passwords.
     * 
     * @return The AuthenticationProvider bean
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
