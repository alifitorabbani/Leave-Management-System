# Audit Komprehensif - Leave Workflow Comparison

**Proyek: Workflow Engine Comparison**  
**Perusahaan: PT. Len Industri (epersero)**  
**Pengembang: Alifito Rabbani Cahyono**  
**Tanggal Audit: 2026-02-26**  
**Konteks: Proyek magang — Riset internal workflow orchestration**

---

## 1. Ringkasan Eksekutif

### 1.1 Latar Belakang Proyek

Proyek ini dibuat sebagai bagian dari proyek penelitian dan pengembangan internal di **PT. Len Industri (epersero)**, sebuah Badan Usaha Milik Negara (BUMN) yang bergerak di bidang teknologi dan industri pertahanan. Proyek ini bertujuan untuk membandingkan tiga pendekatan implementasi workflow untuk sistem pengajuan cuti perusahaan.

### 1.2 Tujuan Audit

Audit ini dilakukan untuk:
1. Memahami secara mendalam setiap komponen dari ketiga versi
2. Mengidentifikasi fitur, kelemahan, dan karakteristik masing-masing
3. Menyediakan dokumentasi lengkap untuk keperluan riset dan pembelajaran
4. Memberikan rekomendasi berdasarkan kebutuhan spesifik

### 1.3 Scope Audit

| Komponen | Manual Version | Flowable Version | Deboot Version |
|----------|----------------|------------------|----------------|
| Backend | Spring Boot 3.2.0 | Spring Boot 3.2.0 + Flowable 6.8.0 | Spring Boot 3.2.0 + Flowable 6.8.0 |
| Frontend | React 18 + Vite | React 18 + Vite | React 18 + Vite |
| Database | PostgreSQL | PostgreSQL | PostgreSQL |
| Keamanan | JWT + Spring Security | JWT + Spring Security | JWT + Spring Security |

---

## 2. Detail Audit Manual Version

### 2.1 Informasi Proyek

| Aspek | Detail |
|-------|--------|
| **Nama** | Manual Version Backend |
| **Artifact ID** | manual-version-backend |
| **Versi** | 1.0.0 |
| **Framework** | Spring Boot 3.2.0 |
| **Bahasa** | Java 17 |
| **Port Default** | 8100 (Backend), 3100 (Frontend) |
| **Database** | leave_manual_db |

### 2.2 Struktur Direktori Backend

```
manual-version/backend/
├── pom.xml
├── src/main/java/com/len/leave/manual/
│   ├── ManualVersionApplication.java
│   ├── aspect/
│   │   └── LoggingAspect.java
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   └── LeaveRequestController.java
│   ├── dto/
│   │   ├── ApiResponse.java
│   │   ├── AuthResponse.java
│   │   ├── LeaveRequestDTO.java
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   └── UserResponse.java
│   ├── entity/
│   │   ├── AuditLog.java
│   │   ├── LeaveRequest.java
│   │   ├── LeaveStatus.java
│   │   ├── LeaveType.java
│   │   ├── User.java
│   │   └── UserRole.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── InvalidStateTransitionException.java
│   │   └── LeaveRequestNotFoundException.java
│   ├── repository/
│   │   ├── AuditLogRepository.java
│   │   ├── LeaveRequestRepository.java
│   │   └── UserRepository.java
│   ├── security/
│   │   ├── CustomUserDetails.java
│   │   ├── CustomUserDetailsService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtUtils.java
│   └── service/
│       ├── AuthService.java
│       └── LeaveRequestService.java
└── src/main/resources/
    └── application.properties
```

### 2.3 Dependency Maven

