package com.len.leave.manual.controller;

import com.len.leave.manual.dto.ApiResponse;
import com.len.leave.manual.dto.LeaveRequestDTO;
import com.len.leave.manual.entity.AuditLog;
import com.len.leave.manual.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Leave Request Controller - REST API
 * 
 * Provides REST endpoints for leave request operations.
 * All endpoints return standardized ApiResponse format.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (Persero)
 */
@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
@Slf4j
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    /**
     * Submit a new leave request
     * 
     * POST /api/leave-requests
     */
    @PostMapping
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> submitRequest(
            @Valid @RequestBody LeaveRequestDTO.CreateRequest request) {
        
        log.info("POST /api/leave-requests - Submitting new leave request");
        
        LeaveRequestDTO.Response response = leaveRequestService.submitRequest(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Leave request submitted successfully", response));
    }

    /**
     * Get all leave requests with pagination
     * 
     * GET /api/leave-requests
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<LeaveRequestDTO.Response>>> getAllRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/leave-requests - Fetching all requests, page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveRequestDTO.Response> requests = leaveRequestService.getAllRequests(pageable);
        
        return ResponseEntity
                .ok(ApiResponse.success(requests));
    }

    /**
     * Get leave request by ID
     * 
     * GET /api/leave-requests/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> getRequestById(@PathVariable UUID id) {
        log.info("GET /api/leave-requests/{} - Fetching request by ID", id);
        
        LeaveRequestDTO.Response response = leaveRequestService.getRequestById(id);
        
        return ResponseEntity
                .ok(ApiResponse.success(response));
    }

    /**
     * Get all requests waiting for manager approval
     * 
     * GET /api/leave-requests/pending-manager
     */
    @GetMapping("/pending-manager")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO.Response>>> getPendingManagerApproval() {
        log.info("GET /api/leave-requests/pending-manager");
        
        List<LeaveRequestDTO.Response> requests = leaveRequestService.getPendingManagerApproval();
        
        return ResponseEntity
                .ok(ApiResponse.success(requests));
    }

    /**
     * Get all requests waiting for HR approval
     * 
     * GET /api/leave-requests/pending-hr
     */
    @GetMapping("/pending-hr")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO.Response>>> getPendingHRApproval() {
        log.info("GET /api/leave-requests/pending-hr");
        
        List<LeaveRequestDTO.Response> requests = leaveRequestService.getPendingHRApproval();
        
        return ResponseEntity
                .ok(ApiResponse.success(requests));
    }

    /**
     * Get requests by employee ID
     * 
     * GET /api/leave-requests/employee/{employeeId}
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO.Response>>> getRequestsByEmployee(
            @PathVariable String employeeId) {
        log.info("GET /api/leave-requests/employee/{}", employeeId);
        
        List<LeaveRequestDTO.Response> requests = leaveRequestService.getRequestsByEmployee(employeeId);
        
        return ResponseEntity
                .ok(ApiResponse.success(requests));
    }

    /**
     * Manager approves a leave request
     * 
     * POST /api/leave-requests/{id}/manager-approve
     */
    @PostMapping("/{id}/manager-approve")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> managerApprove(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveRequestDTO.ApprovalRequest approval) {
        
        log.info("POST /api/leave-requests/{}/manager-approve", id);
        
        LeaveRequestDTO.Response response = leaveRequestService.managerApproval(id, approval);
        
        return ResponseEntity
                .ok(ApiResponse.success("Request approved by manager", response));
    }

    /**
     * Manager rejects a leave request
     * 
     * POST /api/leave-requests/{id}/manager-reject
     */
    @PostMapping("/{id}/manager-reject")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> managerReject(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveRequestDTO.ApprovalRequest rejection) {
        
        log.info("POST /api/leave-requests/{}/manager-reject", id);
        
        LeaveRequestDTO.Response response = leaveRequestService.managerReject(id, rejection);
        
        return ResponseEntity
                .ok(ApiResponse.success("Request rejected by manager", response));
    }

    /**
     * HR approves a leave request
     * 
     * POST /api/leave-requests/{id}/hr-approve
     */
    @PostMapping("/{id}/hr-approve")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> hrApprove(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveRequestDTO.ApprovalRequest approval) {
        
        log.info("POST /api/leave-requests/{}/hr-approve", id);
        
        LeaveRequestDTO.Response response = leaveRequestService.hrApproval(id, approval);
        
        return ResponseEntity
                .ok(ApiResponse.success("Request approved by HR", response));
    }

    /**
     * HR rejects a leave request
     * 
     * POST /api/leave-requests/{id}/hr-reject
     */
    @PostMapping("/{id}/hr-reject")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> hrReject(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveRequestDTO.ApprovalRequest rejection) {
        
        log.info("POST /api/leave-requests/{}/hr-reject", id);
        
        LeaveRequestDTO.Response response = leaveRequestService.hrReject(id, rejection);
        
        return ResponseEntity
                .ok(ApiResponse.success("Request rejected by HR", response));
    }

    /**
     * Get audit logs for a leave request
     * 
     * GET /api/leave-requests/{id}/audit-logs
     */
    @GetMapping("/{id}/audit-logs")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs(@PathVariable UUID id) {
        log.info("GET /api/leave-requests/{}/audit-logs", id);
        
        List<AuditLog> auditLogs = leaveRequestService.getAuditLogs(id);
        
        return ResponseEntity
                .ok(ApiResponse.success(auditLogs));
    }
}
