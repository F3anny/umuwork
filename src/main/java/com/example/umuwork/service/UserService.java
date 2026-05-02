package com.example.umuwork.service;

import com.example.umuwork.dto.UserRequestDTO;
import com.example.umuwork.dto.UserResponseDTO;
import com.example.umuwork.model.User;
import com.example.umuwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserResponseDTO register(UserRequestDTO request){
        log.info("Registering new user with email:{}",request.getEmail());
        if(userRepository.existsByEmail(request.getEmail())){
                  throw new RuntimeException("Email arleady in use");
        }
        if(userRepository.existsByPhone(request.getPhone())){
            throw new RuntimeException("Phone arleady exists");
        }
        User user=new User();
        user.setFullname(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());

        User saved=userRepository.save(user);
        log.info("User saved with id:{}",saved.getId());
        return mapToResponseDTO(saved);

    }
    public UserResponseDTO getUserById(Long id){
        log.info("Fetching use with id:{}",id);
        User user=userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found with id:"+id) );
        return mapToResponseDTO(user);


    }
    public List<UserResponseDTO> getAllUsers(){
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());



    }
    @Transactional
    public UserResponseDTO updateUser(Long id,UserRequestDTO request) {
        log.info("Updating user with id:{}", id);
        User user =userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found with id:"+id));
        user.setFullname(request.getFullName());
        user.setPhone(request.getPhone());

        User updated=userRepository.save(user);
        log.info("User updated with id:"+updated.getId());
        return mapToResponseDTO(updated);


    }

    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId((long) user.getId());
        response.setFullName(user.getFullname());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
