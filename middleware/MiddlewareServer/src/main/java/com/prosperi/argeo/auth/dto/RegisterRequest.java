package com.prosperi.argeo.auth.dto;

import java.time.LocalDateTime;

import com.prosperi.argeo.enums.UserRole;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;
    private String phone;
    private String address;
    private LocalDateTime lastAccess;
}