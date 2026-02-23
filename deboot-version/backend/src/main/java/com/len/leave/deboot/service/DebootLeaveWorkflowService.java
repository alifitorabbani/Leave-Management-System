package com.len.leave.deboot.service;

import com.len.leave.deboot.dto.LeaveRequestDTO;
import com.len.leave.deboot.entity.AuditLog;
import com.len.leave.deboot.entity.LeaveRequest;
import com.len.leave.deboot.entity.LeaveStatus;
import com.len.leave.deboot.repository.AuditLogRepository;
import com.len.leave.deboot.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Deboot Leave Workflow Service
 * 
 * This service implements the workflow using Flowable BPM Engine.
 * Deboot is implemented as a wrapper around Flowable for simplified workflow management.
 * 
 * Key features:
 * - Uses Flowable RuntimeService for process management
 * - Uses TaskService for user task handling
 * - Uses HistoryService for audit trail
 * - Process definitions in BPMN 2.0 XML
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DebootLeaveWorkflowService {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AuditLogRepository auditLogRepository;

    private static final String PROCESS_DEFINITION_KEY = "leaveApproval";
    private static final String PROCESS_VAR_EMPLOYEE_NAME = "employeeName";
    private static final String PROCESS_VAR_LEAVE_TYPE = "leaveType";
    private static final String PROCESS_VAR_START_DATE = "startDate";
    private static final String PROCESS_VAR_END_DATE = "endDate";

    /**
     * Submit a new leave request
     * 
     * Starts a new Flowable process for leave approval
     */
    @Transactional
    public LeaveRequestDTO.Response submitRequest(LeaveRequestDTO.CreateRequest request) {
        log.info("Starting leave workflow using Flowable");
        
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

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
        log.info("Leave request saved with ID: {}", saved.getId());
        
        // Start Flowable process
        Map<String, Object> variables = new HashMap<>();
        variables.put(PROCESS_VAR_EMPLOYEE_NAME, request.getEmployeeName());
        variables.put(PROCESS_VAR_LEAVE_TYPE, request.getLeaveType().name());
        variables.put(PROCESS_VAR_START_DATE, request.getStartDate().toString());
        variables.put(PROCESS_VAR_END_DATE, request.getEndDate().toString());
        variables.put("requestId", saved.getId().toString());
        
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                PROCESS_DEFINITION_KEY, 
                saved.getId().toString(), 
                variables
        );
        
        // Update with process instance ID
        saved.setWorkflowInstanceId(processInstance.getId());
        leaveRequestRepository.save(saved);
        
        log.info("Process started with ID: {}", processInstance.getId());

        return mapToResponse(saved);
    }

    /**
     * Get all pending tasks for manager approval
     */
    public List<LeaveRequestDTO.Response> getManagerPendingTasks() {
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("manager")
                .processDefinitionKey(PROCESS_DEFINITION_KEY)
                .list();
        
        List<LeaveRequestDTO.Response> result = new ArrayList<>();
        for (Task task : tasks) {
            String requestId = (String) taskService.getVariable(task.getId(), "requestId");
            LeaveRequest leaveRequest = leaveRequestRepository.findById(UUID.fromString(requestId)).orElse(null);
            if (leaveRequest != null) {
                result.add(mapToResponse(leaveRequest));
            }
        }
        return result;
    }

    /**
     * Get all pending tasks for HR approval
     */
    public List<LeaveRequestDTO.Response> getHRPendingTasks() {
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("hr")
                .processDefinitionKey(PROCESS_DEFINITION_KEY)
                .list();
        
        List<LeaveRequestDTO.Response> result = new ArrayList<>();
        for (Task task : tasks) {
            String requestId = (String) taskService.getVariable(task.getId(), "requestId");
            LeaveRequest leaveRequest = leaveRequestRepository.findById(UUID.fromString(requestId)).orElse(null);
            if (leaveRequest != null) {
                result.add(mapToResponse(leaveRequest));
            }
        }
        return result;
    }

    /**
     * Approve by manager
     */
    @Transactional
    public LeaveRequestDTO.Response approveByManager(UUID requestId, LeaveRequestDTO.ApprovalRequest approvalRequest) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Request is not in pending status");
        }
        
        // Complete manager task
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("manager")
                .processInstanceId(leaveRequest.getWorkflowInstanceId())
                .list();
        
        for (Task task : tasks) {
            taskService.complete(task.getId(), Map.of("approved", true, "comment", approvalRequest.getComment()));
        }
        
        // Update status
        leaveRequest.setStatus(LeaveStatus.MANAGER_APPROVED);
        leaveRequest.setManagerComment(approvalRequest.getComment());
        leaveRequest.setApprovedByManager(approvalRequest.getApprovedBy());
        leaveRequest.setManagerApprovalDate(LocalDateTime.now());
        
        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        log.info("Leave request {} approved by manager", requestId);
        
        return mapToResponse(saved);
    }

    /**
     * Reject by manager
     */
    @Transactional
    public LeaveRequestDTO.Response rejectByManager(UUID requestId, LeaveRequestDTO.ApprovalRequest approvalRequest) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Request is not in pending status");
        }
        
        // Complete manager task
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("manager")
                .processInstanceId(leaveRequest.getWorkflowInstanceId())
                .list();
        
        for (Task task : tasks) {
            taskService.complete(task.getId(), Map.of("approved", false, "comment", approvalRequest.getComment()));
        }
        
        // Update status
        leaveRequest.setStatus(LeaveStatus.MANAGER_REJECTED);
        leaveRequest.setManagerComment(approvalRequest.getComment());
        leaveRequest.setApprovedByManager(approvalRequest.getApprovedBy());
        leaveRequest.setManagerApprovalDate(LocalDateTime.now());
        
        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        log.info("Leave request {} rejected by manager", requestId);
        
        return mapToResponse(saved);
    }

    /**
     * Approve by HR
     */
    @Transactional
    public LeaveRequestDTO.Response approveByHR(UUID requestId, LeaveRequestDTO.ApprovalRequest approvalRequest) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        if (leaveRequest.getStatus() != LeaveStatus.MANAGER_APPROVED) {
            throw new IllegalStateException("Request is not approved by manager yet");
        }
        
        // Complete HR task
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("hr")
                .processInstanceId(leaveRequest.getWorkflowInstanceId())
                .list();
        
        for (Task task : tasks) {
            taskService.complete(task.getId(), Map.of("hrApproved", true, "comment", approvalRequest.getComment()));
        }
        
        // Update status
        leaveRequest.setStatus(LeaveStatus.HR_APPROVED);
        leaveRequest.setHrComment(approvalRequest.getComment());
        leaveRequest.setApprovedByHR(approvalRequest.getApprovedBy());
        leaveRequest.setHrApprovalDate(LocalDateTime.now());
        
        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        log.info("Leave request {} approved by HR", requestId);
        
        return mapToResponse(saved);
    }

    /**
     * Reject by HR
     */
    @Transactional
    public LeaveRequestDTO.Response rejectByHR(UUID requestId, LeaveRequestDTO.ApprovalRequest approvalRequest) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        if (leaveRequest.getStatus() != LeaveStatus.MANAGER_APPROVED) {
            throw new IllegalStateException("Request is not approved by manager yet");
        }
        
        // Complete HR task
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("hr")
                .processInstanceId(leaveRequest.getWorkflowInstanceId())
                .list();
        
        for (Task task : tasks) {
            taskService.complete(task.getId(), Map.of("hrApproved", false, "comment", approvalRequest.getComment()));
        }
        
        // Update status
        leaveRequest.setStatus(LeaveStatus.HR_REJECTED);
        leaveRequest.setHrComment(approvalRequest.getComment());
        leaveRequest.setApprovedByHR(approvalRequest.getApprovedBy());
        leaveRequest.setHrApprovalDate(LocalDateTime.now());
        
        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        log.info("Leave request {} rejected by HR", requestId);
        
        return mapToResponse(saved);
    }

    /**
     * Get pending requests for manager approval
     */
    public List<LeaveRequestDTO.Response> getPendingManagerApproval() {
        log.debug("Fetching pending requests for manager approval");
        return leaveRequestRepository.findRequestsWaitingForManagerApproval().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Get pending requests for HR approval
     */
    public List<LeaveRequestDTO.Response> getPendingHRApproval() {
        log.debug("Fetching pending requests for HR approval");
        return leaveRequestRepository.findRequestsWaitingForHRApproval().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Get audit logs for a leave request
     */
    public List<AuditLog> getAuditLogs(UUID leaveRequestId) {
        log.debug("Fetching audit logs for leave request: {}", leaveRequestId);
        return auditLogRepository.findByLeaveRequestIdOrderByCreatedAtAsc(leaveRequestId);
    }

    /**
     * Get all leave requests
     */
    public List<LeaveRequestDTO.Response> getAllRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Get request by ID
     */
    public LeaveRequestDTO.Response getRequestById(UUID id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        return mapToResponse(leaveRequest);
    }

    private LeaveRequestDTO.Response mapToResponse(LeaveRequest leaveRequest) {
        return LeaveRequestDTO.Response.builder()
                .id(leaveRequest.getId())
                .employeeName(leaveRequest.getEmployeeName())
                .employeeId(leaveRequest.getEmployeeId())
                .department(leaveRequest.getDepartment())
                .leaveType(leaveRequest.getLeaveType())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .reason(leaveRequest.getReason())
                .status(leaveRequest.getStatus())
                .managerComment(leaveRequest.getManagerComment())
                .hrComment(leaveRequest.getHrComment())
                .approvedByManager(leaveRequest.getApprovedByManager())
                .approvedByHR(leaveRequest.getApprovedByHR())
                .managerApprovalDate(leaveRequest.getManagerApprovalDate())
                .hrApprovalDate(leaveRequest.getHrApprovalDate())
                .totalDays(leaveRequest.getTotalDays())
                .workflowInstanceId(leaveRequest.getWorkflowInstanceId())
                .createdAt(leaveRequest.getCreatedAt())
                .updatedAt(leaveRequest.getUpdatedAt())
                .build();
    }
}
