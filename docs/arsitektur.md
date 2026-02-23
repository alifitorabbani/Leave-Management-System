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
3. **Deboot Version** — Implementasi menggunakan Deboot Framework (akselerator Flowable)

---

## 2. Arsitektur Manual Version

### 2.1 Diagram Arsitektur

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React)                         │
│              Port: 3100 → API: http://localhost:8100       │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP
┌──────────────────────────▼──────────────────────────────────┐
│                    BACKEND (Spring Boot)                    │
│                      Port: 8100                             │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐   │
│  │  Controller  │  │   Service    │  │  Repository    │   │
│  │  (REST API)  │──│(State Machine)│──│  (JPA)        │   │
│  └──────────────┘  └──────────────┘  └────────────────┘   │
│         │                  │                               │
│         ▼                  ▼                               │
│  ┌──────────────────────────────────────────────────┐      │
│  │              ENTITIES & DTOs                     │      │
│  │  - LeaveRequest (Entity)                        │      │
│  │  - LeaveStatus (Enum: PENDING, APPROVED, etc)   │      │
│  │  - AuditLog (Entity)                           │      │
│  └──────────────────────────────────────────────────┘      │
└──────────────────────────┬──────────────────────────────────┘
                           │ JDBC
┌──────────────────────────▼──────────────────────────────────┐
│               POSTGRESQL DATABASE                           │
│               Database: leave_manual_db                     │
├─────────────────────────────────────────────────────────────┤
│  TABLES:                                                    │
│  - leave_requests                                          │
│  - audit_logs                                              │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Komponen Utama

| Komponen | Deskripsi |
|----------|-----------|
| `LeaveRequestController` | REST endpoints untuk pengajuan dan approval |
| `LeaveRequestService` | Logika state machine dengan if-else |
| `LeaveStatus` | Enum status workflow (PENDING, MANAGER_APPROVED, etc.) |
| `LeaveRequest` | Entity JPA untuk data cuti |
| `AuditLog` | Entity untuk audit trail manual |

### 2.3 Karakteristik

- **State Management**: Enum-based dengan validasi if-else
- **Workflow Logic**: Kode Java hardcoded
- **Audit Trail**: Manual dengan tabel audit_logs
- **Skalabilitas**: Terbatas pada kompleksitas kode

---

## 3. Arsitektur Flowable Version

### 3.1 Diagram Arsitektur

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React)                         │
│              Port: 3200 → API: http://localhost:8200       │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP
┌──────────────────────────▼──────────────────────────────────┐
│                    BACKEND (Spring Boot)                    │
│                      Port: 8200                             │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐   │
│  │  Controller  │  │   Service    │  │  Repository    │   │
│  │  (REST API)  │──│(Flowable)   │──│  (JPA)        │   │
│  └──────────────┘  └──────────────┘  └────────────────┘   │
│         │                  │                               │
│         ▼                  ▼                               │
│  ┌──────────────────────────────────────────────────┐      │
│  │           FLOWABLE ENGINE                        │      │
│  │  ┌─────────────┐  ┌────────────┐  ┌──────────┐ │      │
│  │  │RuntimeService│ │TaskService  │ │HistorySvc │ │      │
│  │  └─────────────┘  └────────────┘  └──────────┘ │      │
│  └──────────────────────────────────────────────────┘      │
│         │                                                    │
│         ▼                                                    │
│  ┌──────────────────────────────────────────────────┐      │
│  │         BPMN 2.0 PROCESS DEFINITION              │      │
│  │         (leave-approval.bpmn20.xml)                │      │
│  └──────────────────────────────────────────────────┘      │
└──────────────────────────┬──────────────────────────────────┘
                           │ JDBC
┌──────────────────────────▼──────────────────────────────────┐
│               POSTGRESQL DATABASE                           │
│               Database: leave_flowable_db                   │
├─────────────────────────────────────────────────────────────┤
│  TABLES (Application):                                      │
│  - leave_requests                                          │
├─────────────────────────────────────────────────────────────┤
│  TABLES (Flowable - Auto-generated):                       │
│  - ACT_RE_PROCDEF, ACT_RU_TASK, ACT_HI_PROCINST           │
│  - ACT_HI_TASKINST, ACT_HI_VARINST, ACT_HI_ACTINST       │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 Komponen Utama

