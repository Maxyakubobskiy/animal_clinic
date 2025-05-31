package com.example.animal_clinic.services;

import com.example.animal_clinic.entities.Veterinarian;
import com.example.animal_clinic.repositories.VeterinarianRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarianService {

    private static final Logger logger = LoggerFactory.getLogger(VeterinarianService.class);
    private final VeterinarianRepository veterinarianRepository;

    @Autowired
    public VeterinarianService(VeterinarianRepository veterinarianRepository) {
        this.veterinarianRepository = veterinarianRepository;
    }

    public List<Veterinarian> getAllVet() {
        logger.debug("Method getAllVet() called. Returning all veterinarians from database.");
        return veterinarianRepository.findAll();
    }

    public Optional<Veterinarian> getVetById(Long id){
        logger.debug("Method getVetById({}) called. Returning veterinarian with id {} from database.", id, id);
        return veterinarianRepository.findById(id);
    }
}
