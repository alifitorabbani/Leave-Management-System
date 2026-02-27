# Arsitektur Sistem - Workflow Engine Comparison

**Proyek: Workflow Engine Comparison**  
**Perusahaan: PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono**  
**Konteks: Proyek magang — Riset internal workflow orchestration**

---

## 1. Pendahuluan

Dokumen ini menjelaskan arsitektur dari tiga implementasi sistem pengajuan cuti yang dibandingkan dalam proyek ini:

1. **Manual Version** — Implementasi dengan state machine berbasis kode
2. **Flowable Version** — Implementasi menggunakan Flowable BPMN Engine
3. **Deboot Version** — Implementasi menggunakan Flowable dengan pendekatan service yang disederhanakan

---

## 2. Arsitektur Manual Version

### 2.1 Diagram Arsitektur

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React + Vite)                  │
│              Port: 3100 → API: http://localhost:8100       │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP/REST
┌──────────────────────────▼──────────────────────────────────┐
│                 BACKEND (Spring Boot 3.2.0)                 │
│                       Port: 8100                            │
├─────────────────────────────────────────────────────────────┤
│  ┌────────────────┐  ┌─────────────────┐  ┌──────────────┐  │
│  │   Controller   │  │    Service     │  │  Repository  │  │
│  │LeaveRequest    │──│LeaveRequest    │──│ JPA          │  │
│  │Controller     │  │Service         │  │              │  │
│  └───────┬────────┘  └───────┬─────────┘  └──────────────┘  │
│          │                   │                               │
│          ▼                   ▼                               │
│  ┌───────────────────────────────────────────────────────┐   │
│  │                   ENTITIES                            │   │
│  │  ┌─────────────┐ ┌───────────┐ ┌────────────────┐   │   │
│  │  │LeaveRequest │ │LeaveStatus│ │   AuditLog     │   │   │
│  │  │  (Entity)   │ │  (Enum)   │ │   (Entity)     │   │   │
│  │  └─────────────┘ └───────────┘ └────────────────┘   │   │
│  │  ┌─────────────┐ ┌───────────┐ ┌────────────────┐   │   │
│  │  │  LeaveType  │ │    DTO    │ │    Exception  │   │   │
│  │  │  (Enum)    │ │           │ │               │   │   │
│  │  └─────────────┘ └───────────┘ └────────────────┘   │   │
│  └───────────────────────────────────────────────────────┘   │
│          │                                                   │
│          ▼                                                   │
│  ┌───────────────────────────────────────────────────────┐   │
│  │              AOP / ASPECTS                            │   │
│  │         LoggingAspect (AOP Logging)                   │   │
│  └───────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                          │ JDBC
┌──────────────────────────▼──────────────────────────────────┐
│                  POSTGRESQL DATABASE                        │
│                  Database: leave_manual_db                  │
├─────────────────────────────────────────────────────────────┤
│  TABLES:                                                    │
│  ────────────────────────────────────────────────────────   │
│  • leave_requests (id, employee_name, employee_id,         │
│    department, leave_type, start_date, end_date, reason,   │
│    status, manager_comment, hr_comment, etc.)              │
│  • audit_logs (id, leave_request_id, action, performed_by, │
│    role, comment, created_at)                             │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Komponen Utama

| Komponen | Lokasi | Deskripsi |
|----------|--------|-----------|
| `LeaveRequestController` | `controller/LeaveRequestController.java` | REST endpoints untuk pengajuan dan approval |
| `LeaveRequestService` | `service/LeaveRequestService.java` | Logika state machine dengan if-else |
| `LeaveStatus` | `entity/LeaveStatus.java` | Enum status workflow (PENDING, MANAGER_APPROVED, etc.) |
| `LeaveRequest` | `entity/LeaveRequest.java` | Entity JPA untuk data cuti |
| `AuditLog` | `entity/AuditLog.java` | Entity untuk audit trail manual |
| `LoggingAspect` | `aspect/LoggingAspect.java` | AOP untuk logging |
| `GlobalExceptionHandler` | `exception/GlobalExceptionHandler.java` | Handling exception global |

