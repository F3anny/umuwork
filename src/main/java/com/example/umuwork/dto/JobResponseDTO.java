package com.example.umuwork.dto;

import com.example.umuwork.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String location;
    private Double budget;
    private JobStatus status;
    private LocalDateTime preferredStartDate;
    private LocalDateTime createdAt;

    // Client info - from User entity
    private Long clientId;
    private String clientName;
    private String clientPhone;

    // Assigned worker info - only appears when job is ASSIGNED
    private Long assignedWorkerId;
    private String assignedWorkerName;
}