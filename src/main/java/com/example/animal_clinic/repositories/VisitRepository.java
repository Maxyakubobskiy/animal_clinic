package com.example.animal_clinic.repositories;

import com.example.animal_clinic.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAllByPetId(Long petId);
}
