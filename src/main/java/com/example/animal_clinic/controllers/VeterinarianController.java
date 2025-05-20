package com.example.animal_clinic.controllers;

import com.example.animal_clinic.entities.Veterinarian;
import com.example.animal_clinic.services.VeterinarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vets")
public class VeterinarianController {

    private final VeterinarianService veterinarianService;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    @GetMapping
    public ResponseEntity<?> getAllVets(){
        List<Veterinarian> vets = veterinarianService.getAllVet();
        if(vets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVetById(@PathVariable Long id){
        return veterinarianService.getVetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
