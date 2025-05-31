package com.example.animal_clinic.controllers;

import com.example.animal_clinic.entities.Veterinarian;
import com.example.animal_clinic.services.VeterinarianService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(VeterinarianController.class);

    private final VeterinarianService veterinarianService;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    @GetMapping
    public ResponseEntity<?> getAllVets(){
        logger.info("GET /vets - Get all vets from database.");
        List<Veterinarian> vets = veterinarianService.getAllVet();
        if(vets.isEmpty()) {
            logger.warn("GET /vets - No vets found in database.");
            return ResponseEntity.notFound().build();
        }
        logger.info("GET /vets - Found {} vets in database.", vets.size());
        return ResponseEntity.ok(vets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVetById(@PathVariable Long id){
        return veterinarianService.getVetById(id)
                .map(veterinarian -> {
                    logger.info("GET /vets/{} - Found vet with id {} in database.", id,id);
                    return ResponseEntity.ok(veterinarian);
                })
                .orElseGet(()->{
                    logger.warn("GET /vets/{} - No vet with id {} found in database.", id,id);
                    return ResponseEntity.notFound().build();
                });
    }
}
