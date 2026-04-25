package com.example.umuwork.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JobApplicationRequestDTO {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @NotBlank(message = "Cover letter is required")
    @Size(min = 20, max = 500,
            message = "Cover letter must be between 20 and 500 characters")
    private String coverLetter;
}