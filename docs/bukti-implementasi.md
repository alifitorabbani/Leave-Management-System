# Bukti Implementasi Kode

**Proyek: Workflow Engine Comparison**  
**Perusahaan: PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono**  
**Konteks: Proyek magang — Riset internal workflow orchestration**

---

## 1. Manual Version - Implementasi Kode

### 1.1 Enum Status (State Machine)

**File:** `manual-version/backend/src/main/java/com/len/leave/manual/entity/LeaveStatus.java`

```java
package com.len.leave.manual.entity;

public enum LeaveStatus {
    
    PENDING("Menunggu Approval Manager", "leave.pending"),
    MANAGER_APPROVED("Approved oleh Manager", "leave.manager.approved"),
    MANAGER_REJECTED("Ditolak oleh Manager", "leave.manager.rejected"),
    HR_APPROVED("Approved (Final)", "leave.hr.approved"),
    HR_REJECTED("Ditolak oleh HR", "leave.hr.rejected");

    private final String displayName;
    private final String messageCode;

    LeaveStatus(String displayName, String messageCode) {
        this.displayName = displayName;
        this.messageCode = messageCode;
    }

    public boolean isFinalState() {
        return this == MANAGER_REJECTED || this == HR_APPROVED || this == HR_REJECTED;
    }

    public boolean requiresManagerApproval() {
        return this == PENDING;
    }

    public boolean requiresHRApproval() {
        return this == MANAGER_APPROVED;
    }
}
```

**Penjelasan**: Enum ini mendefinisikan semua kemungkinan status dalam workflow. Setiap status memiliki nama tampilan dan kode pesan untuk internasionalisasi.

### 1.2 If-Else State Transition Logic

**File:** `manual-version/backend/src/main/java/com/len/leave/manual/service/LeaveRequestService.java`

```java
@Transactional
public LeaveRequestDTO.Response managerApproval(UUID requestId, LeaveRequestDTO.ApprovalRequest approval) {
    LeaveRequest request = getLeaveRequestById(requestId);
    
    // STATE MACHINE VALIDATION - If-else logic
    if (request.getStatus() != LeaveStatus.PENDING) {
        throw new InvalidStateTransitionException(
                "Request is not in PENDING status. Current status: " + request.getStatus());
    }

    // Determine new status based on approval decision
    LeaveStatus newStatus = approval.isApproved() 
        ? LeaveStatus.MANAGER_APPROVED 
        : LeaveStatus.MANAGER_REJECTED;
    
    // Update request
    request.setStatus(newStatus);
    request.setManagerComment(approval.getComment());
    request.setApprovedByManager(approval.getApprovedBy());
    request.setManagerApprovalDate(LocalDateTime.now());

    LeaveRequest saved = leaveRequestRepository.save(request);

    // Create audit log
    createAuditLog(requestId, LeaveStatus.PENDING, newStatus, 
            AuditLog.AuditAction.MANAGER_APPROVED, approval.getApprovedBy(), 
            approval.getRole(), approval.getComment());

    return mapToResponse(saved);
}
```

**Penjelasan**: Logika transisi status menggunakan if-else untuk memproses approval atau rejection. Setiap transisi hanya dapat dilakukan dari status tertentu.

### 1.3 Controller - REST API

**File:** `manual-version/backend/src/main/java/com/len/leave/manual/controller/LeaveRequestController.java`

```java
@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> createRequest(
            @Valid @RequestBody LeaveRequestDTO.CreateRequest request) {
        LeaveRequestDTO.Response response = leaveRequestService.createRequest(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Leave request created successfully"));
    }

    @PostMapping("/{id}/manager-approval")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> managerApproval(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveRequestDTO.ApprovalRequest approval) {
        LeaveRequestDTO.Response response = leaveRequestService.managerApproval(id, approval);
        return ResponseEntity.ok(ApiResponse.success(response, "Manager approval processed"));
    }

    @PostMapping("/{id}/hr-approval")
    public ResponseEntity<ApiResponse<LeaveRequestDTO.Response>> hrApproval(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveRequestDTO.ApprovalRequest approval) {
        LeaveRequestDTO.Response response = leaveRequestService.hrApproval(id, approval);
        return ResponseEntity.ok(ApiResponse.success(response, "HR approval processed"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO.Response>>> getAllRequests() {
        List<LeaveRequestDTO.Response> responses = leaveRequestService.getAllRequests();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
```

---

## 2. Flowable Version - Implementasi Kode

### 2.1 RuntimeService Usage

**File:** `flowable-version/backend/src/main/java/com/len/leave/flowable/service/LeaveWorkflowService.java`