| Group ID | Artifact ID | Versi | Tipe |
|----------|-------------|-------|------|
| org.springframework.boot | spring-boot-starter-web | 3.2.0 | compile |
| org.springframework.boot | spring-boot-starter-data-jpa | 3.2.0 | compile |
| org.springframework.boot | spring-boot-starter-validation | 3.2.0 | compile |
| org.springframework.boot | spring-boot-starter-aop | 3.2.0 | compile |
| org.springframework.boot | spring-boot-starter-security | 3.2.0 | compile |
| io.jsonwebtoken | jjwt-api | 0.12.3 | compile |
| io.jsonwebtoken | jjwt-impl | 0.12.3 | runtime |
| io.jsonwebtoken | jjwt-jackson | 0.12.3 | runtime |
| org.postgresql | postgresql | - | runtime |
| org.projectlombok | lombok | - | optional |
| com.h2database | h2 | - | runtime |
| com.fasterxml.jackson.datatype | jackson-datatype-jsr310 | - | compile |

### 2.4 Fitur Utama

#### 2.4.1 Fitur Authentication & Authorization

| Fitur | Status | Deskripsi |
|-------|--------|-----------|
| User Registration | ✅ | Registrasi user dengan role (EMPLOYEE, MANAGER, HR) |
| User Login | ✅ | Login dengan JWT token |
| JWT Authentication | ✅ | Token-based authentication |
| Role-based Access Control | ✅ | Spring Security dengan role-based |
| Password Encryption | ✅ | BCrypt password encoder |

#### 2.4.2 Fitur Leave Request

| Fitur | Status | Deskripsi |
|-------|--------|-----------|
| Submit Request | ✅ | Pengajuan cuti baru |
| View All Requests | ✅ | Melihat semua pengajuan |
| View My Requests | ✅ | Melihat pengajuan diri sendiri |
| Manager Approval | ✅ | Approval/rejection oleh manager |
| HR Approval | ✅ | Approval/rejection oleh HR |
| Cancel Request | ✅ | Pembatalan request oleh employee |

#### 2.4.3 Fitur Workflow

| Fitur | Status | Deskripsi |
|-------|--------|-----------|
| State Machine | ✅ | Implementasi dengan Enum LeaveStatus |
| State Validation | ✅ | Validasi transisi status dengan if-else |
| Audit Trail | ✅ | Logging setiap aksi ke tabel audit_logs |
| Status History | ✅ | Melihat riwayat perubahan status |

#### 2.4.4 Entity Structure

**LeaveRequest Entity:**
- id (UUID)
- employeeName (String)
- employeeId (String)
- department (String)
- leaveType (Enum: ANNUAL, SICK, PERSONAL, MATERNITY)
- startDate (LocalDate)
- endDate (LocalDate)
- reason (String)
- status (Enum: PENDING, MANAGER_APPROVED, MANAGER_REJECTED, HR_APPROVED, HR_REJECTED)
- managerComment (String)
- hrComment (String)
- approvedByManager (String)
- approvedByHR (String)
- managerApprovalDate (LocalDateTime)
- hrApprovalDate (LocalDateTime)
- totalDays (Integer)
- createdAt (LocalDateTime)
- updatedAt (LocalDateTime)

**LeaveStatus Enum:**
- PENDING - Menunggu approval manager
- MANAGER_APPROVED - Approved manager, menunggu HR
- MANAGER_REJECTED - Ditolak manager (final)
- HR_APPROVED - Approved HR (final)
- HR_REJECTED - Ditolak HR (final)

**User Entity:**
- id (UUID)
- username (String)
- password (String - encrypted)
- email (String)
- role (Enum: EMPLOYEE, MANAGER, HR)
- department (String)
- createdAt (LocalDateTime)

**AuditLog Entity:**
- id (UUID)
- leaveRequestId (UUID)
- action (Enum)
- performedBy (String)
- role (String)
- comment (String)
- createdAt (LocalDateTime)

### 2.5 REST API Endpoints

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

### 2.6 Sample Data (DataInitializer)

| User Type | Username | Password | Role |
|-----------|-----------|----------|------|
| Employee | employee1 | password123 | EMPLOYEE |
| Employee | employee2 | password123 | EMPLOYEE |
| Employee | employee3 | password123 | EMPLOYEE |
| Manager | manager1 | password123 | MANAGER |
| Manager | manager2 | password123 | MANAGER |
| HR | hr1 | password123 | HR |
| HR | hr2 | password123 | HR |

