package com.example.umuwork.repository;

import com.example.umuwork.model.JobRequest;
import com.example.umuwork.enums.JobStatus;
import com.example.umuwork.enums.TradeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {

    // All jobs posted by a specific client
    Page<JobRequest> findByClientId(Long clientId, Pageable pageable);

    // All open jobs by trade type — worker searches for jobs
    Page<JobRequest> findByStatusAndTradeNeeded(
            JobStatus status,
            TradeType tradeNeeded,
            Pageable pageable
    );

    // All jobs assigned to a specific worker
    List<JobRequest> findByAssignedWorkerId(Long workerId);

    // Search jobs by location and status
    Page<JobRequest> findByLocationContainingIgnoreCaseAndStatus(
            String location,
            JobStatus status,
            Pageable pageable
    );

    // Full job search — location + trade + status
    @Query("SELECT j FROM JobRequest j " +
            "WHERE (:location IS NULL OR LOWER(j.location) " +
            "LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:trade IS NULL OR j.tradeNeeded = :trade) " +
            "AND (:status IS NULL OR j.status = :status) " +
            "ORDER BY j.createdAt DESC")
    Page<JobRequest> searchJobs(
            @Param("location") String location,
            @Param("trade") TradeType trade,
            @Param("status") JobStatus status,
            Pageable pageable
    );

    // Count jobs by status for a client — useful for dashboard
    long countByClientIdAndStatus(Long clientId, JobStatus status);
}