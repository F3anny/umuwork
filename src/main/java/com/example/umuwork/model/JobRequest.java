package com.example.umuwork.model;

import com.example.umuwork.enums.JobStatus;
import com.example.umuwork.enums.TradeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // ← changed from int to Long

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Double budget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType tradeNeeded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(name = "preferred_start_date")  // ← fixed spelling
    private LocalDateTime preferredStartDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ← THIS WAS MISSING — client who posted the job
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    // Worker assigned — null until client picks someone
    @ManyToOne
    @JoinColumn(name = "assigned_worker_id")
    private User assignedWorker;

    // All applications for this job
    @OneToMany(mappedBy = "jobRequest", cascade = CascadeType.ALL)
    private List<JobApplication> applications;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = JobStatus.OPEN;
    }
}