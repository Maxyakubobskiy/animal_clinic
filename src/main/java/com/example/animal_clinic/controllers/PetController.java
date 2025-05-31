package com.example.animal_clinic.controllers;

import com.example.animal_clinic.entities.Pet;
import com.example.animal_clinic.services.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPets(){
        logger.info("GET /pets - Get all pets from database.");
        List<Pet> petList = petService.getAllPets();
        if(petList.isEmpty()) {
            logger.warn("GET /pets - No pets found in database.");
            return ResponseEntity.notFound().build();
        }
        logger.info("GET /pets - Found {} pets in database.", petList.size());
        return ResponseEntity.ok(petList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetById(@PathVariable Long id){
        logger.info("GET /pets/{} - Get pet with id {} from database.", id,id);

        return petService.getPetById(id)
                .map(pet -> {
                    logger.info("GET /pets/{} - Found pet with id {} in database.", id,id);
                    return ResponseEntity.ok(pet);
                })
                .orElseGet(()->{
                    logger.warn("GET /pets/{} - No pet with id {} found in database.", id,id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<?> addPet(@RequestBody Pet pet){
        logger.info("POST /pets - Add pet {} to database.", pet);
        if (pet == null) {
            logger.warn("POST /pets - Pet is null.");
            return ResponseEntity.badRequest().build();
        }
        try{
            Pet savedPet = petService.savePet(pet);
            logger.info("POST /pets - Pet {} saved to database.", savedPet);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedPet);
        }catch (Exception e){
            logger.error("POST /pets - Error saving pet {} to database.", pet, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }
}
