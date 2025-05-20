package com.example.animal_clinic.controllers;

import com.example.animal_clinic.entities.Pet;
import com.example.animal_clinic.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPets(){
        List<Pet> petList = petService.getAllPets();
        if(petList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(petList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetById(@PathVariable Long id){
        return petService.getPetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addPet(@RequestBody Pet pet){
        if (pet == null)
            return ResponseEntity.badRequest().build();

        try{
            Pet savedPet = petService.savePet(pet);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedPet);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }
}
