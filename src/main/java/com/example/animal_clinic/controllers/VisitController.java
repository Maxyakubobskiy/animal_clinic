package com.example.animal_clinic.controllers;

import com.example.animal_clinic.DTO.VisitDTO;
import com.example.animal_clinic.entities.Visit;
import com.example.animal_clinic.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public ResponseEntity<?> addVisit(@RequestBody VisitDTO visit){
        if (visit == null){
            return ResponseEntity.badRequest().build();
        }

        try{
            Visit savedVisit = visitService.saveVisit(visit);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedVisit);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }
    @GetMapping("/by-pet/{petId}")
    public ResponseEntity<?> getVisitsByPetId(@PathVariable Long petId){
        List<Visit> visits = visitService.getVisitsByPetId(petId);
        if(visits.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visits);
    }
}
