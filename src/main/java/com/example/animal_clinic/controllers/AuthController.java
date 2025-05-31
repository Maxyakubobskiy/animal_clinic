package com.example.animal_clinic.controllers;

import com.example.animal_clinic.entities.User;
import com.example.animal_clinic.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        logger.info("GET /auth/login - Login page requested.");
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        logger.info("GET /auth/register - Register page requested.");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password){
        logger.info("POST /auth/register - Register user with username {}.", username);
        try{
            User user = new User(username,password);
            userService.saveUser(user);
            logger.info("POST /auth/register - User {} registered successfully.", username);
            return "redirect:/auth/login";
        }catch (Exception e){
         logger.error("POST /auth/register - Error registering user {}.", username, e);
         return "redirect:/auth/register?error=true";
        }
    }
}
