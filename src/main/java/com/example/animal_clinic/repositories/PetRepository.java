package com.example.animal_clinic.repositories;

import com.example.animal_clinic.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet,Long> {
}