| Komponen | Deskripsi |
|----------|-----------|
| `RuntimeService` | Menjalankan dan mengelola proses workflowService` | Mengelola user tasks (manager |
| `Task/HR approval) |
| `HistoryService` | Mengambil data historis proses |
| `leave-approval.bpmn20.xml` | Definisi proses BPMN 2.0 |

### 3.3 Karakteristik

- **State Management**: Engine-based (Flowable)
- **Workflow Logic**: BPMN 2.0 XML
- **Audit Trail**: Otomatis oleh Flowable History
- **Skalabilitas**: Tinggi dengan fitur Flowable

---

## 4. Arsitektur Deboot Version

### 4.1 Diagram Arsitektur

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React)                         │
│              Port: 3300 → API: http://localhost:8300       │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP
┌──────────────────────────▼──────────────────────────────────┐
│                    BACKEND (Spring Boot)                    │
│                      Port: 8300                             │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐   │
│  │  Controller  │  │   Service    │  │  Repository    │   │
│  │  (REST API)  │──│(Deboot)     │──│  (JPA)        │   │
│  └──────────────┘  └──────────────┘  └────────────────┘   │
│         │                  │                               │
│         ▼                  ▼                               │
│  ┌──────────────────────────────────────────────────┐      │
│  │              DEBOOT FRAMEWORK                      │      │
│  │  ┌────────────────────────────────────────────┐  │      │
│  │  │    @WorkflowDefinition                      │  │      │
│  │  │    @WorkflowTask                            │  │      │
│  │  │    @WorkflowVariable                        │  │      │
│  │  │    (Annotation-driven)                      │  │      │
│  │  └────────────────────────────────────────────┘  │      │
│  └──────────────────────────────────────────────────┘      │
│         │                                                    │
│         ▼                                                    │
│  ┌──────────────────────────────────────────────────┐      │
│  │           FLOWABLE ENGINE (Hidden)               │      │
│  │      (Deboot menyederhanakan akses)              │      │
│  └──────────────────────────────────────────────────┘      │
└──────────────────────────┬──────────────────────────────────┘
                           │ JDBC
┌──────────────────────────▼──────────────────────────────────┐
│               POSTGRESQL DATABASE                           │
│               Database: leave_deboot_db                     │
├─────────────────────────────────────────────────────────────┤
│  TABLES (Application + Flowable):                          │
│  - leave_requests                                          │
│  - ACT_RE_PROCDEF, ACT_RU_TASK, ACT_HI_PROCINST, etc.     │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Komponen Utama

| Komponen | Deskripsi |
|----------|-----------|
| `@WorkflowDefinition` | Annotasi untuk mendefinisikan workflow |
| `@WorkflowTask` | Annotasi untuk user tasks |
| `@WorkflowVariable` | Annotasi untuk variabel proses |
| `DebootLeaveWorkflowService` | Service dengan pattern Deboot |

### 4.3 Karakteristik

- **State Management**: Annotation-driven (Deboot)
- **Workflow Logic**: Java Annotations (tidak perlu BPMN XML)
- **Audit Trail**: Otomatis (menggunakan Flowable)
- **Skalabilitas**: Tinggi dengan penyederhanaan Deboot

---

## 5. Perbandingan Arsitektur

| Aspek | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| **Pendekatan** | Kode Java | BPMN 2.0 | Annotations |
| **Kompleksitas Kode** | Tinggi | Medium | Rendah |
| **Visualisasi** | ❌ | ✅ (BPMN) | ✅ |
| **Konfigurasi** | Manual | XML + Config | Annotations |
| **Database Schema** | Manual | Auto-generated | Auto-generated |
| **Learning Curve** | Rendah | Tinggi | Medium |

---

## 6. Kesimpulan

### Kapan Menggunakan Manual:
- Proses bisnis sederhana dan stabil
- Tim kecil tanpa keahlian BPMN
- Tidak memerlukan visualisasi workflow

### Kapan Menggunakan Flowable:
- Proses bisnis kompleks
- Memerlukan audit trail lengkap
- Kebutuhan compliance dan governance

### Kapan Menggunakan Deboot:
- Ingin manfaat BPM tanpa kompleksitas
- Tim dengan keahlian Java lebih baik
- Pengembangan cepat dengan pattern yang disediakan
