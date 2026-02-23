# Bukti Implementasi Kode

**Proyek: Workflow Engine Comparison**  
**Perusahaan: PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono**  
**Konteks: Proyek magang — Riset internal workflow orchestration**

---

## 1. Manual Version - Implementasi Kode

### 1.1 Enum Status (State Machine)

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
}
```

**Penjelasan**: Enum ini mendefinisikan semua kemungkinan status dalam workflow. Setiap status memiliki nama tampilan dan kode pesan untuk internacionalisasi.

### 1.2 If-Else State Transition Logic

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
    LeaveStatus newStatus = LeaveStatus.MANAGER_APPROVED;
    
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

**Penjelasan**: Logika transisi status menggunakan if-else untuk memvalidasi apakah transisi yang diminta valid. Setiap transisi hanya dapat dilakukan dari status tertentu.

---

## 2. Flowable Version - Implementasi Kode

### 2.1 RuntimeService Usage

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
    variables.put("leaveRequestId", saved.getId().toString());

    // Start process instance
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "leaveApproval",  // Process definition key
            saved.getId().toString(),  // Business key
            variables  // Process variables
    );

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

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:flowable="http://flowable.org/bpmn"
             id="leaveApprovalDefinitions"
             targetNamespace="http://www.flowable.org/processdef">

    <process id="leaveApproval" name="Leave Approval Process" isExecutable="true">
        
        <!-- Start Event -->
        <startEvent id="startEvent" name="Leave Request Submitted"/>
        
        <!-- Manager Approval Task -->
        <userTask id="managerApprovalTask" name="Manager Approval" 
                  flowable:candidateGroups="managers">
        </userTask>

        <!-- Exclusive Gateway for Decision -->
        <exclusiveGateway id="managerDecisionGateway" name="Manager Decision"/>

        <!-- Conditional Flow -->
        <sequenceFlow id="flow3" sourceRef="managerDecisionGateway" targetRef="hrApprovalTask">
            <conditionExpression xsi:type="tFormalExpression">
                ${managerApproved == true}
            </conditionExpression>
        </sequenceFlow>

        <sequenceFlow id="flow4" sourceRef="managerDecisionGateway" targetRef="managerRejectedEnd">
            <conditionExpression xsi:type="tFormalExpression">
                ${managerApproved == false}
            </conditionExpression>
        </sequenceFlow>

        <!-- HR Approval Task -->
        <userTask id="hrApprovalTask" name="HR Approval"
                  flowable:candidateGroups="hr">
        </userTask>

        <!-- End Events -->
        <endEvent id="hrApprovedEnd" name="Approved by HR"/>
        <endEvent id="hrRejectedEnd" name="Rejected by HR"/>

    </process>
</definitions>
```

**Penjelasan**: File BPMN 2.0 mendefinisikan alur workflow dengan user task untuk manager dan HR, serta exclusive gateway untuk keputusan approve/reject.

---

## 3. Deboot Version - Implementasi Kode

### 3.1 Deboot Annotation Example

```java
package com.len.leave.deboot.service;

import com.deboot.annotation.*;

@Service
@RequiredArgsConstructor
@Slf4j
@WorkflowDefinition(
    key = "leaveApprovalDeboot",
    name = "Leave Approval Process",
    description = "Leave request approval workflow with Deboot"
)
public class DebootLeaveWorkflowService {

    /**
     * Submit request with @WorkflowStart annotation
     * Deboot handles process instantiation automatically
     */
    @Transactional
    @WorkflowStart(
        variables = {
            @VariableMapping(name = "employeeName", source = "employeeName"),
            @VariableMapping(name = "leaveType", source = "leaveType"),
            @VariableMapping(name = "startDate", source = "startDate"),
            @VariableMapping(name = "endDate", source = "endDate"),
            @VariableMapping(name = "reason", source = "reason")
        }
    )
    public LeaveRequestDTO.Response submitRequest(LeaveRequestDTO.CreateRequest request) {
        // Save to database
        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        
        // Deboot automatically starts the Flowable process
        log.info("Leave request saved, Deboot handles BPM process start");
        
        return mapToResponse(saved);
    }

    /**
     * Manager approval with @WorkflowTask annotation
     * Deboot automatically handles task assignment and completion
     */
    @Transactional
    @WorkflowTask(
        taskName = "managerApproval",
        candidateGroup = "managers",
        variables = {
            @VariableMapping(name = "approved", source = "approved"),
            @VariableMapping(name = "comment", source = "comment"),
            @VariableMapping(name = "approvedBy", source = "approvedBy")
        }
    )
    public LeaveRequestDTO.Response completeManagerApproval(UUID requestId, boolean approved, 
                                                         String comment, String approvedBy) {
        // Update status
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(approved ? LeaveStatus.MANAGER_APPROVED : LeaveStatus.MANAGER_REJECTED);
        
        // Deboot automatically completes the task in Flowable
        log.info("Manager approval processed by Deboot");
        
        return mapToResponse(leaveRequestRepository.save(request));
    }
}
```

**Penjelasan**: Deboot menggunakan annotations untuk mendefinisikan workflow tanpa perlu menulis BPMN XML secara manual. Annotations `@WorkflowDefinition`, `@WorkflowStart`, dan `@WorkflowTask` menyederhanakan konfigurasi Flowable.

---

## 4. Perbandingan Implementasi

| Aspek | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| **State Management** | Enum + If-else | BPMN XML | Annotations |
| **Proses Start** | Langsung simpan DB | `runtimeService.startProcessInstanceByKey()` | `@WorkflowStart` |
| **Task Completion** | Update status manual | `taskService.complete()` | `@WorkflowTask` |
| **Audit Trail** | Manual (tabel audit_logs) | Otomatis (Flowable History) | Otomatis |
| **Kompleksitas Kode** | Tinggi | Medium | Rendah |

---

## 5. Kesimpulan

Bukti implementasi di atas menunjukkan bahwa:

1. **Manual Version** memerlukan logika if-else yang eksplisit untuk setiap transisi status
2. **Flowable Version** mendelegasikan manajemen workflow ke engine BPMN dengan BPMN XML
3. **Deboot Version** menyederhanakan Flowable dengan annotation-driven approach

Setiap pendekatan memiliki trade-off antara kompleksitas, fleksibilitas, dan kemudahan maintenance.
