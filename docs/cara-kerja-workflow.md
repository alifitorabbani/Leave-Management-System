# Cara Kerja Workflow

**Proyek: Workflow Engine Comparison**  
**Perusahaan: PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono**  
**Konteks: Proyek magang — Riset internal workflow orchestration**

---

## 1. Pendahuluan

Dokumen ini menjelaskan cara kerja sistem workflow pada masing-masing versi implementasi.

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
- **If-else logic**: Validasi transisi yang diizinkan
- **Service layer**: Memproses perubahan status

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

**Process Instance**
- Eksekusi dari process definition
- Setiap pengajuan cuti = instance baru
- Disimpan di tabel `ACT_RU_EXECUTION`

### 3.4 State Management

Status workflow disimpan sebagai:
1. **Process Variables**: `managerApproved`, `hrApproved`, dll
2. **Task Variables**: Komentar, approver name
3. **Historic Data**: Semua aktivitas di记录

---

## 4. Deboot Version - Cara Kerja

### 4.1 Perbedaan dengan Flowable

Deboot menyederhanakan Flowable dengan:

| Aspek | Flowable | Deboot |
|-------|----------|--------|
| Konfigurasi | BPMN XML | Annotations |
| Task Definition | BPMN UserTask | @WorkflowTask |
| Variable Mapping | Manual | @VariableMapping |
| Process Start | runtimeService | @WorkflowStart |

### 4.2 Annotation-Driven Approach

```java
@WorkflowDefinition(key = "leaveApproval")
public class MyService {
    
    @WorkflowStart(variables = {...})
    public void startProcess() { }
    
    @WorkflowTask(taskName = "approval", candidateGroup = "manager")
    public void handleTask() { }
}
```

### 4.3 Konvensi Deboot

1. **Convention over Configuration**: Naming conventions代替 explicit config
2. **Auto-registration**: Workflow otomatis terdaftar saat startup
3. **Simplified API**: Abstraksi atas Flowable services

---

## 5. Perbandingan Cara Kerja

| Aspek | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| Workflow Definition | Kode Java | BPMN XML | Annotations |
| State Tracking | Database field | Process Variables | Process Variables |
| Task Assignment | Manual query | Candidate Groups | Candidate Groups |
| Transition Logic | If-else | BPMN Gateways | BPMN Gateways |
| History | Manual table | Auto History | Auto History |

---

## 6. Kesimpulan

1. **Manual Version**: Simple tapi terbatas, cocok untuk workflow sangat sederhana
2. **Flowable Version**: Fleksibel dan powerful, standar industri BPM
3. **Deboot Version**: Penyederhanaan Flowable tanpa kehilangan fitur

Pilihan tergantung pada kompleksitas workflow dan kebutuhan enterprise.
