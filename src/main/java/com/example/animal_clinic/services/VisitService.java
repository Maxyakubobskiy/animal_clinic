package com.example.animal_clinic.services;

import com.example.animal_clinic.DTO.VisitDTO;
import com.example.animal_clinic.entities.Pet;
import com.example.animal_clinic.entities.Visit;
import com.example.animal_clinic.repositories.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitService {

    private static final Logger logger = LoggerFactory.getLogger(VisitService.class);
    private final VisitRepository visitRepository;
    private final PetService petService;

    @Autowired
    public VisitService(VisitRepository visitRepository, PetService petService) {
        this.visitRepository = visitRepository;
        this.petService = petService;
    }

    public Visit saveVisit(VisitDTO visit) {
        logger.debug("Method saveVisit({}) called. Saving visit {} to database.", visit, visit);
        try{
            Pet pet = petService.getPetById(visit.getPetId()).orElseThrow();
            Visit newVisit = new Visit(visit.getVisitDate(),visit.getDescription(),pet);
            return visitRepository.save(newVisit);
        } catch (Exception e) {
            throw new RuntimeException("Error saving visit: " + e.getMessage());
        }
    }

    public List<Visit> getVisitsByPetId(Long petId){
        logger.debug("Method getVisitsByPetId({}) called. Returning visits for pet with id {} from database.", petId, petId);
        return visitRepository.findAllByPetId(petId);
    }
}