```java
private final RuntimeService runtimeService;

@Transactional
public LeaveRequestDTO.Response submitRequest(LeaveRequestDTO.CreateRequest request) {
    // Save leave request to database
    LeaveRequest saved = leaveRequestRepository.save(leaveRequest);

    // Start Flowable process with variables
    Map<String, Object> variables = new HashMap<>();
    variables.put("employeeName", request.getEmployeeName());
    variables.put("leaveType", request.getLeaveType().name());
    variables.put("startDate", request.getStartDate().toString());
    variables.put("endDate", request.getEndDate().toString());
    variables.put("reason", request.getReason());
    variables.put("requestId", saved.getId().toString());

    // Start process instance
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "leaveApproval",  // Process definition key
            saved.getId().toString(),  // Business key
            variables  // Process variables
    );

    // Update with process instance ID
    saved.setWorkflowInstanceId(processInstance.getId());
    leaveRequestRepository.save(saved);

    log.info("Flowable process started with ID: {}", processInstance.getId());
    return mapToResponse(saved);
}
```

**Penjelasan**: `runtimeService.startProcessInstanceByKey()` digunakan untuk memulai proses workflow baru. Process definition key merujuk ke BPMN yang sudah di-deploy.

### 2.2 TaskService Usage

```java
private final TaskService taskService;

@Transactional
public LeaveRequestDTO.Response completeManagerApproval(String taskId, boolean approved, 
                                                      String comment, String approvedBy) {
    // Get task from Flowable
    Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    
    // Set variables for the task
    taskService.setVariable(taskId, "managerApproved", approved);
    taskService.setVariable(taskId, "managerComment", comment);
    taskService.setVariable(taskId, "managerName", approvedBy);

    // Complete the task - this triggers next step in BPMN
    Map<String, Object> processVariables = new HashMap<>();
    processVariables.put("managerApproved", approved);
    processVariables.put("managerComment", comment);
    
    taskService.complete(taskId, processVariables);

    // Update local entity status
    leaveRequest.setStatus(approved ? LeaveStatus.MANAGER_APPROVED : LeaveStatus.MANAGER_REJECTED);
    return mapToResponse(leaveRequestRepository.save(leaveRequest));
}
```

**Penjelasan**: `taskService.complete()` menyelesaikan user task dan memicu transisi ke langkah berikutnya dalam workflow BPMN berdasarkan kondisi yang sudah didefinisikan.

### 2.3 BPMN XML Definition

**File:** `flowable-version/bpmn/leave-approval.bpmn20.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:flowable="http://flowable.org/bpmn"
             id="leaveApprovalDefinitions"
             targetNamespace="http://www.flowable.org/processdef">

    <process id="leaveApproval" name="Leave Approval Process" isExecutable="true">
        
        <!-- Start Event -->
        <startEvent id="startEvent" name="Leave Request Submitted"/>
        
        <!-- Sequence Flow: Start → Manager Approval -->
        <sequenceFlow id="flow1" sourceRef="startEvent" targetRef="managerApprovalTask"/>

        <!-- Manager Approval Task -->
        <userTask id="managerApprovalTask" name="Manager Approval" 
                  flowable:candidateGroups="managers">
        </userTask>

        <!-- Gateway: Manager Decision -->
        <exclusiveGateway id="managerDecisionGateway" name="Manager Decision"/>
        <sequenceFlow id="flow2" sourceRef="managerApprovalTask" targetRef="managerDecisionGateway"/>

        <!-- Conditional Flow: Approve -->
        <sequenceFlow id="flow3" sourceRef="managerDecisionGateway" targetRef="hrApprovalTask">
            <conditionExpression xsi:type="tFormalExpression">
                ${managerApproved == true}
            </conditionExpression>
        </sequenceFlow>

        <!-- Conditional Flow: Reject -->
        <sequenceFlow id="flow4" sourceRef="managerDecisionGateway" targetRef="managerRejectedEnd">
            <conditionExpression xsi:type="tFormalExpression">
                ${managerApproved == false}
            </conditionExpression>
        </sequenceFlow>

        <!-- HR Approval Task -->
        <userTask id="hrApprovalTask" name="HR Approval"
                  flowable:candidateGroups="hr">
        </userTask>

        <!-- Gateway: HR Decision -->
        <exclusiveGateway id="hrDecisionGateway" name="HR Decision"/>
        <sequenceFlow id="flow5" sourceRef="hrApprovalTask" targetRef="hrDecisionGateway"/>

        <!-- HR Approved End -->
        <sequenceFlow id="flow6" sourceRef="hrDecisionGateway" targetRef="hrApprovedEnd">
            <conditionExpression xsi:type="tFormalExpression">
                ${hrApproved == true}
            </conditionExpression>
        </sequenceFlow>

        <!-- HR Rejected End -->
        <sequenceFlow id="flow7" sourceRef="hrDecisionGateway" targetRef="hrRejectedEnd">
            <conditionExpression xsi:type="tFormalExpression">
                ${hrApproved == false}
            </conditionExpression>
        </sequenceFlow>

        <!-- End Events -->
        <endEvent id="managerRejectedEnd" name="Rejected by Manager"/>
        <endEvent id="hrApprovedEnd" name="Approved by HR"/>
        <endEvent id="hrRejectedEnd" name="Rejected by HR"/>

    </process>
</definitions>
```

