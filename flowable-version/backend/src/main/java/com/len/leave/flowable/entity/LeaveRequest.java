package com.len.leave.flowable.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Leave Request Entity
 * 
 * Main entity representing a leave request in the Flowable BPM workflow system.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
@Entity
@Table(name = "leave_requests")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "department")
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "manager_comment")
    private String managerComment;

    @Column(name = "hr_comment")
    private String hrComment;

    @Column(name = "approved_by_manager")
    private String approvedByManager;

    @Column(name = "approved_by_hr")
    private String approvedByHR;

    @Column(name = "manager_approval_date")
    private LocalDateTime managerApprovalDate;

    @Column(name = "hr_approval_date")
    private LocalDateTime hrApprovalDate;

    @Column(name = "total_days")
    private Integer totalDays;

    @Column(name = "process_instance_id")
    private String processInstanceId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Calculate total days of leave
     */
    @PrePersist
    @PreUpdate
    public void calculateTotalDays() {
        if (startDate != null && endDate != null) {
            this.totalDays = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
    }
}
