package com.example.umuwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {

    private Long id;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    // Who left the review
    private Long clientId;
    private String clientName;

    // Who was reviewed
    private Long workerId;
    private String workerName;
    private String workerTrade;
}