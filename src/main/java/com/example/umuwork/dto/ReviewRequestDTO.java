package com.example.umuwork.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequestDTO {

    @NotNull(message = "Worker ID is required")
    private Long workerId;

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    @Size(min = 10, max = 300,
            message = "Comment must be between 10 and 300 characters")
    private String comment;
}