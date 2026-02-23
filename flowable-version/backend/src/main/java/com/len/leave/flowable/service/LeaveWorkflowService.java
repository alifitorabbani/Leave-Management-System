package com.len.leave.flowable.service;

import com.len.leave.flowable.dto.LeaveRequestDTO;
import com.len.leave.flowable.entity.AuditLog;
import com.len.leave.flowable.entity.LeaveRequest;
import com.len.leave.flowable.entity.LeaveStatus;
import com.len.leave.flowable.entity.LeaveType;
import com.len.leave.flowable.repository.AuditLogRepository;
import com.len.leave.flowable.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Leave Workflow Service - Flowable BPM Implementation
 * 
 * This service implements the workflow using Flowable BPM Engine.
 * Key differences from manual version:
 * - Uses RuntimeService to start/process workflows
 * - Uses TaskService to manage user tasks
 * - Uses HistoryService for audit trail
 * - Process definitions in BPMN 2.0 XML
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (Persero)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveWorkflowService {

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
    private static final String PROCESS_VAR_REASON = "reason";
    private static final String PROCESS_VAR_EMPLOYEE_ID = "employeeId";
    private static final String PROCESS_VAR_DEPARTMENT = "department";

    /**
     * Start a new leave approval workflow using Flowable
     * 
     * This creates a new process instance in Flowable engine.
     */
    @Transactional
    public LeaveRequestDTO.Response submitRequest(LeaveRequestDTO.CreateRequest request) {
        log.info("Starting new leave workflow using Flowable BPM Engine");
        
        // Validate dates
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Create leave request entity
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

        // Start Flowable process
        Map<String, Object> variables = new HashMap<>();
        variables.put(PROCESS_VAR_EMPLOYEE_NAME, request.getEmployeeName());
        variables.put(PROCESS_VAR_LEAVE_TYPE, request.getLeaveType().name());
        variables.put(PROCESS_VAR_START_DATE, request.getStartDate().toString());
        variables.put(PROCESS_VAR_END_DATE, request.getEndDate().toString());
        variables.put(PROCESS_VAR_REASON, request.getReason());
        variables.put(PROCESS_VAR_EMPLOYEE_ID, request.getEmployeeId());
        variables.put(PROCESS_VAR_DEPARTMENT, request.getDepartment());
        variables.put("leaveRequestId", saved.getId().toString());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                PROCESS_DEFINITION_KEY,
                saved.getId().toString(),
                variables
        );

        log.info("Flowable process started with ID: {}", processInstance.getId());
        
        return mapToResponse(saved);
    }

    /**
     * Get tasks assigned to manager (manager approval step)
     */
    @Transactional(readOnly = true)
    public List<Task> getManagerTasks() {
        log.debug("Fetching tasks for manager approval");
        return taskService.createTaskQuery()
                .taskCandidateGroup("managers")
                .orderByTaskCreateTime().desc()
                .list();
    }

    /**
     * Get tasks assigned to HR (HR approval step)
     */
    @Transactional(readOnly = true)
    public List<Task> getHRTasks() {
        log.debug("Fetching tasks for HR approval");
        return taskService.createTaskQuery()
                .taskCandidateGroup("hr")
                .orderByTaskCreateTime().desc()
                .list();
    }

    /**
     * Complete manager approval task
     */
    @Transactional
    public LeaveRequestDTO.Response completeManagerApproval(String taskId, boolean approved, 
                                                           String comment, String approvedBy) {
        log.info("Completing manager approval task: {}, approved: {}", taskId, approved);
        
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("Task not found: " + taskId);
        }

        // Get leave request ID from process variables
        String leaveRequestId = (String) taskService.getVariable(taskId, "leaveRequestId");
        LeaveRequest leaveRequest = leaveRequestRepository.findById(UUID.fromString(leaveRequestId))
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        // Set task variables
        taskService.setVariable(taskId, "managerApproved", approved);
        taskService.setVariable(taskId, "managerComment", comment);
        taskService.setVariable(taskId, "managerName", approvedBy);

        // Complete the task - this will trigger the flow based on approved variable
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("managerApproved", approved);
        processVariables.put("managerComment", comment);
        
        taskService.complete(taskId, processVariables);

        // Update leave request status
        if (approved) {
            leaveRequest.setStatus(LeaveStatus.MANAGER_APPROVED);
        } else {
            leaveRequest.setStatus(LeaveStatus.MANAGER_REJECTED);
        }
        leaveRequest.setManagerComment(comment);
        leaveRequest.setApprovedByManager(approvedBy);
        leaveRequest.setManagerApprovalDate(LocalDateTime.now());

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        
        log.info("Manager approval completed. New status: {}", saved.getStatus());
        return mapToResponse(saved);
    }

    /**
     * Complete HR approval task
     */
    @Transactional
    public LeaveRequestDTO.Response completeHRApproval(String taskId, boolean approved,
                                                      String comment, String approvedBy) {
        log.info("Completing HR approval task: {}, approved: {}", taskId, approved);
        
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("Task not found: " + taskId);
        }

        String leaveRequestId = (String) taskService.getVariable(taskId, "leaveRequestId");
        LeaveRequest leaveRequest = leaveRequestRepository.findById(UUID.fromString(leaveRequestId))
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("hrApproved", approved);
        processVariables.put("hrComment", comment);
        
        taskService.complete(taskId, processVariables);

        if (approved) {
            leaveRequest.setStatus(LeaveStatus.HR_APPROVED);
        } else {
            leaveRequest.setStatus(LeaveStatus.HR_REJECTED);
        }
        leaveRequest.setHrComment(comment);
        leaveRequest.setApprovedByHR(approvedBy);
        leaveRequest.setHrApprovalDate(LocalDateTime.now());

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        
        log.info("HR approval completed. New status: {}", saved.getStatus());
        return mapToResponse(saved);
    }

    /**
     * Get all leave requests
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getAllRequests() {
        return leaveRequestRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Get leave request by ID
     */
    @Transactional(readOnly = true)
    public LeaveRequestDTO.Response getRequestById(UUID id) {
        return mapToResponse(leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found")));
    }

    /**
     * Get process instance details for a leave request
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getProcessInfo(UUID leaveRequestId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(leaveRequestId.toString())
                .singleResult();
        
        Map<String, Object> info = new HashMap<>();
        if (processInstance != null) {
            info.put("processId", processInstance.getId());
            info.put("processDefinitionId", processInstance.getProcessDefinitionId());
            info.put("startedAt", processInstance.getStartTime());
            info.put("isActive", processInstance.isSuspended());
        }
        
        return info;
    }

    /**
     * Get task details for a specific process
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * Get pending requests that need manager approval
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getPendingManagerApproval() {
        log.debug("Fetching pending requests for manager approval");
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("managers")
                .orderByTaskCreateTime().desc()
                .list();
        
        return tasks.stream()
                .map(task -> {
                    String leaveRequestId = (String) taskService.getVariable(task.getId(), "leaveRequestId");
                    return leaveRequestRepository.findById(UUID.fromString(leaveRequestId))
                            .map(this::mapToResponse)
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Get pending requests that need HR approval
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getPendingHRApproval() {
        log.debug("Fetching pending requests for HR approval");
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("hr")
                .orderByTaskCreateTime().desc()
                .list();
        
        return tasks.stream()
                .map(task -> {
                    String leaveRequestId = (String) taskService.getVariable(task.getId(), "leaveRequestId");
                    return leaveRequestRepository.findById(UUID.fromString(leaveRequestId))
                            .map(this::mapToResponse)
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Approve leave request by manager using UUID
     */
    @Transactional
    public LeaveRequestDTO.Response managerApprove(UUID id, LeaveRequestDTO.ApprovalRequest approval) {
        log.info("Manager approving leave request: {}", id);
        
        // Find the task for this leave request
        Task task = taskService.createTaskQuery()
                .taskCandidateGroup("managers")
                .or()
                    .taskVariableValueEquals("leaveRequestId", id.toString())
                .endOr()
                .singleResult();
        
        if (task == null) {
            throw new RuntimeException("No pending task found for leave request: " + id);
        }
        
        return completeManagerApproval(task.getId(), true, approval.getComment(), approval.getApprovedBy());
    }

    /**
     * Reject leave request by manager using UUID
     */
    @Transactional
    public LeaveRequestDTO.Response managerReject(UUID id, LeaveRequestDTO.ApprovalRequest approval) {
        log.info("Manager rejecting leave request: {}", id);
        
        Task task = taskService.createTaskQuery()
                .taskCandidateGroup("managers")
                .or()
                    .taskVariableValueEquals("leaveRequestId", id.toString())
                .endOr()
                .singleResult();
        
        if (task == null) {
            throw new RuntimeException("No pending task found for leave request: " + id);
        }
        
        return completeManagerApproval(task.getId(), false, approval.getComment(), approval.getApprovedBy());
    }

    /**
     * Approve leave request by HR using UUID
     */
    @Transactional
    public LeaveRequestDTO.Response hrApprove(UUID id, LeaveRequestDTO.ApprovalRequest approval) {
        log.info("HR approving leave request: {}", id);
        
        Task task = taskService.createTaskQuery()
                .taskCandidateGroup("hr")
                .or()
                    .taskVariableValueEquals("leaveRequestId", id.toString())
                .endOr()
                .singleResult();
        
        if (task == null) {
            throw new RuntimeException("No pending task found for leave request: " + id);
        }
        
        return completeHRApproval(task.getId(), true, approval.getComment(), approval.getApprovedBy());
    }

    /**
     * Reject leave request by HR using UUID
     */
    @Transactional
    public LeaveRequestDTO.Response hrReject(UUID id, LeaveRequestDTO.ApprovalRequest approval) {
        log.info("HR rejecting leave request: {}", id);
        
        Task task = taskService.createTaskQuery()
                .taskCandidateGroup("hr")
                .or()
                    .taskVariableValueEquals("leaveRequestId", id.toString())
                .endOr()
                .singleResult();
        
        if (task == null) {
            throw new RuntimeException("No pending task found for leave request: " + id);
        }
        
        return completeHRApproval(task.getId(), false, approval.getComment(), approval.getApprovedBy());
    }

    /**
     * Get audit logs for a leave request
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogs(UUID leaveRequestId) {
        log.debug("Fetching audit logs for leave request: {}", leaveRequestId);
        return auditLogRepository.findByLeaveRequestIdOrderByCreatedAtAsc(leaveRequestId);
    }

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
