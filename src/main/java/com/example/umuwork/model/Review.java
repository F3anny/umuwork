package com.example.umuwork.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 300)
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Client who left the review
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    // Worker who received the review
    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    // The job this review is about
    @ManyToOne
    @JoinColumn(name = "job_request_id", nullable = false)
    private JobRequest jobRequest;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}