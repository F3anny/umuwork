package com.example.umuwork.repository;

import com.example.umuwork.model.JobApplication;
import com.example.umuwork.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // All applications for a specific job
    List<JobApplication> findByJobRequestId(Long jobRequestId);

    // All applications by a specific worker
    List<JobApplication> findByWorkerId(Long workerId);

    // Check if worker already applied to this job
    boolean existsByWorkerIdAndJobRequestId(
            Long workerId,
            Long jobRequestId
    );

    // Find specific application — worker + job combination
    Optional<JobApplication> findByWorkerIdAndJobRequestId(
            Long workerId,
            Long jobRequestId
    );

    // All applications by status for a job
    List<JobApplication> findByJobRequestIdAndStatus(
            Long jobRequestId,
            ApplicationStatus status
    );

    // Count applications per job
    long countByJobRequestId(Long jobRequestId);
}