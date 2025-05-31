package com.example.animal_clinic.services;

import com.example.animal_clinic.entities.Pet;
import com.example.animal_clinic.repositories.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets(){
        logger.debug("Method getAllPets() called. Returning all pets from database.");
        return petRepository.findAll();
    }

    public Optional<Pet> getPetById(Long petId){
        logger.debug("Method getPetById({}) called. Returning pet with id {} from database.", petId, petId);
        return petRepository.findById(petId);
    }

    public Pet savePet(Pet pet){
        logger.debug("Method savePet({}) called. Saving pet {} to database.", pet, pet);
        try{
         return petRepository.save(pet);
        } catch (Exception e) {
            throw new RuntimeException("Error saving pet: " + e.getMessage());
        }

    }
}