### 2.3 Karakteristik

- **State Management**: Enum-based dengan validasi if-else
- **Workflow Logic**: Kode Java hardcoded di Service layer
- **Audit Trail**: Manual dengan tabel audit_logs
- **Skalabilitas**: Terbatas pada kompleksitas kode
- **Dependency**: Spring Boot 3.2.0, Spring Data JPA, PostgreSQL

---

## 3. Arsitektur Flowable Version

### 3.1 Diagram Arsitektur

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React + Vite)                  │
│              Port: 3200 → API: http://localhost:8200       │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP/REST
┌──────────────────────────▼──────────────────────────────────┐
│                 BACKEND (Spring Boot 3.2.0)                 │
│                       Port: 8200                            │
├─────────────────────────────────────────────────────────────┤
│  ┌────────────────┐  ┌─────────────────┐  ┌──────────────┐  │
│  │   Controller   │  │    Service     │  │  Repository  │  │
│  │LeaveRequest    │──│LeaveWorkflow   │──│ JPA          │  │
│  │Controller     │  │Service         │  │              │  │
│  └───────┬────────┘  └───────┬─────────┘  └──────────────┘  │
│          │                   │                               │
│          ▼                   ▼                               │
│  ┌───────────────────────────────────────────────────────┐   │
│  │            FLOWABLE ENGINE (v6.8.0)                  │   │
│  │  ┌───────────────┐ ┌────────────┐ ┌──────────────┐  │   │
│  │  │ RuntimeService │ │TaskService │ │HistoryService│  │   │
│  │  │ (Process)     │ │(User Tasks)│ │ (Audit)      │  │   │
│  │  └───────────────┘ └────────────┘ └──────────────┘  │   │
│  └───────────────────────────────────────────────────────┘   │
│          │                                                   │
│          ▼                                                   │
│  ┌───────────────────────────────────────────────────────┐   │
│  │              BPMN 2.0 PROCESS DEFINITION              │   │
│  │              (leave-approval.bpmn20.xml)               │   │
│  └───────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                          │ JDBC
┌──────────────────────────▼──────────────────────────────────┐
│                  POSTGRESQL DATABASE                        │
│                  Database: leave_flowable_db               │
├─────────────────────────────────────────────────────────────┤
│  TABLES (Application):                                     │
│  ────────────────────────────────────────────────────────   │
│  • leave_requests                                          │
│  • audit_logs                                              │
├─────────────────────────────────────────────────────────────┤
│  TABLES (Flowable - Auto-generated):                       │
│  ────────────────────────────────────────────────────────   │
│  • ACT_RE_PROCDEF (Process Definitions)                   │
│  • ACT_RU_TASK (Runtime Tasks)                            │
│  • ACT_RU_EXECUTION (Runtime Executions)                 │
│  • ACT_HI_PROCINST (Historical Process Instances)       │
│  • ACT_HI_TASKINST (Historical Task Instances)          │
│  • ACT_HI_VARINST (Historical Variables)                 │
│  • ACT_HI_ACTINST (Historical Activity Instances)       │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 Komponen Utama

| Komponen | Deskripsi |
|----------|-----------|
| `RuntimeService` | Menjalankan dan mengelola proses workflow |
| `TaskService` | Mengelola user tasks (manager/HR approval) |
| `HistoryService` | Mengambil data historis proses |
| `RepositoryService` | Mengelola deployment BPMN |
| `leave-approval.bpmn20.xml` | Definisi proses BPMN 2.0 |

### 3.3 BPMN Process Definition

```xml
<!-- Simplified Flow -->
START → Manager Approval (UserTask) → [Gateway] → HR Approval (UserTask) → [Gateway] → END
                                    ↓                    ↓
                              Manager Reject        HR Reject
                                    ↓                    ↓
                                   END                 END
```

### 3.4 Karakteristik

