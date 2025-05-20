package com.example.animal_clinic.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VisitDTO {
    private LocalDate visitDate;
    private String description;
    private Long petId;
}
