package com.example.umuwork.service;

import com.example.umuwork.dto.JobApplicationResponseDTO;
import com.example.umuwork.dto.JobRequestDTO;
import com.example.umuwork.dto.JobResponseDTO;
import com.example.umuwork.enums.JobStatus;
import com.example.umuwork.enums.TradeType;
import com.example.umuwork.enums.UserRole;
import com.example.umuwork.model.JobRequest;
import com.example.umuwork.model.User;
import com.example.umuwork.repository.JobRequestRepository;
import com.example.umuwork.repository.UserRepository;
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
public class JobService {

    private final JobRequestRepository jobRequestRepository;
    private final UserRepository userRepository;

    // ── POST A JOB ───────────────────────────────────────
    @Transactional
    public JobResponseDTO postJob(Long clientId,
                                  JobRequestDTO request) {
        log.info("Client {} posting a new job", clientId);

        // 1. Check client exists
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + clientId));

        // 2. Check user is a CLIENT
        if (client.getRole() != UserRole.CLIENT) {
            throw new RuntimeException(
                    "Only clients can post jobs");
        }

        // 3. Convert RequestDTO → Entity
        JobRequest job = new JobRequest();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setBudget(request.getBudget());
        job.setPreferredStartDate(request.getPreferredStartDate());
        job.setClient(client);
        job.setStatus(JobStatus.OPEN);

        // 4. Save to database
        JobRequest saved = jobRequestRepository.save(job);
        log.info("Job posted successfully with id: {}", saved.getId());

        // 5. Return ResponseDTO
        return mapToResponseDTO(saved);
    }
    public JobResponseDTO getJobById(Long jobId) {
        log.info("Fetching job with id: {}", jobId);

        JobRequest job = jobRequestRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + jobId));

        return mapToResponseDTO(job);
    }

    // ── SEARCH JOBS ──────────────────────────────────────
    public Page<JobResponseDTO> searchJobs(
            String location,
            TradeType trade,
            JobStatus status,
            int page,
            int size) {

        log.info("Searching jobs: location={}, trade={}, status={}",
                location, trade, status);

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("createdAt").descending());

        return jobRequestRepository
                .searchJobs(location, trade, status, pageable)
                .map(this::mapToResponseDTO);
    }

    // ── ASSIGN WORKER TO JOB ─────────────────────────────
    @Transactional
    public JobResponseDTO assignWorker(Long jobId, Long workerId) {
        log.info("Assigning worker {} to job {}", workerId, jobId);

        // 1. Get the job
        JobRequest job = jobRequestRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + jobId));

        // 2. Check job is still open
        if (job.getStatus() != JobStatus.OPEN) {
            throw new RuntimeException(
                    "Job is no longer open for assignment");
        }

        // 3. Get the worker
        User worker = userRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException(
                        "Worker not found with id: " + workerId));

        // 4. Assign and update status
        job.setAssignedWorker(worker);
        job.setStatus(JobStatus.ASSIGNED);

        JobRequest updated = jobRequestRepository.save(job);
        log.info("Worker {} assigned to job {}", workerId, jobId);

        return mapToResponseDTO(updated);
    }

    // ── COMPLETE JOB ─────────────────────────────────────
    @Transactional
    public JobResponseDTO completeJob(Long jobId) {
        log.info("Completing job with id: {}", jobId);

        JobRequest job = jobRequestRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + jobId));

        if (job.getStatus() != JobStatus.ASSIGNED) {
            throw new RuntimeException(
                    "Only assigned jobs can be marked complete");
        }

        job.setStatus(JobStatus.COMPLETED);
        JobRequest updated = jobRequestRepository.save(job);

        return mapToResponseDTO(updated);
    }

    // ── CANCEL JOB ───────────────────────────────────────
    @Transactional
    public JobResponseDTO cancelJob(Long jobId) {
        log.info("Cancelling job with id: {}", jobId);

        JobRequest job = jobRequestRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + jobId));

        if (job.getStatus() == JobStatus.COMPLETED) {
            throw new RuntimeException(
                    "Cannot cancel a completed job");
        }

        job.setStatus(JobStatus.CANCELLED);
        JobRequest updated = jobRequestRepository.save(job);

        return mapToResponseDTO(updated);
    }



    public JobResponseDTO mapToResponseDTO(JobRequest job){
        JobResponseDTO response=new JobResponseDTO();
        response.setId((long) job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setLocation(job.getLocation());
        response.setBudget(job.getBudget());
        response.setStatus(job.getStatus());
        response.setPreferredStartDate(job.getPreferredStartDate());
        response.setCreatedAt(job.getCreatedAt());

        // Client info from related User entity
        response.setClientId((long) job.getClient().getId());
        response.setClientName(job.getClient().getFullname());
        response.setClientPhone(job.getClient().getPhone());

        // Assigned worker info — only if job is assigned
        if (job.getAssignedWorker() != null) {
            response.setAssignedWorkerId(
                    (long) job.getAssignedWorker().getId());
            response.setAssignedWorkerName(
                    job.getAssignedWorker().getFullname());
        }

        return response;
    }
}