- **State Management**: Engine-based (Flowable)
- **Workflow Logic**: BPMN 2.0 XML
- **Audit Trail**: Otomatis oleh Flowable History
- **Skalabilitas**: Tinggi dengan fitur Flowable clustering
- **Visualisasi**: BPMN diagram dapat divisualisasikan

---

## 4. Arsitektur Deboot Version

### 4.1 Diagram Arsitektur

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React + Vite)                  │
│              Port: 3300 → API: http://localhost:8300       │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP/REST
┌──────────────────────────▼──────────────────────────────────┐
│                 BACKEND (Spring Boot 3.2.0)                 │
│                       Port: 8300                            │
├─────────────────────────────────────────────────────────────┤
│  ┌────────────────┐  ┌─────────────────┐  ┌──────────────┐  │
│  │   Controller   │  │    Service     │  │  Repository  │  │
│  │LeaveRequest    │──│DebootLeave      │──│ JPA          │  │
│  │Controller     │  │WorkflowService  │  │              │  │
│  └───────┬────────┘  └───────┬─────────┘  └──────────────┘  │
│          │                   │                               │
│          ▼                   ▼                               │
│  ┌───────────────────────────────────────────────────────┐   │
│  │              FLOWABLE ENGINE (v6.8.0)                 │   │
│  │              (Same as Flowable Version)               │   │
│  │  ┌───────────────┐ ┌────────────┐ ┌──────────────┐  │   │
│  │  │ RuntimeService │ │TaskService │ │HistoryService│  │   │
│  │  └───────────────┘ └────────────┘ └──────────────┘  │   │
│  └───────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                          │ JDBC
┌──────────────────────────▼──────────────────────────────────┐
│                  POSTGRESQL DATABASE                        │
│                  Database: leave_deboot_db                  │
├─────────────────────────────────────────────────────────────┤
│  TABLES (Application + Flowable):                          │
│  ────────────────────────────────────────────────────────   │
│  • leave_requests                                          │
│  • audit_logs                                              │
│  • ACT_RE_PROCDEF, ACT_RU_TASK, ACT_HI_PROCINST, etc.     │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Perbedaan dengan Flowable Version

| Aspek | Flowable Version | Deboot Version |
|-------|------------------|----------------|
| **Konfigurasi Workflow** | BPMN 2.0 XML file | Service-based (code) |
| **Proses Deployment** | Deploy BPMN XML | Auto-start dengan Flowable |
| **Task Definition** | Di BPMN | Di Service method |
| **Pendekatan** | Declarative (XML) | Imperative (Code-first) |
| **Kompleksitas Kode** | Medium | Rendah |

### 4.3 Komponen Utama

| Komponen | Deskripsi |
|----------|-----------|
| `DebootLeaveWorkflowService` | Service utama dengan pendekatan yang disederhanakan |
| `RuntimeService` | Flowable RuntimeService (injected) |
| `TaskService` | Flowable TaskService (injected) |
| `HistoryService` | Flowable HistoryService (injected) |

### 4.4 Pendekatan Deboot

Deboot version menggunakan Flowable secara langsung tetapi dengan pendekatan yang lebih sederhana:

1. **Tanpa BPMN XML**: Tidak memerlukan file definisi proses
2. **Service-based**: Semua logika workflow ada di service class
3. **Runtime Process**: Proses dimulai secara terprogram melalui RuntimeService
4. **Task Query**: Task diperoleh melalui TaskService dengan query kandidat group

### 4.5 Karakteristik

- **State Management**: Code-driven (tanpa BPMN XML)
- **Workflow Logic**: Java service method
- **Audit Trail**: Otomatis (menggunakan Flowable)
- **Skalabilitas**: Tinggi (menggunakan Flowable engine)
- **Kompleksitas**: Lebih rendah dari Flowable version

---

## 5. Perbandingan Arsitektur

### 5.1 tabel Perbandingan

