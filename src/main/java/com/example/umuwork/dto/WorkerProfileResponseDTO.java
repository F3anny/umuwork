package com.example.umuwork.dto;

import com.example.umuwork.enums.TradeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerProfileResponseDTO {

    private Long id;
    private Long userId;
    private String workerFullName;
    private String workerPhone;
    private TradeType trade;
    private String bio;
    private int yearsExperience;
    private String location;
    private boolean isVerified;
    private double avgRating;
    private int totalJobsDone;
}