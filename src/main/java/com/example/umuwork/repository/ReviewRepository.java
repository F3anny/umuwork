package com.example.umuwork.repository;

import com.example.umuwork.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findByWorkerId(Long workerId, Pageable pageable);
    Page<Review> findByClientId(Long clientId ,Pageable pageable);
    boolean existsByClientAndWorkerIdAndJobRequestId(
            Long clientId,
            Long workerId,
            Long jobRequestId
    );
    @Query("SELECT AVG(r.rating) FROM Review r " +
            "WHERE r.worker.id = :workerId")
    Double calculateAverageRating(@Param("workerId") Long workerId);

    // Count total reviews for a worker
    long countByWorkerId(Long workerId);



}
