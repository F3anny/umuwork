package com.example.umuwork.model;

import com.example.umuwork.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cover_letter", nullable = false, length = 500)
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    // The worker who applied
    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    // The job they applied to
    @ManyToOne
    @JoinColumn(name = "job_request_id", nullable = false)
    private JobRequest jobRequest;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
        status = ApplicationStatus.PENDING;
    }
}