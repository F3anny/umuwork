package com.example.umuwork.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobRequestDTO {

    @NotBlank(message = "Job title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Job description is required")
    @Size(min = 20, max = 1000, message = "Description must be between 20 and 1000 characters")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Budget is required")
    @Min(value = 1000, message = "Minimum budget is 1000 RWF")
    private Double budget;

    @NotNull(message = "Preferred start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDateTime preferredStartDate;
}