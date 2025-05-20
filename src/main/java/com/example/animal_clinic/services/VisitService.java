package com.example.animal_clinic.services;

import com.example.animal_clinic.DTO.VisitDTO;
import com.example.animal_clinic.entities.Pet;
import com.example.animal_clinic.entities.Visit;
import com.example.animal_clinic.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PetService petService;

    @Autowired
    public VisitService(VisitRepository visitRepository, PetService petService) {
        this.visitRepository = visitRepository;
        this.petService = petService;
    }

    public Visit saveVisit(VisitDTO visit) {
        try{
            Pet pet = petService.getPetById(visit.getPetId()).orElseThrow();
            Visit newVisit = new Visit(visit.getVisitDate(),visit.getDescription(),pet);
            return visitRepository.save(newVisit);
        } catch (Exception e) {
            throw new RuntimeException("Error saving visit: " + e.getMessage());
        }
    }

    public List<Visit> getVisitsByPetId(Long petId){
        return visitRepository.findAllByPetId(petId);
    }
}
