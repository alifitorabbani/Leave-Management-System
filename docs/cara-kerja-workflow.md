# Cara Kerja Workflow

**Proyek: Workflow Engine Comparison**  
**Perusahaan: PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono**  
**Konteks: Proyek magang — Riset internal workflow orchestration**

---

## 1. Pendahuluan

Dokumen ini menjelaskan cara kerja sistem workflow pada masing-masing versi implementasi. Meskipun semua versi mengimplementasikan use case yang sama (sistem pengajuan cuti dengan approval manager dan HR), pendekatan teknisnya berbeda-beda.

---

## 2. Manual Version - Cara Kerja

### 2.1 Alur Kerja

```
Employee                    Manager                        HR
   │                          │                             │
   ├── Submit Request ───────►│                             │
   │                          │                             │
   │                     [PENDING]                         │
   │                          │                             │
   │                     Review & Decide                   │
   │                          │                             │
   ├──◄── [APPROVED] ────────┤                             │
   │                          ├── Submit to HR ──────────► │
   │                          │                             │
   │                     [MANAGER_APPROVED]               │
   │                          │                        Review & Decide
   │                          │                             │
   ├──◄────────── [APPROVED] ─┤◄─────────────────────────┤
   │                          │                        [HR_APPROVED]
```

### 2.2 State Management

Status workflow dikelola melalui enum `LeaveStatus`:

1. **Initial State**: `PENDING` - Request dibuat dan menunggu manager approval
2. **Manager Decision**:
   - Jika approve: status berubah ke `MANAGER_APPROVED`
   - Jika reject: status berubah ke `MANAGER_REJECTED` (final)
3. **HR Decision**:
   - Jika approve: status berubah ke `HR_APPROVED` (final)
   - Jika reject: status berubah ke `HR_REJECTED` (final)

### 2.3 Implementasi

State management dilakukan dengan:
- **Enum LeaveStatus**: Mendefinisikan semua status
- **If-else logic**: Validasi transisi yang diizinkan di Service layer
- **Service layer**: Memproses perubahan status

### 2.4 Service Logic

```java
// Submit request - creates new request with PENDING status
public LeaveRequestDTO.Response createRequest(LeaveRequestDTO.CreateRequest request) {
    LeaveRequest leaveRequest = LeaveRequest.builder()
        .status(LeaveStatus.PENDING)
        // ... other fields
        .build();
    return mapToResponse(leaveRequestRepository.save(leaveRequest));
}

// Manager approval - validates current state before transition
public LeaveRequestDTO.Response managerApproval(UUID requestId, ApprovalRequest approval) {
    LeaveRequest request = getLeaveRequestById(requestId);
    
    // State machine validation
    if (request.getStatus() != LeaveStatus.PENDING) {
        throw new InvalidStateTransitionException("...");
    }
    
    // Determine new status
    LeaveStatus newStatus = approval.isApproved() 
        ? LeaveStatus.MANAGER_APPROVED 
        : LeaveStatus.MANAGER_REJECTED;
    
    request.setStatus(newStatus);
    // ... update other fields
    return mapToResponse(leaveRequestRepository.save(request));
}
```

---

## 3. Flowable Version - Cara Kerja

### 3.1 Arsitektur Runtime

Flowable menggunakan beberapa service utama:

| Service | Fungsi |
|---------|--------|
| `RuntimeService` | Memulai dan mengelola proses |
| `TaskService` | Mengelola user tasks |
| `HistoryService` | Mengambil data historis |
| `RepositoryService` | Mengelola deployment BPMN |

### 3.2 Process Instance Lifecycle

```
┌─────────────┐
│   START     │
│  (Event)    │
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│  Manager Approval  │
│    (UserTask)      │
└──────────┬──────────┘
           │
    ┌──────┴──────┐
    ▼             ▼
┌─────────┐   ┌─────────┐
│ Approve │   │ Reject  │
│ (Gate)  │   │ (Gate)  │
└────┬────┘   └────┬────┘
     │             │
     ▼             ▼
┌────────────┐  ┌──────────┐
│HR Approval │  │   END    │
│ (UserTask) │  │ (Rejected)│
└─────┬──────┘  └──────────┘
      │
┌─────┴─────┐
▼           ▼
┌─────┐ ┌──────┐
│ END │ │ END  │
│(OK) │ │(Reject)
└─────┘ └──────┘
```

### 3.3 Runtime vs Process Definition

**Process Definition**
- Template/workflow yang di-deploy ke Flowable
- Tidak berubah selama runtime
- Disimpan di tabel `ACT_RE_PROCDEF`
- Didefinisikan dalam BPMN 2.0 XML

**Process Instance**
- Eksekusi dari process definition
- Setiap pengajuan cuti = instance baru
- Disimpan di tabel `ACT_RU_EXECUTION`
- Memiliki unique process instance ID

### 3.4 State Management dengan Flowable

Status workflow disimpan sebagai:
1. **Process Variables**: `managerApproved`, `hrApproved`, dll
2. **Task Variables**: Komentar, approver name
3. **Historic Data**: Semua aktivitas direkam otomatis

### 3.5 Contoh Penggunaan Service

