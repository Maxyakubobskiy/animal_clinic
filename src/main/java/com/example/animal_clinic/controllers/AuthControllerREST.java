package com.example.animal_clinic.controllers;

import com.example.animal_clinic.entities.User;
import com.example.animal_clinic.security.JWTUtils;
import com.example.animal_clinic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthControllerREST {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    @Autowired
    public AuthControllerREST(UserService userService, AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User authRequest){
        if (authRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtUtils.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(token);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        try{
            User savedUser = userService.saveUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedUser);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }
}