**Penjelasan**: File BPMN 2.0 mendefinisikan alur workflow dengan user task untuk manager dan HR, serta exclusive gateway untuk keputusan approve/reject.

---

## 3. Deboot Version - Implementasi Kode

### 3.1 Service Implementation

**File:** `deboot-version/backend/src/main/java/com/len/leave/deboot/service/DebootLeaveWorkflowService.java`

```java
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

    /**
     * Submit a new leave request
     * Starts a new Flowable process for leave approval
     */
    @Transactional
    public LeaveRequestDTO.Response submitRequest(LeaveRequestDTO.CreateRequest request) {
        log.info("Starting leave workflow using Flowable");
        
        // Validation
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Save leave request
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
        
        // Start Flowable process directly
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeName", request.getEmployeeName());
        variables.put("leaveType", request.getLeaveType().name());
        variables.put("startDate", request.getStartDate().toString());
        variables.put("endDate", request.getEndDate().toString());
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
        
        // Complete manager task with rejection
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
     * Get pending tasks for manager
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
}
```

**Penjelasan**: Deboot version menggunakan Flowable secara langsung melalui service, tanpa memerlukan definisi BPMN XML. Pendekatan ini lebih sederhana karena semua logika ada di kode Java.

---

## 4. Perbandingan Implementasi

### 4.1 tabel Perbandingan

| Aspek | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| **State Management** | Enum + If-else | BPMN XML | Service Code |
| **Proses Start** | Langsung simpan DB | `runtimeService.startProcessInstanceByKey()` | `runtimeService.startProcessInstanceByKey()` |
| **Task Completion** | Update status manual | `taskService.complete()` | `taskService.complete()` |
| **Task Query** | Database query | `taskService.createTaskQuery()` | `taskService.createTaskQuery()` |
| **Audit Trail** | Manual (tabel audit_logs) | Otomatis (Flowable History) | Otomatis |
| **Kompleksitas Kode** | Tinggi | Medium | Rendah |
| **BPMN File** | ❌ | ✅ | ❌ |
| **Dependency** | Spring Boot only | Flowable 6.8.0 | Flowable 6.8.0 |

### 4.2 Perbandingan LOC

| Versi | Backend LOC | Frontend LOC | Total |
|-------|------------|--------------|-------|
| Manual | 1,731 | 1,364 | 3,095 |
| Flowable | 1,563 | 1,364 | 2,927 |
| Deboot | 1,482 | 1,364 | 2,846 |

---

## 5. Sample Data (DataInitializer)

Setiap versi memiliki DataInitializer yang membuat sample data untuk pengujian:

### 5.1 Distribusi Status pada Sample Data

| Status | Jumlah Request |
|--------|---------------|
| PENDING | 6 |
| MANAGER_APPROVED | 4 |
| HR_APPROVED | 4 |
| MANAGER_REJECTED | 2 |
| HR_REJECTED | 2 |
| **Total** | **18** |

### 5.2 Jenis Cuti pada Sample Data

| Leave Type | Keterangan |
|------------|-------------|
| ANNUAL | Cuti tahunan |
| SICK | Cuti sakit |
| PERSONAL | Cuti pribadi |
| MATERNITY | Cuti melahirkan |

---

## 6. Kesimpulan

Bukti implementasi di atas menunjukkan bahwa:

1. **Manual Version** memerlukan logika if-else yang eksplisit untuk setiap transisi status, dengan kompleksitas kode tertinggi (1,731 LOC)

2. **Flowable Version** mendelegasikan manajemen workflow ke engine BPMN dengan BPMN XML, memberikan visualisasi dan audit trail otomatis

3. **Deboot Version** menggunakan Flowable secara langsung melalui service code tanpa BPMN XML, memberikan keseimbangan antara simplicity dan fungsionalitas

Setiap pendekatan memiliki trade-off antara kompleksitas, fleksibilitas, dan kemudahan maintenance. Pilihan tergantung pada kebutuhan spesifik proyek dan tim.
