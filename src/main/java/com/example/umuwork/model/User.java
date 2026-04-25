package com.example.umuwork.model;

import com.example.umuwork.enums.UserRole;
import com.example.umuwork.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column(name="full_name",nullable = false)
    private String fullname;
    @Email
    @NotBlank
    @Column(nullable = false,unique = true)
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String password;
    @NotBlank
    @Column(nullable = false)
    private String phone;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
    }


}