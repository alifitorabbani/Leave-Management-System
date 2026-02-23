package com.len.leave.flowable.dto;

import com.len.leave.flowable.entity.LeaveStatus;
import com.len.leave.flowable.entity.LeaveType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Leave Request DTO
 * 
 * Data Transfer Object for creating new leave requests.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
public class LeaveRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        
        @NotBlank(message = "Nama employee wajib diisi")
        private String employeeName;

        private String employeeId;

        private String department;

        @NotNull(message = "Jenis cuti wajib dipilih")
        private LeaveType leaveType;

        @NotNull(message = "Tanggal mulai wajib diisi")
        private LocalDate startDate;

        @NotNull(message = "Tanggal selesai wajib diisi")
        private LocalDate endDate;

        @NotBlank(message = "Alasan wajib diisi")
        private String reason;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID id;
        private String employeeName;
        private String employeeId;
        private String department;
        private LeaveType leaveType;
        private LocalDate startDate;
        private LocalDate endDate;
        private String reason;
        private LeaveStatus status;
        private String managerComment;
        private String hrComment;
        private String approvedByManager;
        private String approvedByHR;
        private LocalDateTime managerApprovalDate;
        private LocalDateTime hrApprovalDate;
        private Integer totalDays;
        private String processInstanceId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApprovalRequest {
        
        @NotBlank(message = "Comment wajib diisi")
        private String comment;

        @NotBlank(message = "Approved by wajib diisi")
        private String approvedBy;

        private String role;
        
        private String taskId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusUpdateRequest {
        
        @NotNull(message = "Status wajib diisi")
        private LeaveStatus status;

        private String comment;

        @NotBlank(message = "Updated by wajib diisi")
        private String updatedBy;
        
        private String taskId;
    }
}
