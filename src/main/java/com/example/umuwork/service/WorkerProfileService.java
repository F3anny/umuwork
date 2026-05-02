package com.example.umuwork.service;

import com.example.umuwork.dto.WorkerProfileRequestDTO;
import com.example.umuwork.dto.WorkerProfileResponseDTO;
import com.example.umuwork.enums.TradeType;
import com.example.umuwork.model.User;
import com.example.umuwork.model.WorkerProfile;
import com.example.umuwork.repository.UserRepository;
import com.example.umuwork.repository.WorkerProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.example.umuwork.enums.UserRole.WORKER;

@Service
@RequiredArgsConstructor
@Slf4j

public class WorkerProfileService {
    private final WorkerProfileRepository workerProfileRepository;
    private final UserRepository userRepository;
    @Transactional
    public WorkerProfileResponseDTO createProfile(Long userId, WorkerProfileRequestDTO request){
        log.info("Creating profile for user with id:{}",userId);

        User user=userRepository.findById(userId).orElseThrow(
                ()->new RuntimeException("User nto found with id:"+userId));

        if(user.getRole()!= WORKER){
            throw new RuntimeException("Only user who is a worker is allowed to create a profile");
        }
        if(workerProfileRepository.existsByUserId(userId)){
            throw new RuntimeException("WorkerProfile for user with this id arleady exists");
        }
        WorkerProfile profile=new WorkerProfile();
        profile.setUser(user);
        profile.setTrade(request.getTrade());
        profile.setBio(request.getBio());
        profile.setYearsExperience(request.getYearsExperience());
        profile.setLocation(request.getLocation());
        profile.setVerified(false);
        profile.setAvgRating(0.0);
        profile.setTotalJobsDone(0);

        WorkerProfile saved = workerProfileRepository.save(profile);
        log.info("Worker profile created with id: {}", saved.getId());

        // 6. Return ResponseDTO
        return mapToResponseDTO(saved);

    }
    public WorkerProfileResponseDTO getProfile(Long userId){
        log.info("Fetching the workerProfile for user:{}",userId);
        WorkerProfile profile=workerProfileRepository.findByUserId(userId);
        if(profile==null){
            throw new RuntimeException("User with this id not found "+userId);
        }
        return mapToResponseDTO(profile);


    }
     public Page<WorkerProfileResponseDTO> searchWorkers(String location,
                                                         TradeType trade,
                                                         double minRating,
                                                         int page,
                                                         int size){
         log.info("Searching workers: location={}, trade={}, minRating={}",
                 location, trade, minRating);
         Pageable pageable= PageRequest.of(page,size, Sort.by("AvgRating").descending());
         return workerProfileRepository
                 .searchWorkers(location, trade, minRating, pageable)
                 .map(this::mapToResponseDTO);

     }
     @Transactional
     public void UpdateAvgRating(long userId,Double newAverage){
        log.info("Updating rating for user");
        WorkerProfile profile =workerProfileRepository.findByUserId(userId);
        if(profile!=null){
            profile.setAvgRating(newAverage);
            workerProfileRepository.save(profile);
        }


     }



    public WorkerProfileResponseDTO mapToResponseDTO(WorkerProfile profile) {
        WorkerProfileResponseDTO response = new WorkerProfileResponseDTO();
        response.setId(profile.getId());
        response.setUserId((long) profile.getUser().getId());
        response.setWorkerFullName(profile.getUser().getFullname());
        response.setWorkerPhone(profile.getUser().getPhone());
        response.setTrade(profile.getTrade());
        response.setBio(profile.getBio());
        response.setYearsExperience(profile.getYearsExperience());
        response.setLocation(profile.getLocation());
        response.setVerified(profile.isVerified());
        response.setAvgRating(profile.getAvgRating());
        response.setTotalJobsDone(profile.getTotalJobsDone());

        return response;


    }
}
