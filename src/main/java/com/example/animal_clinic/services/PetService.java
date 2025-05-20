package com.example.animal_clinic.services;

import com.example.animal_clinic.entities.Pet;
import com.example.animal_clinic.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets(){
        return petRepository.findAll();
    }

    public Optional<Pet> getPetById(Long petId){
        return petRepository.findById(petId);
    }

    public Pet savePet(Pet pet){
        try{
         return petRepository.save(pet);
        } catch (Exception e) {
            throw new RuntimeException("Error saving pet: " + e.getMessage());
        }

    }
}