### 2.7 Kelebihan

1. **Tidak ada dependency eksternal** - Hanya menggunakan Spring Boot
2. **Kontrol penuh** - Semua logika bisnis dapat dimodifikasi dengan mudah
3. **Tidak ada vendor lock-in** - Tidak tergantung pada framework tertentu
4. **Mudah dipahami** - Cocok untuk pembelajaran dasar workflow
5. **Debugging langsung** - Tidak ada abstraksi yang menyembunyikan logika
6. **Performance optimal** - Tidak ada overhead dari engine eksternal

### 2.8 Kekurangan

1. **Kode kompleks untuk workflow kompleks** - Jika workflow rumit, if-else akan sangat banyak
2. **Tidak ada visualisasi** - Tidak dapat melihat alur workflow secara visual
3. **Audit trail manual** - Harus membuat implementasi audit sendiri
4. **Scaling terbatas** - Untuk aplikasi besar, perlu esfuerzo tambahan
5. **Perubahan workflow sulit** - Perlu ubah kode untuk ubah alur
6. **Risiko inkonsistensi** - state更容易-terjadi inconsistency jika tidak hati-hati

---

## 3. Detail Audit Flowable Version

### 3.1 Informasi Proyek

| Aspek | Detail |
|-------|--------|
| **Nama** | Flowable Version Backend |
| **Artifact ID** | flowable-version-backend |
| **Versi** | 1.0.0 |
| **Framework** | Spring Boot 3.2.0 + Flowable 6.8.0 |
| **Bahasa** | Java 17 |
| **Port Default** | 8200 (Backend), 3200 (Frontend) |
| **Database** | leave_flowable_db |

### 3.2 Struktur Direktori Backend

```
flowable-version/backend/
├── pom.xml
├── bpmn/
│   └── leave-approval.bpmn20.xml
├── src/main/java/com/len/leave/flowable/
│   ├── FlowableVersionApplication.java
│   ├── config/
│   │   ├── FlowableConfig.java
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   └── LeaveRequestController.java
│   ├── dto/
│   │   ├── ApiResponse.java
│   │   ├── AuthResponse.java
│   │   ├── LeaveRequestDTO.java
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   └── UserResponse.java
│   ├── entity/
│   │   ├── AuditLog.java
│   │   ├── LeaveRequest.java
│   │   ├── LeaveStatus.java
│   │   ├── LeaveType.java
│   │   ├── User.java
│   │   └── UserRole.java
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   ├── repository/
│   │   ├── AuditLogRepository.java
│   │   ├── LeaveRequestRepository.java
│   │   └── UserRepository.java
│   ├── security/
│   │   ├── CustomUserDetails.java
│   │   ├── CustomUserDetailsService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtUtils.java
│   └── service/
│       ├── AuthService.java
│       └── LeaveWorkflowService.java
└── src/main/resources/
    └── application.properties
```

### 3.3 Dependency Maven (Tambahan)

| Group ID | Artifact ID | Versi | Tipe |
|----------|-------------|-------|------|
| org.flowable | flowable-spring-boot-starter | 6.8.0 | compile |

### 3.4 Fitur Utama

#### 3.4.1 Fitur Authentication & Authorization

| Fitur | Status | Deskripsi |
|-------|--------|-----------|
| User Registration | ✅ | Registrasi user dengan role |
| User Login | ✅ | Login dengan JWT token |
| JWT Authentication | ✅ | Token-based authentication |
| Role-based Access Control | ✅ | Spring Security dengan role-based |

#### 3.4.2 Fitur Leave Request

| Fitur | Status | Deskripsi |
|-------|--------|-----------|
| Submit Request | ✅ | Pengajuan cuti + start Flowable process |
| View All Requests | ✅ | Melihat semua pengajuan |
| View My Requests | ✅ | Melihat pengajuan diri sendiri |
| Manager Approval | ✅ | Via Flowable task completion |
| HR Approval | ✅ | Via Flowable task completion |
| Cancel Request | ✅ | Via Flowable process termination |

