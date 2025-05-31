package com.example.animal_clinic.controllers;

import com.example.animal_clinic.entities.User;
import com.example.animal_clinic.security.JWTUtils;
import com.example.animal_clinic.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthControllerREST.class);
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
        logger.info("POST /api/auth/login - Login user with username {}.", authRequest!=null ? authRequest.getUsername() : "null");
        if (authRequest == null) {
            logger.warn("POST /api/auth/login - Request body is null.");
            return ResponseEntity.badRequest().build();
        }

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                logger.info("POST /api/auth/login - User {} logged in successfully.", authRequest.getUsername());
                String token = jwtUtils.generateToken(authRequest.getUsername());
                logger.info("POST /api/auth/login - Generated token for user {}.", authRequest.getUsername());
                return ResponseEntity.ok(token);
            }else{
                logger.warn("POST /api/auth/login - User {} failed to log in.", authRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        }catch (Exception e){
            logger.error("POST /api/auth/login - Error logging in user {}.", authRequest.getUsername(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication error " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        logger.info("POST /api/auth/register - Register user with username {}.", user!=null ? user.getUsername() : "null");
        if (user == null) {
            logger.warn("POST /api/auth/register - Request body is null.");
            return ResponseEntity.badRequest().build();
        }
        try{
            User savedUser = userService.saveUser(user);
            logger.info("POST /api/auth/register - User {} registered successfully.", savedUser.getUsername());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedUser);
        }catch (Exception e){
            logger.error("POST /api/auth/register - Error registering user {}.", user.getUsername(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }
}
