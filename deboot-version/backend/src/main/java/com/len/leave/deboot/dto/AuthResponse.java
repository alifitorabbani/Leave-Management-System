package com.len.leave.deboot.dto;

import com.len.leave.deboot.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Authentication Response DTO
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private String employeeId;
    private String department;
    private UserRole role;
    private String token;
    private LocalDateTime loginTime;
}
