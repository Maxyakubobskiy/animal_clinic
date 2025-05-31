package com.example.animal_clinic.security;

import com.example.animal_clinic.entities.User;
import com.example.animal_clinic.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername({}) called.", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("loadUserByUsername({}) - User not found.", username);
                    return new UsernameNotFoundException("User not found");});
        logger.info("loadUserByUsername({}) - Found user.", username);
        return new CustomUserDetails(user);
    }
}
