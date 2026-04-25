package com.example.umuwork.model;

import com.example.umuwork.enums.PaymentMethod;
import com.example.umuwork.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    // MTN MoMo transaction reference
    // This is where MTN sends back their transaction ID
    @Column(name = "momo_reference")
    private String momoReference;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // One payment per job
    @OneToOne
    @JoinColumn(name = "job_request_id", nullable = false)
    private JobRequest jobRequest;

    // Who is paying
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    // Who is receiving payment
    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = PaymentStatus.PENDING;
    }
}