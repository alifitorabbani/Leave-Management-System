package com.len.leave.manual.service;

import com.len.leave.manual.dto.LeaveRequestDTO;
import com.len.leave.manual.entity.AuditLog;
import com.len.leave.manual.entity.LeaveRequest;
import com.len.leave.manual.entity.LeaveStatus;
import com.len.leave.manual.exception.InvalidStateTransitionException;
import com.len.leave.manual.exception.LeaveRequestNotFoundException;
import com.len.leave.manual.repository.AuditLogRepository;
import com.len.leave.manual.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Leave Request Service - Manual Workflow Implementation
 * 
 * This service implements the workflow state machine logic using if-else
 * and enum-based state transitions. This is the CORE of the manual workflow.
 * 
 * State Transition Logic:
 * - PENDING -> MANAGER_APPROVED (when manager approves)
 * - PENDING -> MANAGER_REJECTED (when manager rejects)
 * - MANAGER_APPROVED -> HR_APPROVED (when HR approves)
 * - MANAGER_APPROVED -> HR_REJECTED (when HR rejects)
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (Persero)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final AuditLogRepository auditLogRepository;

    /**
     * Submit a new leave request
     * 
     * New requests start with PENDING status
     */
    @Transactional
    public LeaveRequestDTO.Response submitRequest(LeaveRequestDTO.CreateRequest request) {
        log.info("Submitting new leave request for employee: {}", request.getEmployeeName());
        
        // Validate dates
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Create new leave request with PENDING status
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employeeName(request.getEmployeeName())
                .employeeId(request.getEmployeeId())
                .department(request.getDepartment())
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        
        // Create audit log
        createAuditLog(saved.getId(), null, LeaveStatus.PENDING, 
                AuditLog.AuditAction.SUBMITTED, request.getEmployeeName(), "EMPLOYEE", 
                "Leave request submitted");

        log.info("Leave request submitted successfully with ID: {}", saved.getId());
        return mapToResponse(saved);
    }

    /**
     * Manager approval workflow
     * 
     * TRANSITION: PENDING -> MANAGER_APPROVED (approve) or MANAGER_REJECTED (reject)
     */
    @Transactional
    public LeaveRequestDTO.Response managerApproval(UUID requestId, LeaveRequestDTO.ApprovalRequest approval) {
        log.info("Manager {} processing request: {}", approval.getApprovedBy(), requestId);
        
        LeaveRequest request = getLeaveRequestById(requestId);
        
        // STATE MACHINE VALIDATION
        // Only PENDING requests can be processed by manager
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidStateTransitionException(
                    "Request is not in PENDING status. Current status: " + request.getStatus());
        }

        // Determine new status based on approval decision
        // In real implementation, this would come from the request body
        LeaveStatus newStatus = LeaveStatus.MANAGER_APPROVED;
        AuditLog.AuditAction action = AuditLog.AuditAction.MANAGER_APPROVED;

        // Update request
        request.setStatus(newStatus);
        request.setManagerComment(approval.getComment());
        request.setApprovedByManager(approval.getApprovedBy());
        request.setManagerApprovalDate(LocalDateTime.now());

        LeaveRequest saved = leaveRequestRepository.save(request);

        // Create audit log
        createAuditLog(requestId, LeaveStatus.PENDING, newStatus, 
                action, approval.getApprovedBy(), approval.getRole(), approval.getComment());

        log.info("Request {} processed by manager. New status: {}", requestId, newStatus);
        return mapToResponse(saved);
    }

    /**
     * Manager reject - explicit method for rejection
     */
    @Transactional
    public LeaveRequestDTO.Response managerReject(UUID requestId, LeaveRequestDTO.ApprovalRequest rejection) {
        log.info("Manager {} rejecting request: {}", rejection.getApprovedBy(), requestId);
        
        LeaveRequest request = getLeaveRequestById(requestId);
        
        // STATE MACHINE VALIDATION
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidStateTransitionException(
                    "Request is not in PENDING status. Current status: " + request.getStatus());
        }

        LeaveStatus newStatus = LeaveStatus.MANAGER_REJECTED;

        request.setStatus(newStatus);
        request.setManagerComment(rejection.getComment());
        request.setApprovedByManager(rejection.getApprovedBy());
        request.setManagerApprovalDate(LocalDateTime.now());

        LeaveRequest saved = leaveRequestRepository.save(request);

        createAuditLog(requestId, LeaveStatus.PENDING, newStatus, 
                AuditLog.AuditAction.MANAGER_REJECTED, rejection.getApprovedBy(), 
                rejection.getRole(), rejection.getComment());

        log.info("Request {} rejected by manager. Final status: {}", requestId, newStatus);
        return mapToResponse(saved);
    }

    /**
     * HR approval workflow
     * 
     * TRANSITION: MANAGER_APPROVED -> HR_APPROVED (approve) or HR_REJECTED (reject)
     */
    @Transactional
    public LeaveRequestDTO.Response hrApproval(UUID requestId, LeaveRequestDTO.ApprovalRequest approval) {
        log.info("HR {} processing request: {}", approval.getApprovedBy(), requestId);
        
        LeaveRequest request = getLeaveRequestById(requestId);
        
        // STATE MACHINE VALIDATION
        // Only MANAGER_APPROVED requests can be processed by HR
        if (request.getStatus() != LeaveStatus.MANAGER_APPROVED) {
            throw new InvalidStateTransitionException(
                    "Request is not waiting for HR approval. Current status: " + request.getStatus());
        }

        LeaveStatus newStatus = LeaveStatus.HR_APPROVED;

        request.setStatus(newStatus);
        request.setHrComment(approval.getComment());
        request.setApprovedByHR(approval.getApprovedBy());
        request.setHrApprovalDate(LocalDateTime.now());

        LeaveRequest saved = leaveRequestRepository.save(request);

        createAuditLog(requestId, LeaveStatus.MANAGER_APPROVED, newStatus, 
                AuditLog.AuditAction.HR_APPROVED, approval.getApprovedBy(), 
                approval.getRole(), approval.getComment());

        log.info("Request {} approved by HR. Final status: {}", requestId, newStatus);
        return mapToResponse(saved);
    }

    /**
     * HR reject - explicit method for rejection
     */
    @Transactional
    public LeaveRequestDTO.Response hrReject(UUID requestId, LeaveRequestDTO.ApprovalRequest rejection) {
        log.info("HR {} rejecting request: {}", rejection.getApprovedBy(), requestId);
        
        LeaveRequest request = getLeaveRequestById(requestId);
        
        // STATE MACHINE VALIDATION
        if (request.getStatus() != LeaveStatus.MANAGER_APPROVED) {
            throw new InvalidStateTransitionException(
                    "Request is not waiting for HR approval. Current status: " + request.getStatus());
        }

        LeaveStatus newStatus = LeaveStatus.HR_REJECTED;

        request.setStatus(newStatus);
        request.setHrComment(rejection.getComment());
        request.setApprovedByHR(rejection.getApprovedBy());
        request.setHrApprovalDate(LocalDateTime.now());

        LeaveRequest saved = leaveRequestRepository.save(request);

        createAuditLog(requestId, LeaveStatus.MANAGER_APPROVED, newStatus, 
                AuditLog.AuditAction.HR_REJECTED, rejection.getApprovedBy(), 
                rejection.getRole(), rejection.getComment());

        log.info("Request {} rejected by HR. Final status: {}", requestId, newStatus);
        return mapToResponse(saved);
    }

    /**
     * Get all leave requests with pagination
     */
    @Transactional(readOnly = true)
    public Page<LeaveRequestDTO.Response> getAllRequests(Pageable pageable) {
        log.debug("Fetching all leave requests, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return leaveRequestRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::mapToResponse);
    }

    /**
     * Get all requests waiting for manager approval
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getPendingManagerApproval() {
        log.debug("Fetching requests waiting for manager approval");
        return leaveRequestRepository.findRequestsWaitingForManagerApproval()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Get all requests waiting for HR approval
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getPendingHRApproval() {
        log.debug("Fetching requests waiting for HR approval");
        return leaveRequestRepository.findRequestsWaitingForHRApproval()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Get leave request by ID
     */
    @Transactional(readOnly = true)
    public LeaveRequestDTO.Response getRequestById(UUID id) {
        return mapToResponse(getLeaveRequestById(id));
    }

    /**
     * Get all audit logs for a request
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogs(UUID requestId) {
        return auditLogRepository.findByLeaveRequestIdOrderByCreatedAtAsc(requestId);
    }

    /**
     * Get requests by employee ID
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getRequestsByEmployee(String employeeId) {
        return leaveRequestRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Helper: Get leave request by ID or throw exception
     */
    private LeaveRequest getLeaveRequestById(UUID id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found with ID: " + id));
    }

    /**
     * Helper: Create audit log entry
     */
    private void createAuditLog(UUID leaveRequestId, LeaveStatus previousStatus, 
                               LeaveStatus newStatus, AuditLog.AuditAction action,
                               String performedBy, String role, String comment) {
        AuditLog auditLog = AuditLog.builder()
                .leaveRequestId(leaveRequestId)
                .action(action)
                .previousStatus(previousStatus != null ? previousStatus.name() : null)
                .newStatus(newStatus.name())
                .performedBy(performedBy)
                .role(role)
                .comment(comment)
                .build();
        
        auditLogRepository.save(auditLog);
    }

    /**
     * Helper: Map entity to response DTO
     */
    private LeaveRequestDTO.Response mapToResponse(LeaveRequest request) {
        return LeaveRequestDTO.Response.builder()
                .id(request.getId())
                .employeeName(request.getEmployeeName())
                .employeeId(request.getEmployeeId())
                .department(request.getDepartment())
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(request.getStatus())
                .managerComment(request.getManagerComment())
                .hrComment(request.getHrComment())
                .approvedByManager(request.getApprovedByManager())
                .approvedByHR(request.getApprovedByHR())
                .managerApprovalDate(request.getManagerApprovalDate())
                .hrApprovalDate(request.getHrApprovalDate())
                .totalDays(request.getTotalDays())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}