| Aspek | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| **Pendekatan** | Kode Java | BPMN 2.0 | Service-based |
| **Kompleksitas Kode (Backend)** | 1,731 LOC | 1,563 LOC | 1,482 LOC |
| **Visualisasi** | ❌ | ✅ (BPMN) | ❌ |
| **Konfigurasi** | Manual (Java) | XML + Config | Java Code |
| **Database Schema** | Manual | Auto-generated | Auto-generated |
| **Learning Curve** | Rendah | Tinggi | Medium |
| **State Machine** | Enum + If-Else | BPMN Engine | Flowable Services |
| **Audit Trail** | Manual (tabel) | Auto (Flowable) | Auto (Flowable) |
| **Dependency** | Spring Boot only | Flowable 6.8.0 | Flowable 6.8.0 |

### 5.2 Diagram Perbandingan Arsitektur

```
┌─────────────────────────────────────────────────────────────────┐
│                        MANUAL VERSION                           │
│  ┌─────────┐    ┌─────────────┐    ┌────────────┐            │
│  │Controller│───►│ StateMachine│───►│ Repository │            │
│  │  (API)  │    │  (If-Else)  │    │   (JPA)    │            │
│  └─────────┘    └─────────────┘    └────────────┘            │
│                        │                                        │
│                        ▼                                        │
│                 ┌─────────────┐                                 │
│                 │  Entities   │                                 │
│                 │ LeaveStatus │                                 │
│                 └─────────────┘                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                       FLOWABLE VERSION                          │
│  ┌─────────┐    ┌─────────────┐    ┌────────────┐            │
│  │Controller│───►│   Service   │───►│ Repository │            │
│  │  (API)  │    │ (Flowable)  │    │   (JPA)    │            │
│  └─────────┘    └──────┬──────┘    └────────────┘            │
│                        │                                        │
│                        ▼                                        │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              FLOWABLE ENGINE + BPMN XML                 │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                        DEBOOT VERSION                           │
│  ┌─────────┐    ┌─────────────────────┐    ┌────────────┐      │
│  │Controller│───►│ DebootLeaveWorkflow │───►│ Repository │      │
│  │  (API)  │    │     Service         │    │   (JPA)    │      │
│  └─────────┘    └──────────┬──────────┘    └────────────┘      │
│                           │                                        │
│                           ▼                                        │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │              FLOWABLE ENGINE (Direct)                  │     │
│  │         (RuntimeService + TaskService)                 │     │
│  └─────────────────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6. Technology Stack

### 6.1 Backend

| Komponen | Manual | Flowable | Deboot |
|----------|--------|----------|--------|
| Framework | Spring Boot 3.2.0 | Spring Boot 3.2.0 | Spring Boot 3.2.0 |
| BPM Engine | ❌ | Flowable 6.8.0 | Flowable 6.8.0 |
| Database | PostgreSQL | PostgreSQL | PostgreSQL |
| ORM | Spring Data JPA | Spring Data JPA | Spring Data JPA |
| Build | Maven | Maven | Maven |
| Java | 17+ | 17+ | 17+ |

### 6.2 Frontend

| Komponen | Semua Versi |
|----------|-------------|
| Framework | React 18 |
| Language | TypeScript |
| Build Tool | Vite |
| Styling | Tailwind CSS |
| HTTP Client | Axios |
| Routing | React Router |

---

## 7. Kesimpulan

### Kapan Menggunakan Manual:
- Proses bisnis sederhana dan stabil
- Tim kecil tanpa keahlian BPMN
- Tidak memerlukan visualisasi workflow
- Budget terbatas

### Kapan Menggunakan Flowable:
- Proses bisnis kompleks
- Memerlukan audit trail lengkap
- Kebutuhan compliance dan governance
- Memerlukan visualisasi workflow
- Skala enterprise

### Kapan Menggunakan Deboot:
- Ingin manfaat BPM tanpa kompleksitas XML
- Tim dengan keahlian Java lebih baik
- Pengembangan cepat
-Perlu skalabilitas Flowable tanpa belajar BPMN
