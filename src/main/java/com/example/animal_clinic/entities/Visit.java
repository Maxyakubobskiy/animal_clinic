package com.example.animal_clinic.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate visitDate;
    private String description;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @JsonBackReference
    private Pet pet;

    public Visit(LocalDate visitDate, String description, Pet pet) {
        this.visitDate = visitDate;
        this.description = description;
        this.pet = pet;
    }
}
