package com.example.umuwork.dto;

import com.example.umuwork.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationResponseDTO {

    private Long id;
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    // Job info
    private Long jobId;
    private String jobTitle;
    private String jobLocation;

    // Worker info
    private Long workerId;
    private String workerName;
    private String workerTrade;
    private double workerRating;
}