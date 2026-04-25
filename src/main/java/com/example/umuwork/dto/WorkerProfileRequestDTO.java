package com.example.umuwork.dto;

import com.example.umuwork.enums.TradeType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class WorkerProfileRequestDTO {

    @NotNull(message = "Trade type is required")
    private TradeType trade;

    @NotBlank(message = "Bio is required")
    @Size(min = 20, max = 500, message = "Bio must be between 20 and 500 characters")
    private String bio;

    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 50, message = "Years of experience seems too high")
    private int yearsExperience;

    @NotBlank(message = "Location is required")
    private String location;
}