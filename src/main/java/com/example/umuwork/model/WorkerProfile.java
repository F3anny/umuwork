package com.example.umuwork.model;

import com.example.umuwork.enums.TradeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "worker_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType trade;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String bio;

    @Column(name = "years_experience", nullable = false)
    private int yearsExperience;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Column(name = "avg_rating")
    private double avgRating = 0.0;

    @Column(name = "total_jobs_done")
    private int totalJobsDone = 0;
}