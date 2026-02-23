package com.len.leave.flowable.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Audit Log Entity
 * 
 * Records all state transitions and actions in the workflow.
 * This provides audit trail for compliance and debugging.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
@Entity
@Table(name = "audit_logs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "leave_request_id", nullable = false)
    private UUID leaveRequestId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private AuditAction action;

    @Column(name = "previous_status")
    private String previousStatus;

    @Column(name = "new_status")
    private String newStatus;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "role")
    private String role;

    @Column(name = "comment")
    private String comment;

    @Column(name = "process_instance_id")
    private String processInstanceId;

    @Column(name = "ip_address")
    private String ipAddress;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Audit Action Types
     */
    public enum AuditAction {
        SUBMITTED,
        MANAGER_APPROVED,
        MANAGER_REJECTED,
        HR_APPROVED,
        HR_REJECTED,
        UPDATED,
        CANCELLED
    }
}
