package com.example.umuwork.repository;

import com.example.umuwork.enums.TradeType;
import com.example.umuwork.model.WorkerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkerProfileRepository extends JpaRepository<WorkerProfile,Long> {
    WorkerProfile findByUserId(Long UserId);
    boolean existsByUserId(Long UserId);
    Page<WorkerProfile> findByTradeAndisVerified(TradeType trade, boolean isVerified, Pageable pageable);
    Page<WorkerProfile> findByLocationContainingIgnoreCaseAndTrade(
           String location,
           TradeType trade,
           Pageable pageable
    );
    // Find top rated workers — custom JPQL query
    @Query("SELECT w FROM WorkerProfile w " +
            "WHERE w.avgRating >= :minRating " +
            "AND w.isVerified = true " +
            "ORDER BY w.avgRating DESC")
    Page<WorkerProfile> findTopRatedWorkers(
            @Param("minRating") double minRating,
            Pageable pageable
    );

    // Full search — location + trade + minimum rating
    @Query("SELECT w FROM WorkerProfile w " +
            "WHERE (:location IS NULL OR LOWER(w.location) " +
            "LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:trade IS NULL OR w.trade = :trade) " +
            "AND w.avgRating >= :minRating " +
            "AND w.isVerified = true " +
            "ORDER BY w.avgRating DESC")
    Page<WorkerProfile> searchWorkers(
            @Param("location") String location,
            @Param("trade") TradeType trade,
            @Param("minRating") double minRating,
            Pageable pageable
    );


}
