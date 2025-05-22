package com.example.animal_clinic.services;

import com.example.animal_clinic.entities.User;
import com.example.animal_clinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user){
        try{
            if(userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new RuntimeException("User with username " + user.getUsername() + " already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }
}
