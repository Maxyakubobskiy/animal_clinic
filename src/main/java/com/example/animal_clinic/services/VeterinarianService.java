package com.example.animal_clinic.services;

import com.example.animal_clinic.entities.Veterinarian;
import com.example.animal_clinic.repositories.VeterinarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarianService {

    private final VeterinarianRepository veterinarianRepository;

    @Autowired
    public VeterinarianService(VeterinarianRepository veterinarianRepository) {
        this.veterinarianRepository = veterinarianRepository;
    }

    public List<Veterinarian> getAllVet(){
        return veterinarianRepository.findAll();
    }

    public Optional<Veterinarian> getVetById(Long id){
        return veterinarianRepository.findById(id);
    }
}