#### 3.4.3 Fitur Workflow (Flowable)

| Fitur | Status | Deskripsi |
|-------|--------|-----------|
| BPMN Process Definition | ✅ | leave-approval.bpmn20.xml |
| RuntimeService | ✅ | Start/process instances |
| TaskService | ✅ | Query/complete user tasks |
| HistoryService | ✅ | Audit trail otomatis |
| Candidate Groups | ✅ | Task assignment ke manager/hr |
| Process Variables | ✅ | Simpan data di process |
| Automatic Audit | ✅ | Semua aktivitas di-log otomatis |

#### 3.4.4 BPMN Process Definition

**File:** `flowable-version/bpmn/leave-approval.bpmn20.xml`

```xml
<!-- Process: leaveApproval -->
<!-- Start Event → Manager Approval → HR Approval → End -->

<!-- User Tasks -->
- managerApprovalTask (candidateGroup: managers)
- hrApprovalTask (candidateGroup: hr)

<!-- Gateways -->
- managerDecisionGateway (exclusive)
- hrDecisionGateway (exclusive)

<!-- End Events -->
- managerRejectedEnd
- hrApprovedEnd  
- hrRejectedEnd
```

### 3.5 REST API Endpoints

Sama dengan Manual Version, dengan tambahan:
- Task query endpoints via Flowable TaskService

### 3.6 Sample Data

Sama dengan Manual Version.

### 3.7 Kelebihan

1. **Standar industri** - Menggunakan BPMN 2.0 standar
2. **Visual workflow** - Dapat divisualisasikan dengan Flowable Designer
3. **Audit trail otomatis** - Semua aktivitas di-log oleh Flowable
4. **Hot deployment** - Workflow dapat diubah tanpa redeploy kode
5. **Process mining** - Dapat menganalisis process performance
6. **Scalability tinggi** - Mendukung clustering
7. **Komunitas besar** - Dokumentasi dan support yang baik

### 3.8 Kekurangan

1. **Kurva pembelajaran tinggi** - Perlu pelajari BPMN dan Flowable API
2. **Overhead untuk proyek kecil** - Terlalu banyak fitur untuk aplikasi sederhana
3. **Konfigurasi kompleks** - Lebih banyak setup dibanding manual
4. **Resource consumption** - Memerlukan lebih banyak resource
5. **Vendor lock-in** - Terikat dengan ekosistem Flowable

---

## 4. Detail Audit Deboot Version

### 4.1 Informasi Proyek

| Aspek | Detail |
|-------|--------|
| **Nama** | Deboot Version Backend |
| **Artifact ID** | deboot-version-backend |
| **Versi** | 1.0.0 |
| **Framework** | Spring Boot 3.2.0 + Flowable 6.8.0 |
| **Bahasa** | Java 17 |
| **Port Default** | 8300 (Backend), 3300 (Frontend) |
| **Database** | leave_deboot_db |

### 4.2 Catatan Penting

Deboot Version diimplementasikan sebagai **pendekatan service-based menggunakan Flowable secara langsung** tanpa BPMN XML. Ini adalah cara penyederhanaan penggunaan Flowable tanpa harus mempelajari BPMN.

### 4.3 Struktur Direktori Backend

