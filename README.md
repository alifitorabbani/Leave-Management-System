# STUDI PERBANDINGAN IMPLEMENTASI WORKFLOW ENGINE

## Analisis Pendekatan Manual, Flowable BPM, dan Deboot (Service-Based)

---

**Proyek Riset Internal R&D | PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono — Backend Engineer Intern (Workflow Research)**  

---

## DAFTAR ISI

1. [Pendahuluan](#1-pendahuluan)
    - [1.1 Latar Belakang Proyek](#11-latar-belakang-proyek)
    - [1.2 Apa itu Workflow Engine?](#12-apa-itu-workflow-engine)
    - [1.3 Mengapa Perbandingan Ini Penting?](#13-mengapa-perbandingan-ini-penting)
2. [Latar Belakang Masalah](#2-latar-belakang-masalah)
    - [2.1 Problem Statement](#21-problem-statement)
    - [2.2 Rumusan Masalah](#22-rumusan-masalah)
3. [Tujuan Penelitian](#3-tujuan-penelitian)
    - [3.1 Tujuan Umum](#31-tujuan-umum)
    - [3.2 Tujuan Khusus](#32-tujuan-khusus)
4. [Ruang Lingkup](#4-ruang-lingkup)
    - [4.1 Batasan Penelitian](#41-batasan-penelitian)
    - [4.2 Use Case yang Diimplementasikan](#42-use-case-yang-diimplementasikan)
5. [Metodologi](#5-metodologi)
    - [5.1 Pendekatan Penelitian](#51-pendekatan-penelitian)
    - [5.2 Kriteria Perbandingan](#52-kriteria-perbandingan)
6. [Tinjauan Pustaka](#6-tinjauan-pustaka)
    - [6.1 Konsep Workflow](#61-konsep-workflow)
    - [6.2 BPMN (Business Process Model and Notation)](#62-bpmn-business-process-model-and-notation)
    - [6.3 Flowable BPM Engine](#63-flowable-bpm-engine)
    - [6.4 State Machine](#64-state-machine)
7. [Analisis Implementasi](#7-analisis-implementasi)
    - [7.1 Manual Version](#71-manual-version)
        - [7.1.1 Konsep](#711-konsep)
        - [7.1.2 Arsitektur](#712-arsitektur)
        - [7.1.3 Implementasi Kode](#713-implementasi-kode)
        - [7.1.4 Kelebihan](#714-kelebihan)
        - [7.1.5 Kekurangan](#715-kekurangan)
    - [7.2 Flowable Version](#72-flowable-version)
        - [7.2.1 Konsep](#721-konsep)
        - [7.2.2 Arsitektur](#722-arsitektur)
        - [7.2.3 BPMN Process Definition](#723-bpmn-process-definition)
        - [7.2.4 Implementasi Kode](#724-implementasi-kode)
        - [7.2.5 Kelebihan](#725-kelebihan)
        - [7.2.6 Kekurangan](#726-kekurangan)
    - [7.3 Deboot Version](#73-deboot-version)
        - [7.3.1 Konsep](#731-konsep)
        - [7.3.2 Arsitektur](#732-arsitektur)
        - [7.3.3 BPMN Process Definition](#733-bpmn-process-definition)
        - [7.3.4 Implementasi Kode](#734-implementasi-kode)
        - [7.3.5 Kelebihan](#735-kelebihan)
        - [7.3.6 Kekurangan](#736-kekurangan)
8. [Perbandingan Teknis](#8-perbandingan-teknis)
    - [8.1 Statistik LOC](#81-statistik-loc-lines-of-code)
    - [8.2 Perbandingan Fitur](#82-perbandingan-fitur)
    - [8.3 Technology Stack](#83-technology-stack)
    - [8.4 Konfigurasi Port](#84-konfigurasi-port)
    - [8.5 REST API Endpoints](#85-rest-api-endpoints)
    - [8.6 Default Credentials](#86-default-credentials)
9. [Lisensi dan Pricing](#9-lisensi-dan-pricing)
    - [9.1 Manual Version](#91-manual-version)
    - [9.2 Flowable BPM](#92-flowable-bpm)
    - [9.3 Deboot Framework](#93-deboot-framework)
    - [9.4 Perbandingan TCO](#94-perbandingan-tco)
    - [9.5 Rekomendasi Berdasarkan Budget](#95-rekomendasi-berdasarkan-budget)
    - [9.6 Rekomendasi Berdasarkan Ukuran Organisasi](#96-rekomendasi-berdasarkan-ukuran-organisasi)
10. [Hasil dan Pembahasan](#10-hasil-dan-pembahasan)
    - [10.1 Analisis Kompleksitas Kode](#101-analisis-kompleksitas-kode)
    - [10.2 Analisis Enterprise](#102-analisis-enterprise)
    - [10.3 Rekomendasi Berdasarkan Use Case](#103-rekomendasi-berdasarkan-use-case)
    - [10.4 Analogi Perumpamaan](#104-analogi-perumpamaan)
    - [10.5 Hasil dan Pembahasan Pricing & Lisensi](#105-hasil-dan-pembahasan-pricing--lisensi)
    - [10.6 Code-based vs Web Modeler](#106-code-based-vs-web-modeler)
11. [Kesimpulan dan Saran](#11-kesimpulan-dan-saran)
    - [11.1 Kesimpulan](#111-kesimpulan)
    - [11.2 Catatan Penting](#112-catatan-penting)
    - [11.3 Penjelasan "Vendor Support"](#113-penjelasan-vendor-support)
    - [11.4 Saran](#114-saran)

---

## 1. PENDAHULUAN

### 1.1 Latar Belakang Proyek

Dalam konteks transformasi digital perusahaan, sistem manajemen cuti merupakan salah satu aplikasi enterprise yang penting. Pengajuan cuti karyawan memerlukan workflow approval yang melibatkan beberapa pihak seperti manager dan HRD. traditional implementasi workflow dapat dilakukan secara manual atau menggunakan engine khusus.

Proyek ini membandingkan **tiga pendekatan implementasi workflow** untuk sistem pengajuan cuti perusahaan:

1. **Manual Workflow** — Implementasi dengan state machine berbasis kode (if-else, enum)
2. **Flowable BPM** — Implementasi menggunakan Flowable BPMN Engine (Community Edition)
3. **Deboot Framework** — Implementasi menggunakan Flowable dengan pendekatan yang disederhanakan (service-based tanpa BPMN XML)

### 1.2 Apa itu Workflow Engine?

Workflow Engine adalah sistem yang mengelola dan mengeksekusi alur kerja bisnis secara otomatis. Engine ini mengatur:
- Urutan tugas (task sequencing)
- Alokasi tugas ke pengguna (task assignment)
- Status dan progress proses
- Audit trail / riwayat aktivitas

### 1.3 Mengapa Perbandingan Ini Penting?

- Memahami trade-off antar pendekatan
- Membantu organisasi memilih solusi yang tepat
- Menjadi bahan pembelajaran internal
- Mendukung pengambilan keputusan teknis

---

## 2. LATAR BELAKANG MASALAH

### 2.1 Problem Statement

Dalam pengembangan sistem informasi enterprise, pemilihan pendekatan workflow merupakan keputusan arsitektur yang penting. Pertanyaan utama yang ingin dijawab:

1. **Kapan sebaiknya menggunakan implementasi manual tanpa BPM engine?**
2. **Apakah BPMN engine seperti Flowable selalu diperlukan?**
3. **Apakah ada pendekatan tengah yang memberikan manfaat BPM tanpa kompleksitas penuh?**

### 2.2 Rumusan Masalah

Berdasarkan problem statement di atas, penelitian ini merumuskan masalah:

1. Bagaimana implementasi manual (tanpa engine) dibandingkan dengan implementasi menggunakan Flowable BPM?
2. Apakah pendekatan service-based (Deboot) dapat menjadi alternatif yang efektif?
3. Apa trade-off dari masing-masing pendekatan dalam hal kompleksitas kode, maintainability, dan skalabilitas?

---

## 3. TUJUAN PENELITIAN

### 3.1 Tujuan Umum

Menganalisis dan membandingkan tiga pendekatan implementasi workflow untuk sistem pengajuan cuti perusahaan.

### 3.2 Tujuan Khusus

1. **Perbandingan Teknis** — Mengukur perbedaan kompleksitas kode (LOC), maintainability, dan skalabilitas antar pendekatan workflow

2. **Evaluasi BPM Engine** — Memahami kapan BPMN engine diperlukan vs manual workflow

3. **Rekomendasi Enterprise** — Memberikan insight untuk pemilihan solusi workflow di lingkungan enterprise

4. **Proof of Concept** — Mendemonstrasikan implementasi nyata dari setiap pendekatan

5. **Pembelajaran Internal** — Menjadi bahan ajar untuk tim developer di PT. Len Industri

---

## 4. RUANG LINGKUP

### 4.1 Batasan Penelitian

- **Aplikasi**: Sistem Pengajuan Cuti (Leave Request Management)
- **Workflow**: 2-step approval (Manager → HR)
- **Teknologi**: Spring Boot, React, PostgreSQL, Flowable
- **Scope**: Backend workflow implementation

### 4.2 Use Case yang Diimplementasikan

```
[Karyawan Submit] → [Manager Approval] → [HR Approval] → [Final Status]
                         │                   │
                         └─ REJECT ──────────┘
```

**Aktor:**
- **Employee** — Pegawai yang mengajukan cuti
- **Manager** — Atasan yang melakukan approval level 1
- **HR** — Human Resources yang melakukan approval final

**Jenis Cuti:**
- Annual Leave (Cuti Tahunan)
- Sick Leave (Cuti Sakit)
- Personal Leave (Cuti Pribadi)
- Maternity Leave (Cuti Melahirkan)

---

## 5. METODOLOGI

### 5.1 Pendekatan Penelitian

Penelitian ini menggunakan pendekatan **deskriptif komparatif** dengan langkah-langkah:

1. **Studi Literatur** — Mempelajari konsep workflow engine, BPMN, dan implementasi terkait
2. **Implementasi** — Mengembangkan tiga versi sistem dengan pendekatan berbeda
3. **Analisis** — Membandingkan berdasarkan kriteria teknis dan non-teknis
4. **Evaluasi** — Memberikan rekomendasi berdasarkan hasil analisis

### 5.2 Kriteria Perbandingan

| Kategori | Kriteria |
|----------|----------|
| **Teknis** | LOC, kompleksitas kode, arsitektur, maintainability |
| **Fungsional** | Fitur, workflow capability, audit trail |
| **Operasional** | Setup effort, learning curve, debugging |
| **Enterprise** | Scalability, compliance, vendor lock-in |

---

## 6. TINJAUAN PUSTAKA

### 6.1 Konsep Workflow

Workflow adalah urutan aktivitas tugas yang dilakukan dalam proses bisnis. Dalam konteks sistem informasi, workflow engine mengelola eksekusi alur kerja ini secara otomatis.

**Komponen utama workflow:**
- **Process Definition** — Definisi alur kerja
- **Process Instance** — Eksekusi spesifik dari definisi
- **Task** — Unit kerja yang dilakukan oleh user
- **Transition** — Perpindahan dari satu aktivitas ke aktivitas lain

### 6.2 BPMN (Business Process Model and Notation)

BPMN adalah standar industri untuk pemodelan proses bisnis. Elemen utama:

- **Start Event** — Titik awal proses
- **End Event** — Titik akhir proses
- **User Task** — Tugas yang dilakukan manusia
- **Service Task** — Tugas otomatis
- **Exclusive Gateway** — Keputusan dengan satu pilihan
- **Parallel Gateway** — Alur paralel

### 6.3 Flowable BPM Engine

Flowable adalah open-source BPMN engine yang menyediakan:
- Process execution engine
- Task management
- History and audit
- Form handling
- DMN (Decision Model and Notation)

### 6.4 State Machine

State machine adalah model matematika yang merepresentasikan perilaku sistem dengan status dan transisi. Dalam implementasi manual, enum digunakan untuk mendefinisikan status.

---

## 7. ANALISIS IMPLEMENTASI

### 7.1 MANUAL VERSION

#### 7.1.1 Konsep

Manual Version mengimplementasikan workflow menggunakan kode Java standar tanpa engine eksternal. State machine diimplementasikan dengan:
- Enum untuk status
- If-else untuk transisi
- Repository pattern untuk data access

#### 7.1.2 Arsitektur

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
│                  Database: leave_manual_db                   │
│  TABLES:                                                    │
│  • leave_requests                                          │
│  • audit_logs                                              │
└─────────────────────────────────────────────────────────────┘
```

#### 7.1.3 Implementasi Kode

**Entity LeaveStatus (Enum):**
```java
public enum LeaveStatus {
    PENDING("Menunggu Approval Manager", "leave.pending"),
    MANAGER_APPROVED("Approved oleh Manager", "leave.manager.approved"),
    MANAGER_REJECTED("Ditolak oleh Manager", "leave.manager.rejected"),
    HR_APPROVED("Approved (Final)", "leave.hr.approved"),
    HR_REJECTED("Ditolak oleh HR", "leave.hr.rejected");
    
    private final String displayName;
    private final String messageCode;
}
```

**Penjelasan Kode:**
- `LeaveStatus` adalah enum yang mendefinisikan semua kemungkinan status dalam workflow
- Setiap status memiliki `displayName` untuk tampilan UI dan `messageCode` untuk internasionalisasi
- Urutan status menunjukkan alur workflow: PENDING → MANAGER_APPROVED/REJECTED → HR_APPROVED/REJECTED
- Status final adalah MANAGER_REJECTED, HR_APPROVED, dan HR_REJECTED (tidak bisa diubah lagi)

**Service Logic (State Transition):**
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

**Penjelasan Kode:**
- Method `managerApproval()` menerima request ID dan data approval dari manager
- **Validasi State**: Pada baris 3 (cek status == PENDING), dicek apakah status saat ini adalah PENDING - jika bukan, akan throw exception InvalidStateTransitionException
- **Transisi Status**: Jika approval disetujui (isApproved=true), status berubah ke MANAGER_APPROVED, jika ditolak menjadi MANAGER_REJECTED
- **Update Data**: Manager comment, nama approver, dan tanggal approval dicatat
- **Audit Trail**: Setiap perubahan status dicatat ke tabel audit_logs via createAuditLog()
- Jika workflow ditolak di level manager, status menjadi final (MANAGER_REJECTED) dan tidak berlanjut ke HR

#### 7.1.4 Kelebihan

1. **Tidak ada dependency eksternal** — Hanya Spring Boot
2. **Kontrol penuh** — Semua logika bisnis dapat dimodifikasi dengan mudah
3. **Tidak ada vendor lock-in** — Tidak tergantung pada framework tertentu
4. **Mudah dipahami** — Cocok untuk pembelajaran dasar workflow
5. **Debugging langsung** — Tidak ada abstraksi yang menyembunyikan logika
6. **Performance optimal** — Tidak ada overhead dari engine eksternal

#### 7.1.5 Kekurangan

1. **Kode kompleks untuk workflow kompleks** — Jika workflow rumit, if-else sangat banyak
2. **Tidak ada visualisasi** — Tidak dapat melihat alur workflow secara visual
3. **Audit trail manual** — Harus membuat implementasi audit sendiri
4. **Scaling terbatas** — Untuk aplikasi besar, perlu effort tambahan
5. **Perubahan workflow sulit** — Perlu ubah kode untuk ubah alur

---

### 7.2 FLOWABLE VERSION

#### 7.2.1 Konsep

Flowable Version menggunakan Flowable BPMN Engine untuk mengelola workflow. Process didefinisikan dalam file BPMN 2.0 XML dan di-deploy ke engine.

#### 7.2.2 Arsitektur

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
│  • leave_requests                                          │
│  • audit_logs                                              │
├─────────────────────────────────────────────────────────────┤
│  TABLES (Flowable - Auto-generated):                       │
│  • ACT_RE_PROCDEF (Process Definitions)                   │
│  • ACT_RU_TASK (Runtime Tasks)                            │
│  • ACT_RU_EXECUTION (Runtime Executions)                 │
│  • ACT_HI_PROCINST (Historical Process Instances)         │
│  • ACT_HI_TASKINST (Historical Task Instances)            │
│  • ACT_HI_ACTINST (Historical Activity Instances)        │
└─────────────────────────────────────────────────────────────┘
```

#### 7.2.3 BPMN Process Definition

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

**Penjelasan Kode BPMN:**
- `<process id="leaveApproval">` adalah definisi process utama dengan ID "leaveApproval"
- `<startEvent id="startEvent">` adalah titik awal proses ketika user submit request
- `<userTask id="managerApprovalTask">` adalah task untuk approval manager, dengan `flowable:candidateGroups="managers"` berarti task ini akan muncul di task list user dengan role manager
- `<exclusiveGateway>` digunakan untuk keputusan (approve/reject) - hanya satu jalur yang dipilih
- `<conditionExpression>${managerApproved == true}</conditionExpression>` adalah kondisi untuk melanjutkan ke langkah berikutnya
- Jika manager reject, proses langsung selesai di `managerRejectedEnd`
- Jika manager approve, proses berlanjut ke HR approval task
- Pattern sama berlaku untuk HR decision
- Keunggulan BPMN: Visual, mudah dipahami stakeholder non-technical

#### 7.2.4 Implementasi Kode (RuntimeService)

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

**Penjelasan Kode RuntimeService:**
- `runtimeService.startProcessInstanceByKey()` adalah method untuk memulai process workflow baru
- Parameter pertama "leaveApproval" adalah process definition key (harus sama dengan ID di BPMN XML)
- Parameter kedua adalah business key (biasanya ID dari entity terkait)
- Parameter ketiga adalah variabel yang akan disimpan dalam process - ini bisa diambil later oleh task
- Process instance ID disimpan di entity untuk tracking
- Flowable akan otomatis membuat task untuk manager berdasarkan candidateGroups di BPMN

#### 7.2.5 Kelebihan

1. **Standar industri** — Menggunakan BPMN 2.0 standar
2. **Visual workflow** — Dapat divisualisasikan dengan Flowable Designer
3. **Audit trail otomatis** — Semua aktivitas di-log oleh Flowable
4. **Hot deployment** — Workflow dapat diubah tanpa redeploy kode
5. **Process mining** — Dapat menganalisis process performance
6. **Scalability tinggi** — Mendukung clustering
7. **Komunitas besar** — Dokumentasi dan support yang baik

#### 7.2.6 Kekurangan

1. **Kurva pembelajaran tinggi** — Perlu pelajari BPMN dan Flowable API
2. **Overhead untuk proyek kecil** — Terlalu banyak fitur untuk aplikasi sederhana
3. **Konfigurasi kompleks** — Lebih banyak setup dibanding manual
4. **Resource consumption** — Memerlukan lebih banyak resource
5. **Vendor lock-in** — Terikat dengan ekosistem Flowable

---

### 7.3 DEBOOT VERSION

#### 7.3.1 Konsep

Deboot Version adalah pendekatan **service-based** yang menggunakan Flowable secara langsung tanpa file BPMN XML. Ini adalah penyederhanaan dari Flowable penuh - menggunakan kemampuan engine tanpa kompleksitas BPMN.

**Penting**: Deboot dalam proyek ini **bukan framework terpisah**, melainkan pendekatan/implementasi kustom menggunakan Flowable library secara langsung.

#### 7.3.2 Perbedaan dengan Flowable Version

| Aspek | Flowable Version | Deboot Version |
|-------|------------------|----------------|
| **Konfigurasi Workflow** | BPMN 2.0 XML file | Service-based (code) |
| **Proses Deployment** | Deploy BPMN XML | Auto-start dengan Flowable |
| **Task Definition** | Di BPMN | Di Service method |
| **Pendekatan** | Declarative (XML) | Imperative (Code-first) |
| **Visualisasi** | ✅ (BPMN Diagram) | ❌ |

#### 7.3.3 Arsitektur

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
│  │              (Direct Service Usage)                    │   │
│  │  ┌───────────────┐ ┌────────────┐ ┌──────────────┐  │   │
│  │  │ RuntimeService │ │TaskService │ │HistoryService│  │   │
│  │  └───────────────┘ └────────────┘ └──────────────┘  │   │
│  └───────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                           │ JDBC
┌──────────────────────────▼──────────────────────────────────┐
│                  POSTGRESQL DATABASE                        │
│                  Database: leave_deboot_db                  │
│  TABLES:                                                    │
│  • leave_requests                                          │
│  • audit_logs                                              │
│  • ACT_RE_PROCDEF, ACT_RU_TASK, ACT_HI_PROCINST, etc.     │
└─────────────────────────────────────────────────────────────┘
```

#### 7.3.4 Implementasi Kode

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class DebootLeaveWorkflowService {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final LeaveRequestRepository leaveRequestRepository;
    
    private static final String PROCESS_DEFINITION_KEY = "leaveApproval";

    /**
     * Submit a new leave request
     * Starts a new Flowable process for leave approval
     */
    @Transactional
    public LeaveRequestDTO.Response submitRequest(LeaveRequestDTO.CreateRequest request) {
        log.info("Starting leave workflow using Flowable");
        
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
        
        // Start Flowable process directly (no BPMN file needed)
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
     * Approve by manager - using TaskService directly
     */
    @Transactional
    public LeaveRequestDTO.Response approveByManager(UUID requestId, LeaveRequestDTO.ApprovalRequest approvalRequest) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Request is not in pending status");
        }
        
        // Complete manager task via TaskService
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
}
```

**Penjelasan Kode Deboot Version (Baris per Baris):**

Kode di atas adalah implementasi **DebootLeaveWorkflowService** yang menggunakan **Flowable Engine secara langsung melalui Service API** tanpa file BPMN. Berikut penjelasan detail setiap bagian:

**1. Deklarasi Kelas dan Dependency Injection:**
```java
@Service  // Menandai kelas ini sebagai Spring Bean untuk autowiring
@RequiredArgsConstructor  // Lombok: generate constructor untuk final fields
@Slf4j  // Lombok: generate log object untuk logging
public class DebootLeaveWorkflowService {
```
- `@Service`: Menandai kelas ini sebagai service component di Spring
- `@RequiredArgsConstructor`: Lombok annotation yang otomatis membuat constructor dengan parameter untuk semua field yang `final`
- `@Slf4j`: Membuat objek `log` untuk logging menggunakan SLF4J

**2. Injeksi Flowable Services:**
```java
    private final RuntimeService runtimeService;    // Mengelola proses yang sedang berjalan
    private final TaskService taskService;           // Mengelola task/user task
    private final HistoryService historyService;    // Mengambil data history proses
    private final LeaveRequestRepository leaveRequestRepository;  // Repository JPA
    
    private static final String PROCESS_DEFINITION_KEY = "leaveApproval";  // Key proses
```
- `RuntimeService`: Service Flowable untuk memulai, menghentikan, dan mengelola proses yang berjalan
- `TaskService`: Service untuk mengelola user task (claim, complete, delegate)
- `HistoryService`: Service untuk mengambil data historis proses yang sudah selesai
- `PROCESS_DEFINITION_KEY`: Konstanta untuk identifikasi proses di Flowable

**3. Method submitRequest - Pengajuan Cuti:**
```java
    @Transactional
    public LeaveRequestDTO.Response submitRequest(LeaveRequestDTO.CreateRequest request) {
        log.info("Starting leave workflow using Flowable");
```
- `@Transactional`: Menjamin operasi database atomic (jika gagal, semua perubahan di-rollback)

**4. Simpan Data Leave Request:**
```java
        // Save leave request ke database
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employeeName(request.getEmployeeName())
                .employeeId(request.getEmployeeId())
                .department(request.getDepartment())
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)  // Status awal: PENDING
                .build();

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
```
- Membuat objek LeaveRequest menggunakan Builder pattern
- Status awal PENDING (menunggu persetujuan)
- `leaveRequestRepository.save()` menyimpan ke database dan mengembalikan entity dengan ID

**5. Mulai Flowable Process:**
```java
        // Start Flowable process directly (no BPMN file needed)
        // Perbedaan utama dengan Flowable Version: tidak perlu file BPMN!
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeName", request.getEmployeeName());
        variables.put("leaveType", request.getLeaveType().name());
        variables.put("startDate", request.getStartDate().toString());
        variables.put("endDate", request.getEndDate().toString());
        variables.put("requestId", saved.getId().toString());
        
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                PROCESS_DEFINITION_KEY,  // Process definition key
                saved.getId().toString(), // Business key (untuk korelasi)
                variables                // Variable yang bisa diakses di proses
        );
```
- **Kunci Perbedaan Deboot**: Menggunakan `RuntimeService.startProcessInstanceByKey()` tanpa file BPMN
- Flowable akan otomatis membuat proses dinamis berdasarkan key yang diberikan
- `variables`: Data yang akan disimpan bersama proses dan bisa diakses di user task
- `Business Key`: ID leave request untuk korelasi antara proses dan data aplikasi

**6. Update dengan Process Instance ID:**
```java
        // Update dengan process instance ID untuk tracking
        saved.setWorkflowInstanceId(processInstance.getId());
        leaveRequestRepository.save(saved);
        
        log.info("Process started with ID: {}", processInstance.getId());
        return mapToResponse(saved);
    }
```
- Menyimpan `processInstance.getId()` untuk tracking proses di masa depan
- Return response DTO ke client

**7. Method approveByManager - Persetujuan Manager:**
```java
    /**
     * Approve by manager - using TaskService directly
     */
    @Transactional
    public LeaveRequestDTO.Response approveByManager(UUID requestId, LeaveRequestDTO.ApprovalRequest approvalRequest) {
        // Ambil leave request dari database
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        
        // Validasi status harus PENDING
        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Request is not in pending status");
        }
```
- Validasi bahwa request exists dan statusnya PENDING

**8. Cari dan Selesaikan Manager Task:**
```java
        // Complete manager task via TaskService
        // Cari task yang assigned ke manager group
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("manager")  // Cari task untuk group "manager"
                .processInstanceId(leaveRequest.getWorkflowInstanceId())  // Filter by process
                .list();
        
        // Selesaikan semua task yang ditemukan
        for (Task task : tasks) {
            taskService.complete(task.getId(), Map.of(
                "approved", true,              // Variable: disetujui
                "comment", approvalRequest.getComment()  // Variable: komentar
            ));
        }
```
- `TaskService.createTaskQuery()`: Membuat query untuk mencari task
- `.taskCandidateGroup("manager")`: Filter task yang bisa diambil oleh group "manager"
- `.processInstanceId()`: Filter task berdasarkan proses spesifik
- `taskService.complete()`: Menyelesaikan task dan menyimpan variable keputusan

**9. Update Status di Database:**
```java
        // Update status leave request
        leaveRequest.setStatus(LeaveStatus.MANAGER_APPROVED);  // Ubah status
        leaveRequest.setManagerComment(approvalRequest.getComment());  // Simpan komentar
        leaveRequest.setApprovedByManager(approvalRequest.getApprovedBy());  // Siapa yang menyetujui
        leaveRequest.setManagerApprovalDate(LocalDateTime.now());  // Waktu persetujuan
        
        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        log.info("Leave request {} approved by manager", requestId);
        
        return mapToResponse(saved);
    }
}
```
- Update status menjadi MANAGER_APPROVED
- Simpan metadata persetujuan (komentar, approver, timestamp)
- Simpan ke database dan return response

**Perbedaan Utama Deboot vs Flowable Version:**

| Aspek | Flowable Version | Deboot Version |
|-------|------------------|----------------|
| Definisi Proses | File BPMN XML | Dynamic (tanpa file) |
| Process Definition | Di-deploy dari BPMN | Auto-create oleh Flowable |
| Task Routing | Diatur di BPMN | Diatur di kode Java |
| Visualisasi | Bisa lihat diagram | Tidak ada diagram |
| Flexibility | Terbatas BPMN | Lebih fleksibel |
| Maintenance | Perlu update BPMN | Ubah kode Java |

#### 7.3.5 Kelebihan

1. **Tidak perlu belajar BPMN** — Hanya perlu tahu Java dan Flowable API
2. **Boilerplate lebih sedikit** — Lebih ringkas dari Flowable penuh
3. **Pengembangan cepat** — Tidak perlu setup BPMN
4. **Menggunakan Flowable engine** — Tetap dapat manfaat skalabilitas
5. **Audit trail otomatis** — Via Flowable History
6. **Kode lebih bersih** — Semua logika di satu tempat

#### 7.3.6 Kekurangan

1. **Tidak ada visualisasi** — Tidak dapat melihat diagram workflow
2. **Komunitas lebih kecil** — Pendekatan service-based kurang populer
3. **Vendor lock-in** — Tetap terikat dengan Flowable
4. **Perlu memahami Flowable** — Tetap perlu belajar Flowable API

---

## 8. PERBANDINGAN TEKNIS

### 8.1 Statistik LOC (Lines of Code)

| Versi | Backend (Java) | Frontend (TS/TSX) | BPMN/XML | Total |
|-------|---------------|-------------------|----------|-------|
| **Manual** | 1,731 LOC | 1,364 LOC | - | **3,095 LOC** |
| **Flowable** | 1,563 LOC | 1,364 LOC | 96 LOC | **3,023 LOC** |
| **Deboot** | 1,482 LOC | 1,364 LOC | - | **2,846 LOC** |

**Analisis:**
- **Deboot Version** memiliki LOC terendah karena menggunakan pendekatan service yang lebih ringkas
- **Manual Version** memiliki LOC tertinggi karena implementasi state machine secara eksplisit
- **Frontend** identik untuk semua versi karena UI tidak bergantung pada pendekatan workflow

### 8.2 Perbandingan Fitur

| Fitur | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| **State Management** | | | |
| Enum-based | ✅ | ❌ | ❌ |
| BPMN Engine | ❌ | ✅ | ✅ (via Flowable) |
| Service-based | ❌ | ❌ | ✅ |
| **Audit Trail** | | | |
| Manual | ✅ | ❌ | ❌ |
| Built-in History | ❌ | ✅ | ✅ |
| **Visualization** | | | |
| BPMN Diagram | ❌ | ✅ | ❌ |
| Process Tracking | ❌ | ✅ | ✅ |
| **Development** | | | |
| Learning Curve | Rendah | Tinggi | Medium |
| Development Speed | Lambat | Medium | Cepat |
| **Enterprise** | | | |
| Scalability | Rendah | Tinggi | Tinggi |
| Compliance | Manual | Built-in | Built-in |
| Support | Internal | Community/Enterprise | Flowable (via library) |

### 8.3 Technology Stack

#### Backend

| Komponen | Manual Version | Flowable Version | Deboot Version |
|----------|----------------|------------------|----------------|
| Framework | Spring Boot 3.2.0 | Spring Boot 3.2.0 | Spring Boot 3.2.0 |
| BPM Engine | ❌ | Flowable 6.8.0 | Flowable 6.8.0 |
| Database | PostgreSQL | PostgreSQL | PostgreSQL |
| ORM | Spring Data JPA | Spring Data JPA | Spring Data JPA |
| Security | Spring Security + JWT | Spring Security + JWT | Spring Security + JWT |
| Build Tool | Maven | Maven | Maven |
| Java Version | 17+ | 17+ | 17+ |

#### Frontend (Identik untuk semua versi)

| Komponen | Semua Versi |
|----------|-------------|
| Framework | React 18 |
| Language | TypeScript |
| Build Tool | Vite |
| Styling | Tailwind CSS |
| HTTP Client | Axios |
| Routing | React Router |

### 8.4 Konfigurasi Port

| Versi | Backend Port | Frontend Port | Database |
|-------|--------------|---------------|----------|
| Manual | 8100 | 3100 | leave_manual_db |
| Flowable | 8200 | 3200 | leave_flowable_db |
| Deboot | 8300 | 3300 | leave_deboot_db |

### 8.5 REST API Endpoints

| Method | Endpoint | Role | Deskripsi |
|--------|----------|------|-----------|
| POST | /api/auth/register | Public | Registrasi user |
| POST | /api/auth/login | Public | Login user |
| GET | /api/leave-requests | EMPLOYEE, MANAGER, HR | Get all requests |
| GET | /api/leave-requests/my | EMPLOYEE | Get my requests |
| POST | /api/leave-requests | EMPLOYEE | Submit request |
| GET | /api/leave-requests/{id} | EMPLOYEE, MANAGER, HR | Get request detail |
| POST | /api/leave-requests/{id}/manager-approval | MANAGER | Manager approval |
| POST | /api/leave-requests/{id}/hr-approval | HR | HR approval |
| DELETE | /api/leave-requests/{id} | EMPLOYEE | Cancel request |

### 8.6 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Employee | employee1 | password123 |
| Employee | employee2 | password123 |
| Employee | employee3 | password123 |
| Manager | manager1 | password123 |
| Manager | manager2 | password123 |
| HR | hr1 | password123 |
| HR | hr2 | password123 |

---

## 9. LISENSI DAN PRICING

### 9.1 Manual Version

#### Lisensi
- **Tipe**: Proprietary (Internal)
- **Biaya**: Tidak ada (gratis untuk penggunaan internal)
- **Modifikasi**: Penuh (kode sepenuhnya milik perusahaan)
- **Dependency**: Spring Boot (Open Source, Apache License 2.0)

#### Pertimbangan

| Aspek | Keterangan |
|-------|-------------|
| Biaya Awal | Rendah (hanya development cost) |
| Biaya Maintenance | Tinggi (perlu developer internal) |
| Support | Tidak ada (internal only) |
| Compliance | Manual - perlu implementasi sendiri |
| Vendor Lock-in | Tidak ada |

### 9.2 Flowable BPM

#### Lisensi Flowable

Flowable tersedia dalam dua edisi:

##### Community Edition (Open Source)
- **Lisensi**: Apache License 2.0
- **Biaya**: Gratis
- **Fitur**: 
  - Core BPMN engine
  - DMN, Form, CMMN
  - REST API
  - Spring Boot integration
  - Basic history & audit

##### Enterprise Edition (Commercial)
- **Lisensi**: Commercial License
- **Biaya**: Mulai dari ~$5,000 - $50,000/tahun
- **Fitur Tambahan**:
  - Enterprise support
  - Advanced analytics
  - Clustering & high availability
  - Modeler visual
  - Audit & compliance tools
  - Process mining

#### Estimasi Harga dalam IDR (2024)

| Komponen | Estimasi Harga (IDR) |
|----------|---------------------|
| Community Edition | Gratis |
| Enterprise Basic | ~Rp 85.000.000/tahun |
| Enterprise Professional | ~Rp 170.000.000/tahun |
| Enterprise Premium | ~Rp 425.000.000/tahun |

#### Pertimbangan

| Aspek | Keterangan |
|-------|-------------|
| Biaya Awal | Rendah (Community Edition gratis) |
| Biaya Maintenance | Medium (bisa menggunakan Community) |
| Support | Community (gratis) / Enterprise (berbayar) |
| Compliance | Built-in audit trail |
| Vendor Lock-in | Medium - proprietary extensions |

### 9.3 Deboot Framework

> **Penting**: Deboot dalam proyek ini **diimplementasikan sebagai pendekatan service-based menggunakan Flowable secara langsung**, bukan sebagai framework terpisah.

#### Estimasi Harga dalam IDR (2024)

| Komponen | Estimasi Harga (IDR) |
|----------|---------------------|
| Flowable Community | Gratis |
| Development Cost | Sesuai kebutuhan |

#### Pertimbangan

| Aspek | Keterangan |
|-------|-------------|
| Biaya Awal | Rendah (menggunakan Flowable Community) |
| Biaya Maintenance | Rendah (boilerplate lebih sedikit) |
| Support | Community Flowable |
| Compliance | Built-in (menggunakan Flowable) |
| Vendor Lock-in | Tinggi (terikat Flowable) |

### 9.4 Perbandingan Total Cost of Ownership (TCO)

#### Estimasi Biaya 3 Tahun

| Versi | Tahun 1 | Tahun 2 | Tahun 3 | Total |
|-------|---------|---------|---------|-------|
| Manual | Rp 50.000.000 | Rp 25.000.000 | Rp 25.000.000 | Rp 100.000.000 |
| Flowable CE | Rp 50.000.000 | Rp 15.000.000 | Rp 15.000.000 | Rp 80.000.000 |
| Flowable Ent | Rp 250.000.000 | Rp 200.000.000 | Rp 200.000.000 | Rp 650.000.000 |
| Deboot | Rp 50.000.000 | Rp 15.000.000 | Rp 15.000.000 | Rp 80.000.000 |

*Catatan: Estimasi termasuk biaya development, maintenance, dan infrastruktur*

#### Asumsi Perhitungan

| Komponen | Manual | Flowable CE | Flowable Ent | Deboot |
|----------|--------|-------------|--------------|--------|
| Development Cost | Rp 30jt | Rp 30jt | Rp 30jt | Rp 30jt |
| Infrastructure | Rp 10jt/thn | Rp 10jt/thn | Rp 10jt/thn | Rp 10jt/thn |
| Maintenance | Rp 10jt/thn | Rp 5jt/thn | Rp 5jt/thn | Rp 5jt/thn |
| License Fee | - | - | Rp 180jt/thn | - |

### 9.5 Rekomendasi Berdasarkan Budget

| Budget | Rekomendasi |
|--------|-------------|
| < Rp 50jt/tahun | Manual atau Flowable CE |
| Rp 50-150jt/tahun | Deboot |
| > Rp 150jt/tahun | Flowable Enterprise |

### 9.6 Rekomendasi Berdasarkan Ukuran Organisasi

| Ukuran Organisasi | Rekomendasi |
|-------------------|-------------|
| Startup (< 10 org) | Manual |
| SMB (10-100 org) | Deboot atau Flowable CE |
| Enterprise (> 100 org) | Flowable Enterprise |

---

## 10. HASIL DAN PEMBAHASAN

### 10.1 Analisis Kompleksitas Kode

**Manual Version (1,731 LOC)**
- State machine dengan if-else explisit
- Setiap transisi status memerlukan validasi manual
- Audit trail diimplementasikan sendiri
- Cocok untuk workflow sederhana

**Flowable Version (1,563 LOC)**
- Logika workflow di BPMN XML
- Service layer lebih ringkas
- Audit trail otomatis dari engine

**Deboot Version (1,482 LOC)**
- Pendekatan paling ringkas
- Tidak perlu file BPMN
- Semua logika di service code

### 10.2 Analisis Enterprise

| Kriteria | Bobot | Manual | Flowable | Deboot |
|----------|-------|--------|----------|--------|
| Scalability | 15% | 2/5 | 5/5 | 4/5 |
| Maintainability | 15% | 2/5 | 4/5 | 4/5 |
| Team Collaboration | 10% | 2/5 | 5/5 | 4/5 |
| Process Agility | 15% | 2/5 | 5/5 | 4/5 |
| Compliance Readiness | 20% | 2/5 | 5/5 | 5/5 |
| Vendor Lock-in | 10% | 5/5 | 3/5 | 3/5 |
| Learning Curve | 15% | 4/5 | 2/5 | 4/5 |
| **Total Tertimbang** | 100% | **2.6** | **4.1** | **3.9** |

### 10.3 Rekomendasi Berdasarkan Use Case

| Use Case | Rekomendasi |
|----------|-------------|
| **Aplikasi CRUD sederhana** | Manual |
| **Workflow 2-3 langkah** | Semua |
| **Workflow kompleks** | Flowable / Deboot |
| **Enterprise scale** | Flowable |
| **Regulasi ketat** | Flowable |
| **MVP/POC cepat** | Deboot |
| **Tim kecil** | Manual / Deboot |
| **Budget terbatas** | Manual |
| **Visualisasi penting** | Flowable |
| **Pembelajaran BPM** | Flowable |

### 10.4 Analogi Perumpamaan

| Pendekatan | Analogi | Penjelasan |
|------------|---------|------------|
| **Manual** | **Resep Masakan Tradisional** | Semua resep ditulis tangan, chef harus menghafal langkah-langkah |
| **Flowable** | **Restoran dengan Sistem Profesional** | Proses masak terdokumentasi dalam SOP lengkap |
| **Deboot** | **Kitchen Modern dengan Chef AI** | Menggunakan peralatan modern tetapi dengan panduan lebih sederhana |

### 10.5 Hasil dan Pembahasan Pricing & Lisensi

Berdasarkan analisis yang telah dilakukan pada Bagian 9 (Lisensi dan Pricing), berikut adalah hasil dan pembahasan komprehensif:

#### 10.5.1 Perbandingan Biaya Langsung

| Komponen Biaya | Manual | Flowable CE | Flowable Enterprise | Deboot |
|---------------|--------|-------------|---------------------|--------|
| **Lisensi** | Gratis | Gratis | ~Rp 180jt/tahun | Gratis |
| **Development** | Rp 30jt | Rp 30jt | Rp 30jt | Rp 25jt |
| **Maintenance/thn** | Rp 10jt | Rp 5jt | Rp 5jt | Rp 5jt |
| **Infrastructure/thn** | Rp 10jt | Rp 10jt | Rp 10jt | Rp 10jt |
| **Support** | Internal | Community | Dedicated | Community |
| **TCO 3 Tahun** | Rp 100jt | Rp 80jt | Rp 650jt | Rp 80jt |

#### 10.5.2 Pembahasan Hasil Analisis Pricing

**1. Manual Version - Solusi Termurah untuk Skala Kecil**
- Tidak memerlukan biaya lisensi karena menggunakan implementasi kustom
- Biaya tertinggi pada maintenance karena perlu developer khusus
- Cocok untuk organisasi dengan tim IT yang competent
- Namun, biaya tersembunyi berupa risiko bug dan waktu development

**2. Flowable Community Edition - Best Value**
- Gratis dengan fitur BPMN lengkap
- Cocok untuk sebagian besar use case enterprise
- Kelemahan: tidak ada dedicated support dan visual modeler
- Komunitas aktif memberikan suporte melalui forum dan dokumentasi

**3. Flowable Enterprise - Investasi untuk Skala Besar**
- Biaya signifikan (~Rp 180jt/tahun) tetapi memberikan:
  - Dedicated support 24/7
  - Visual modeler untuk business analyst
  - Clustering dan high availability
  - Audit dan compliance tools bawaan
  - Process mining capabilities
- Worth it untuk organisasi dengan > 100 employee

**4. Deboot - Alternatif Cost-Effective**
- Menggunakan Flowable Community (gratis)
- Biaya development lebih rendah dari Flowable tradisional
- Namun tetap memiliki keterbatasan yang sama dengan Flowable CE
- Trade-off: lebih sedikit LOC tapi tanpa visualisasi workflow

#### 10.5.3 Rekomendasi Pricing Berdasarkan Skenario

| Skenario | Rekomendasi | Alasan |
|----------|-------------|--------|
| **Startup dengan budget ketat** | Manual | Biaya terkecil, kontrol penuh |
| **SMB yang tumbuh cepat** | Deboot | Fleksibilitas + biaya terkontrol |
| **Mid-size company** | Flowable CE | Fitur lengkap, gratis, komunitas aktif |
| **Large enterprise** | Flowable Enterprise | Compliance, support, HA至关重要 |
| **Project-based company** | Deboot | Rapid development, mudah di-maintain |

#### 10.5.4 Faktor Tersembunyi yang Perlu Dipertimbangkan

| Faktor | Manual | Flowable CE | Deboot |
|--------|--------|--------------|--------|
| **Learning Curve** | Rendah | Tinggi | Medium |
| **Onboarding Time** | 1-2 minggu | 4-8 minggu | 2-4 minggu |
| **Dokumentasi Internal** | Perlu dibuat | Ada contoh | Perlu dibuat |
| **Tooling Cost** | Standard IDE | + BPMN editor | Standard IDE |
| **Team Skill Required** | Java Core | Java + BPMN | Java + Flowable API |

#### 10.5.5 Kesimpulan Pricing dan Lisensi

1. **Untuk PT. Len Industri**: Dengan skala enterprise dan kebutuhan compliance yang ketat, **Flowable Enterprise** adalah pilihan tepat meskipun biayanya signifikan. Namun untuk MVP atau POC, **Deboot** dapat menjadi pilihan yang lebih cost-effective.

2. **Budget Allocation**: Alokasikan budget untuk:
   - Training tim (20% dari development cost)
   - Dokumentasi dan knowledge transfer (10%)
   - Infrastructure dan monitoring (15% per tahun)
   - Maintenance dan updates (10% per tahun)

3. **Long-term Consideration**: Pertimbangkan total cost bukan hanya lisensi. Manual version，看起来便宜 tetapi biaya maintenance dan risiko bisa lebih tinggi dalam jangka panjang.

### 10.6 Code-based vs Web Modeler

Flowable dapat digunakan dengan dua cara:

1. **Code-based** (digunakan di proyek ini):
   - Tulis kode Java/XML
   - Lebih cocok untuk developer
   - CI/CD friendly

2. **Web Modeler** (Flowable Modeler):
   - Drag & drop di browser
   - Untuk business analyst
   - Perubahan cepat tanpa coding

---

## 11. KESIMPULAN DAN SARAN

### 11.1 Kesimpulan

Berdasarkan analisis yang telah dilakukan:

1. **Manual Version** cocok untuk:
   - Proyek pembelajaran dasar workflow
   - Aplikasi dengan workflow sangat sederhana
   - Tim dengan keterbatasan resources

2. **Flowable Version** cocok untuk:
   - Enterprise dengan kebutuhan kompleks
   - Kebutuhan compliance dan audit ketat
   - Organisasi yang sudah familiar dengan BPMN

3. **Deboot Version** cocok untuk:
   - Tim yang ingin manfaat Flowable tanpa kompleksitas BPMN
   - Pengembangan MVP/POC yang cepat
   - Proyek dengan timeline ketat

### 11.2 Catatan Penting

- Semua tiga versi mengimplementasikan use case yang **identik** untuk perbandingan yang adil
- Frontend **identik** untuk semua versi karena UI tidak bergantung pada pendekatan backend
- Perbedaan utama terletak di **backend (workflow implementation)**
- Deboot adalah **pendekatan service-based**, bukan framework terpisah

### 11.3 Penjelasan "Vendor Support"

| Versi | Penjelasan Vendor |
|-------|-------------------|
| **Manual** | Tidak ada vendor (implementasi internal oleh tim sendiri) |
| **Flowable** | Vendor = Flowable GmbH (Community via forum / Enterprise via kontrak) |
| **Deboot** | Support dari Flowable GmbH karena menggunakan Flowable library |

### 11.4 Saran

1. **Untuk pembelajaran**: Mulai dari Manual Version untuk memahami dasar workflow
2. **Untuk production skala kecil-sedang**: Pertimbangkan Deboot Version
3. **Untuk enterprise dengan compliance ketat**: Gunakan Flowable Version
4. **Untuk kebutuhan visualisasi**: Flowable Version dengan BPMN Designer

---

## DAFTAR DOKUMENTASI

Dokumentasi lengkap tersedia di folder `docs/`:

| File | Deskripsi |
|------|-----------|
| `arsitektur.md` | Penjelasan arsitektur setiap versi |
| `cara-kerja-workflow.md` | Cara kerja workflow di setiap versi |
| `bukti-implementasi.md` | Bukti kode implementasi |
| `laporan-loc.md` | Laporan Lines of Code |
| `lisensi-dan-pricing.md` | Analisis lisensi dan harga |
| `analisis-enterprise.md` | Analisis aspek enterprise |
| `perbandingan.md` | Matrix perbandingan lengkap |
| `audit-komprehensif.md` | Audit lengkap semua versi |

---

## DISCLAIMER

> Proyek ini dibuat sebagai **proyek riset internal** selama magang di PT. Len Industri (Persero).  
> Tujuan utama adalah pembelajaran dan penelitian, bukan untuk produksi.  
> Deboot Framework dalam konteks ini diimplementasikan sebagai pendekatan service-based di atas Flowable (bukan framework eksternal), untuk menunjukkan bagaimana Flowable dapat digunakan dengan cara yang lebih sederhana.

---

**Dibuat oleh Alifito Rabbani Cahyono**  
**PT. Len Industri (Persero) — Backend Engineer Intern**  
