package com.example.umuwork.service;

import com.example.umuwork.dto.ReviewRequestDTO;
import com.example.umuwork.dto.ReviewResponseDTO;
import com.example.umuwork.enums.JobStatus;
import com.example.umuwork.model.JobRequest;
import com.example.umuwork.model.Review;
import com.example.umuwork.model.User;
import com.example.umuwork.repository.JobRequestRepository;
import com.example.umuwork.repository.ReviewRepository;
import com.example.umuwork.repository.UserRepository;
import com.example.umuwork.repository.WorkerProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JobRequestRepository jobRequestRepository;
    private final WorkerProfileService workerService;

    // ── LEAVE A REVIEW ───────────────────────────────────
    @Transactional
    public ReviewResponseDTO leaveReview(
            Long clientId,
            ReviewRequestDTO request) {

        log.info("Client {} leaving review for worker {}",
                clientId, request.getWorkerId());

        // 1. Get client
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException(
                        "Client not found"));

        // 2. Get worker
        User worker = userRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new RuntimeException(
                        "Worker not found"));

        // 3. Get job
        JobRequest job = jobRequestRepository
                .findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException(
                        "Job not found"));

        // 4. Job must be completed before reviewing
        if (job.getStatus() != JobStatus.COMPLETED) {
            throw new RuntimeException(
                    "You can only review after job is completed");
        }

        // 5. Client cannot review twice for same job
        if (reviewRepository.existsByClientAndWorkerIdAndJobRequestId(
                clientId, request.getWorkerId(), request.getJobId())) {
            throw new RuntimeException(
                    "You already reviewed this worker for this job");
        }

        // 6. Convert RequestDTO → Entity
        Review review = new Review();
        review.setClient(client);
        review.setWorker(worker);
        review.setJobRequest(job);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // 7. Save review
        Review saved = reviewRepository.save(review);

        // 8. Recalculate worker's average rating automatically
        Double newAverage = reviewRepository
                .calculateAverageRating(request.getWorkerId());
        workerService.UpdateAvgRating(
                request.getWorkerId(), newAverage);

        log.info("Review saved and worker rating updated");

        return mapToResponseDTO(saved);
    }

    // ── GET WORKER REVIEWS ───────────────────────────────
    public Page<ReviewResponseDTO> getWorkerReviews(
            Long workerId, int page, int size) {

        log.info("Fetching reviews for worker: {}", workerId);

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("createdAt").descending());

        return reviewRepository
                .findByWorkerId(workerId, pageable)
                .map(this::mapToResponseDTO);
    }

    // ── MAPPER ───────────────────────────────────────────
    private ReviewResponseDTO mapToResponseDTO(Review review) {
        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());

        // Client info
        response.setClientId((long) review.getClient().getId());
        response.setClientName(review.getClient().getFullname());

        // Worker info
        response.setWorkerId((long) review.getWorker().getId());
        response.setWorkerName(review.getWorker().getFullname());
        response.setWorkerTrade(
                review.getWorker()
                        .toString()); // we'll fix with workerProfile

        return response;
    }
}