```
deboot-version/backend/
├── pom.xml
├── src/main/java/com/len/leave/deboot/
│   ├── DebootVersionApplication.java
│   ├── DataInitializer.java
│   ├── config/
│   │   ├── FlowableConfig.java
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   └── LeaveRequestController.java
│   ├── dto/
│   │   ├── ApiResponse.java
│   │   ├── AuthResponse.java
│   │   ├── LeaveRequestDTO.java
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   └── UserResponse.java
│   ├── entity/
│   │   ├── AuditLog.java
│   │   ├── LeaveRequest.java
│   │   ├── LeaveStatus.java
│   │   ├── LeaveType.java
│   │   ├── User.java
│   │   └── UserRole.java
│   ├── repository/
│   │   ├── AuditLogRepository.java
│   │   ├── LeaveRequestRepository.java
│   │   └── UserRepository.java
│   ├── security/
│   │   ├── CustomUserDetails.java
│   │   ├── CustomUserDetailsService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtUtils.java
│   └── service/
│       ├── AuthService.java
│       └── DebootLeaveWorkflowService.java
└── src/main/resources/
    └── application.properties
```

### 4.4 Dependency Maven

Sama dengan Flowable Version (menggunakan Flowable 6.8.0).

### 4.5 Perbedaan dengan Flowable Version

| Aspek | Flowable Version | Deboot Version |
|-------|------------------|----------------|
| Definisi Workflow | BPMN 2.0 XML | Service Code |
| Process Deployment | Deploy BPMN XML | Auto-start via Flowable |
| Task Definition | Di BPMN | Via TaskService Query |
| Pendekatan | Declarative (XML) | Imperative (Code-first) |
| Visualisasi | ✅ BPMN Diagram | ❌ Tidak ada |
| LOC (Backend) | 1,563 | 1,482 |

### 4.6 Fitur Utama

#### 4.6.1 Fitur Authentication & Authorization

| Fitur | Status | Deskripsi |
|-------|--------|-----------|
| User Registration | ✅ | Registrasi user dengan role |
| User Login | ✅ | Login dengan JWT token |
| JWT Authentication | ✅ | Token-based authentication |
| Role-based Access Control | ✅ | Spring Security dengan role-based |

#### 4.6.2 Fitur Leave Request

| Fitur | Status | Deskripsi |
|-------|--------|-----------|
| Submit Request | ✅ | Pengajuan cuti + start process |
| View All Requests | ✅ | Melihat semua pengajuan |
| View My Requests | ✅ | Melihat pengajuan diri sendiri |
| Manager Approval | ✅ | Via TaskService |
| HR Approval | ✅ | Via TaskService |

#### 4.6.3 Fitur Workflow (Deboot Approach)

| Fitur | Status | Deskripsi |
|-------|--------|-----------|
| Direct Flowable Usage | ✅ | RuntimeService, TaskService langsung |
| Service-based | ✅ | Semua logika di service class |
| No BPMN XML | ✅ | Tidak perlu file definisi proses |
| Process Variables | ✅ | Simpan data di process |
| Automatic Audit | ✅ | Via Flowable History |

### 4.7 Implementasi Service

```java
@Service
public class DebootLeaveWorkflowService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    
    // Proses dimulai langsung via RuntimeService
    ProcessInstance pi = runtimeService.startProcessInstanceByKey(
        "leaveApproval", 
        businessKey, 
        variables
    );
    
    // Task di-query via TaskService
    List<Task> tasks = taskService.createTaskQuery()
        .taskCandidateGroup("manager")
        .list();
}
```

### 4.8 REST API Endpoints

Sama dengan Manual Version.

### 4.9 Sample Data

Sama dengan Manual Version.

### 4.10 Kelebihan

1. **Tidak perlu belajar BPMN** - Hanya perlu tahu Java dan Flowable API
2. **Boilerplate lebih sedikit** - Lebih ringkas dari Flowable penuh
3. **Pengembangan cepat** - Tidak perlu setup BPMN
4. **Menggunakan Flowable engine** - Tetap dapat manfaat skalabilitas
5. **Audit trail otomatis** - Via Flowable History
6. **Kode lebih bersih** - Semua logika di satu tempat

### 4.11 Kekurangan

1. **Tidak ada visualisasi** - Tidak dapat melihat diagram workflow
2. **Komunitas lebih kecil** - Pendekatan service-based kurang populer
3. **Vendor lock-in** - Tetap terikat dengan Flowable
4. **Perlu memahami Flowable** - Tetap perlu belajar Flowable API

