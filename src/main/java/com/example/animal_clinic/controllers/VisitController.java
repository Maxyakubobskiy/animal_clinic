package com.example.animal_clinic.controllers;

import com.example.animal_clinic.DTO.VisitDTO;
import com.example.animal_clinic.entities.Visit;
import com.example.animal_clinic.services.VisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
public class VisitController {

    private static final Logger logger = LoggerFactory.getLogger(VisitController.class);
    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public ResponseEntity<?> addVisit(@RequestBody VisitDTO visit){
        logger.info("POST /visits - Add visit {} to database.", visit);
        if (visit == null){
            logger.warn("POST /visits - Visit is null.");
            return ResponseEntity.badRequest().build();
        }
        try{
            Visit savedVisit = visitService.saveVisit(visit);
            logger.info("POST /visits - Visit {} saved to database.", savedVisit);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedVisit);
        }catch (Exception e){
            logger.error("POST /visits - Error saving visit {} to database.", visit, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }
    @GetMapping("/by-pet/{petId}")
    public ResponseEntity<?> getVisitsByPetId(@PathVariable Long petId){
        logger.info("GET /visits/by-pet/{} - Get visits for pet with id {} from database.", petId, petId);
        List<Visit> visits = visitService.getVisitsByPetId(petId);
        if(visits.isEmpty()) {
            logger.warn("GET /visits/by-pet/{} - No visits found for pet with id {} in database.", petId, petId);
            return ResponseEntity.notFound().build();
        }
        logger.info("GET /visits/by-pet/{} - Found {} visits for pet with id {} in database.", petId, visits.size(), petId);
        return ResponseEntity.ok(visits);
    }
}