```java
// Start process
ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
    "leaveApproval",      // Process definition key
    leaveRequest.getId().toString(),  // Business key
    variables            // Initial variables
);

// Get tasks for manager
List<Task> managerTasks = taskService.createTaskQuery()
    .taskCandidateGroup("manager")
    .processDefinitionKey("leaveApproval")
    .list();

// Complete task
taskService.complete(taskId, Map.of(
    "approved", true,
    "comment", "Approved"
));
```

---

## 4. Deboot Version - Cara Kerja

### 4.1 Perbedaan dengan Flowable

| Aspek | Flowable | Deboot |
|-------|----------|--------|
| Konfigurasi | BPMN XML | Service Code |
| Task Definition | BPMN UserTask | TaskService Query |
| Process Start | BPMN definition | runtimeService |
| Pendekatan | Declarative | Imperative |

### 4.2 Pendekatan Service-Based

Deboot version tidak menggunakan BPMN XML untuk definisi proses, melainkan menggunakan Flowable secara langsung melalui service:

```java
// Process starts directly via RuntimeService
ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
    PROCESS_DEFINITION_KEY,
    businessKey,
    variables
);

// Tasks are queried using TaskService
List<Task> tasks = taskService.createTaskQuery()
    .taskCandidateGroup("manager")
    .processDefinitionKey(PROCESS_DEFINITION_KEY)
    .list();

// Task completion
taskService.complete(taskId, variables);
```

### 4.3 Konvensi Deboot

1. **Direct Service Usage**: Menggunakan Flowable services langsung
2. **Process Definition Key**: Sama dengan Flowable version (`leaveApproval`)
3. **Task Assignment**: Melalui candidate groups (`managers`, `hr`)
4. **State Sync**: Entity status di-sync dengan Flowable process state

---

## 5. Perbandingan Cara Kerja

### 5.1 tabel Perbandingan

| Aspek | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| Workflow Definition | Kode Java | BPMN XML | Service Code |
| State Tracking | Database field | Process Variables | Process Variables |
| Task Assignment | Manual query | Candidate Groups | Candidate Groups |
| Transition Logic | If-else | BPMN Gateways | Flow Control |
| History | Manual table | Auto History | Auto History |
| Visualisasi | ❌ | ✅ | ❌ |

### 5.2 Perbandingan Submit Request

**Manual Version:**
```java
LeaveRequest request = LeaveRequest.builder()
    .status(LeaveStatus.PENDING)
    .build();
leaveRequestRepository.save(request);
```

**Flowable Version:**
```java
// Save entity
LeaveRequest request = leaveRequestRepository.save(...);

// Start BPMN process
runtimeService.startProcessInstanceByKey("leaveApproval", 
    request.getId().toString(), variables);
```

**Deboot Version:**
```java
// Same as Flowable - no difference in start process
LeaveRequest request = leaveRequestRepository.save(...);
runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY,
    request.getId().toString(), variables);
```

### 5.3 Perbandingan Approval

**Manual Version:**
```java
// Validate state
if (request.getStatus() != LeaveStatus.PENDING) {
    throw new Exception("Invalid state");
}
// Update state
request.setStatus(LeaveStatus.MANAGER_APPROVED);
leaveRequestRepository.save(request);
```

**Flowable Version:**
```java
// Complete Flowable task
taskService.complete(taskId, Map.of("approved", true));
// Sync entity
request.setStatus(LeaveStatus.MANAGER_APPROVED);
leaveRequestRepository.save(request);
```

**Deboot Version:**
```java
// Same as Flowable
taskService.complete(taskId, Map.of("approved", true, "comment", comment));
request.setStatus(LeaveStatus.MANAGER_APPROVED);
leaveRequestRepository.save(request);
```

---

## 6. Audit Trail

### 6.1 Manual Version

Audit trail diimplementasikan secara manual dengan tabel `audit_logs`:

```java
AuditLog auditLog = AuditLog.builder()
    .leaveRequestId(requestId)
    .previousStatus(oldStatus)
    .newStatus(newStatus)
    .action(AuditLog.AuditAction.MANAGER_APPROVED)
    .performedBy(approvedBy)
    .role("manager")
    .comment(comment)
    .build();
auditLogRepository.save(auditLog);
```

### 6.2 Flowable & Deboot Version

Audit trail otomatis dari Flowable History Service:

```java
List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
    .processInstanceId(processInstanceId)
    .finished()
    .list();
```

Semua aktivitas (task creation, completion, dll) secara otomatis terekam dalam tabel `ACT_HI_*`.

---

## 7. Kesimpulan

1. **Manual Version**: Simple tapi terbatas, cocok untuk workflow sangat sederhana
   - Semua logika di kode Java
   - State machine dengan if-else
   - Audit trail manual

2. **Flowable Version**: Fleksibel dan powerful, standar industri BPM
   - Workflow didefinisikan dalam BPMN XML
   - Visualisasi proses
   - Audit trail otomatis

3. **Deboot Version**: Penyederhanaan Flowable tanpa kehilangan fitur
   - Tidak perlu learn BPMN XML
   - Semua logika di service code
   - Audit trail otomatis dari Flowable

Pilihan tergantung pada kompleksitas workflow dan kebutuhan enterprise.