---

## 5. Perbandingan Komprehensif

### 5.1 Perbandingan Fitur

| Fitur | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| **Authentication** | | | |
| JWT Auth | ✅ | ✅ | ✅ |
| Role-based Access | ✅ | ✅ | ✅ |
| Registration | ✅ | ✅ | ✅ |
| **Workflow** | | | |
| State Machine | Enum | BPMN | Service |
| Visualisasi | ❌ | ✅ | ❌ |
| Hot Deployment | ❌ | ✅ | ❌ |
| Process Mining | ❌ | ✅ | ❌ |
| **Audit** | | | |
| Manual Audit | ✅ | ❌ | ❌ |
| Auto History | ❌ | ✅ | ✅ |
| **Scalability** | | | |
| Clustering | ❌ | ✅ | ✅ |
| **Complexity** | | | |
| LOC (Backend) | 1,731 | 1,563 | 1,482 |
| Learning Curve | Rendah | Tinggi | Medium |

### 5.2 Perbandingan Teknologi

| Aspek | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| Spring Boot | 3.2.0 | 3.2.0 | 3.2.0 |
| Flowable | ❌ | 6.8.0 | 6.8.0 |
| Database | PostgreSQL | PostgreSQL | PostgreSQL |
| Java | 17+ | 17+ | 17+ |
| Frontend | React 18 | React 18 | React 18 |

### 5.3 Perbandingan API Endpoints

| Endpoint | Manual | Flowable | Deboot |
|----------|--------|----------|--------|
| POST /api/auth/register | ✅ | ✅ | ✅ |
| POST /api/auth/login | ✅ | ✅ | ✅ |
| GET /api/leave-requests | ✅ | ✅ | ✅ |
| POST /api/leave-requests | ✅ | ✅ | ✅ |
| POST /api/leave-requests/{id}/manager-approval | ✅ | ✅ | ✅ |
| POST /api/leave-requests/{id}/hr-approval | ✅ | ✅ | ✅ |

---

## 6. Rekomendasi Penggunaan

### 6.1 Berdasarkan Jenis Organisasi

| Jenis Organisasi | Rekomendasi | Alasan |
|------------------|-------------|--------|
| Startup (< 10 orang) | Manual | Simpel, biaya rendah |
| SMB (10-100 orang) | Deboot | Seimbang fitur dan kesederhanaan |
| Enterprise (> 100 orang) | Flowable | Skalabilitas, compliance |

### 6.2 Berdasarkan Kebutuhan

| Kebutuhan | Rekomendasi |
|-----------|-------------|
| Pembelajaran | Manual → Flowable → Deboot |
| MVP/POC cepat | Deboot |
| Visualisasi penting | Flowable |
| Budget sangat terbatas | Manual |
| Compliance ketat | Flowable Enterprise |
| Tim kecil | Manual atau Deboot |

---

## 7. Kesimpulan Audit

### 7.1 Ringkasan Temuan

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

### 7.2 Catatan Penting

- Semua tiga versi mengimplementasikan use case yang **identik** untuk perbandingan yang adil
- Frontend **identik** untuk semua versi karena UI tidak bergantung pada pendekatan backend
- Perbedaan utama terletak di **backend (workflow implementation)**
- Deboot adalah **pendekatan service-based**, bukan framework terpisah

---

## 8. Lampiran

### 8.1 Port Configuration Summary

| Versi | Backend Port | Frontend Port | Database |
|-------|--------------|---------------|----------|
| Manual | 8100 | 3100 | leave_manual_db |
| Flowable | 8200 | 3200 | leave_flowable_db |
| Deboot | 8300 | 3300 | leave_deboot_db |

### 8.2 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Employee | employee1 | password123 |
| Manager | manager1 | password123 |
| HR | hr1 | password123 |

---

**Dokumen ini terakhir diperbarui: 2026-02-26**
**Pengembang: Alifito Rabbani Cahyono - PT. Len Industri (epersero)**
