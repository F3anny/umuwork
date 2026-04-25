package com.example.umuwork.model;

import com.example.umuwork.enums.JobStatus;
import com.example.umuwork.enums.TradeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor

public class JobRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String title;
    @Column(nullable=false,length = 1000)
    private String description;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private Double budget;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType tradeNeeded;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private JobStatus status=JobStatus.OPEN;
    @Column(name="preffered_start_date")
    private LocalDateTime prefferedStartDate;
    @Column(name="created_At")
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name="assigned_worker_id")
    private User assignedWorker;
    @OneToMany(mappedBy = "jobRequest",cascade=CascadeType.ALL)
    private List<JobApplication> applications;
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
        status=JobStatus.OPEN;
    }




}
