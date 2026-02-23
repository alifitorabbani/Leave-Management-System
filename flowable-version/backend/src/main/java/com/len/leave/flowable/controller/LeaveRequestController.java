package com.len.leave.flowable.controller;

import com.len.leave.flowable.dto.ApiResponse;
import com.len.leave.flowable.dto.LeaveRequestDTO;
import com.len.leave.flowable.entity.AuditLog;
import com.len.leave.flowable.service.LeaveWorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
@Slf4j
public class LeaveRequestController {

    private final LeaveWorkflowService leaveWorkflowService;

    @PostMapping
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> submitRequest(
            @RequestBody LeaveRequestDTO.CreateRequest request) {
        log.info("POST /api/leave-requests - Submitting new leave request");
        LeaveRequestDTO.Response response = leaveWorkflowService.submitRequest(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Leave request submitted successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO.Response>>> getAllRequests() {
        log.info("GET /api/leave-requests - Fetching all requests");
        List<LeaveRequestDTO.Response> requests = leaveWorkflowService.getAllRequests();
        return ResponseEntity.ok(ApiResponse.success(requests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> getRequestById(@PathVariable UUID id) {
        log.info("GET /api/leave-requests/{}", id);
        LeaveRequestDTO.Response response = leaveWorkflowService.getRequestById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/pending-manager")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO.Response>>> getPendingManagerApproval() {
        log.info("GET /api/leave-requests/pending-manager");
        List<LeaveRequestDTO.Response> requests = leaveWorkflowService.getPendingManagerApproval();
        return ResponseEntity.ok(ApiResponse.success(requests));
    }

    @GetMapping("/pending-hr")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO.Response>>> getPendingHRApproval() {
        log.info("GET /api/leave-requests/pending-hr");
        List<LeaveRequestDTO.Response> requests = leaveWorkflowService.getPendingHRApproval();
        return ResponseEntity.ok(ApiResponse.success(requests));
    }

    @PostMapping("/{id}/manager-approve")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> managerApprove(
            @PathVariable UUID id,
            @RequestBody LeaveRequestDTO.ApprovalRequest approval) {
        log.info("POST /api/leave-requests/{}/manager-approve", id);
        LeaveRequestDTO.Response response = leaveWorkflowService.managerApprove(id, approval);
        return ResponseEntity.ok(ApiResponse.success("Request approved by manager", response));
    }

    @PostMapping("/{id}/manager-reject")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> managerReject(
            @PathVariable UUID id,
            @RequestBody LeaveRequestDTO.ApprovalRequest rejection) {
        log.info("POST /api/leave-requests/{}/manager-reject", id);
        LeaveRequestDTO.Response response = leaveWorkflowService.managerReject(id, rejection);
        return ResponseEntity.ok(ApiResponse.success("Request rejected by manager", response));
    }

    @PostMapping("/{id}/hr-approve")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> hrApprove(
            @PathVariable UUID id,
            @RequestBody LeaveRequestDTO.ApprovalRequest approval) {
        log.info("POST /api/leave-requests/{}/hr-approve", id);
        LeaveRequestDTO.Response response = leaveWorkflowService.hrApprove(id, approval);
        return ResponseEntity.ok(ApiResponse.success("Request approved by HR", response));
    }

    @PostMapping("/{id}/hr-reject")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> hrReject(
            @PathVariable UUID id,
            @RequestBody LeaveRequestDTO.ApprovalRequest rejection) {
        log.info("POST /api/leave-requests/{}/hr-reject", id);
        LeaveRequestDTO.Response response = leaveWorkflowService.hrReject(id, rejection);
        return ResponseEntity.ok(ApiResponse.success("Request rejected by HR", response));
    }

    @GetMapping("/{id}/audit-logs")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs(@PathVariable UUID id) {
        log.info("GET /api/leave-requests/{}/audit-logs", id);
        List<AuditLog> auditLogs = leaveWorkflowService.getAuditLogs(id);
        return ResponseEntity.ok(ApiResponse.success(auditLogs));
    }
}
