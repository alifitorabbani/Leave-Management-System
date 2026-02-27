# Perbandingan Keseluruhan

**Proyek: Workflow Engine Comparison**  
**Perusahaan: PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono**  
**Konteks: Proyek magang — Riset internal workflow orchestration**
**Tanggal Audit: 2026-02-26**

---

## 1. Matrix Perbandingan

### 1.1 Fitur Utama

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
| LOC (Backend) | 1,731 | 1,563 | 1,482 |
| Learning Curve | Rendah | Tinggi | Medium |
| Development Speed | Lambat | Medium | Cepat |
| **Enterprise** | | | |
| Scalability | Rendah | Tinggi | Tinggi |
| Compliance | Manual | Built-in | Built-in |
| Support | Internal | Community/Enterprise | Vendor |
| **Integration** | | | |
| REST API | ✅ | ✅ | ✅ |
| Database | PostgreSQL | PostgreSQL | PostgreSQL |
| Spring Boot | ✅ | ✅ | ✅ |

### 1.2 Fitur Lengkap per Versi

#### Manual Version
- ✅ JWT Authentication & Authorization
- ✅ User Registration & Login
- ✅ Submit Leave Request
- ✅ Manager Approval/Rejection
- ✅ HR Approval/Rejection
- ✅ State Machine (Enum-based)
- ✅ Manual Audit Trail (tabel audit_logs)
- ✅ Role-based Access Control (Employee, Manager, HR)
- ✅ AOP Logging
- ✅ Global Exception Handling

#### Flowable Version  
- ✅ JWT Authentication & Authorization
- ✅ User Registration & Login
- ✅ Submit Leave Request (dengan BPMN process)
- ✅ Manager Approval/Rejection (via Flowable Task)
- ✅ HR Approval/Rejection (via Flowable Task)
- ✅ BPMN 2.0 Process Definition
- ✅ Automatic History Tracking
- ✅ Process Variables
- ✅ Candidate Groups (managers, hr)
- ✅ Flowable REST API
- ✅ Process Deployment Management

#### Deboot Version
- ✅ JWT Authentication & Authorization
- ✅ User Registration & Login
- ✅ Submit Leave Request (direct Flowable)
- ✅ Manager Approval/Rejection (via TaskService)
- ✅ HR Approval/Rejection (via TaskService)
- ✅ Service-based Workflow (no BPMN XML)
- ✅ Automatic History (via Flowable)
- ✅ Direct Flowable API Usage
- ✅ RuntimeService Integration
- ✅ TaskService Integration

### 1.2 Technology Stack

| Komponen | Manual | Flowable | Deboot |
|----------|--------|----------|--------|
| Backend Framework | Spring Boot 3.2.0 | Spring Boot 3.2.0 | Spring Boot 3.2.0 |
| BPM Engine | ❌ | Flowable 6.8.0 | Flowable 6.8.0 |
| Database | PostgreSQL | PostgreSQL | PostgreSQL |
| ORM | Spring Data JPA | Spring Data JPA | Spring Data JPA |
| Build Tool | Maven | Maven | Maven |
| Java Version | 17+ | 17+ | 17+ |
| Frontend | React + Vite | React + Vite | React + Vite |

### 1.3 Port Configuration

| Versi | Backend Port | Frontend Port | Database |
|-------|--------------|---------------|----------|
| Manual | 8100 | 3100 | leave_manual_db |
| Flowable | 8200 | 3200 | leave_flowable_db |
| Deboot | 8300 | 3300 | leave_deboot_db |

---

## 2. Kelebihan dan Kekurangan

### 2.1 Manual Version

**Kelebihan:**
- Tidak memerlukan library tambahan (hanya Spring Boot)
- Kontrol penuh atas logika bisnis
- Tidak ada vendor lock-in
- mudah dipahami oleh developer Java dasar
- Tidak perlu mempelajari BPMN
- Biaya lisensi gratis
- Debugging langsung (tidak ada abstraksi)

**Kekurangan:**
- Sulit di-maintain untuk workflow kompleks
- Tidak ada visualisasi workflow
- Audit trail harus implementasi manual
- Tidak cocok untuk scaling besar
- Perubahan workflow memerlukan kode change
- Risiko inkonsistensi state lebih tinggi

### 2.2 Flowable Version

**Kelebihan:**
- Standar industri (BPMN 2.0)
- Visual workflow dengan modeler
- Audit trail komprehensif
- Komunitas besar dan dokumentasi lengkap
- Process mining capabilities
- Hot deployment untuk perubahan workflow
- Compliance tools built-in

**Kekurangan:**
- Kurva pembelajaran tinggi
- Perlu mempelajari BPMN
- Overhead untuk proyek kecil
- Konfigurasi kompleks
- Resource consumption lebih tinggi

### 2.3 Deboot Version

**Kelebihan:**
- Tidak memerlukan learn BPMN XML
- Annotation-driven (mengerti developer)
- Boilerplate lebih sedikit
- Pengembangan cepat
- Menggunakan Flowable engine (skalabilitas tinggi)
- Audit trail otomatis dari Flowable

**Kekurangan:**
- Tidak ada visualisasi workflow
- Komunitas lebih kecil (service-based approach)
- Vendor lock-in ke Flowable
- Perlu memahami Flowable services

---

## 3. Use Case yang Sesuai

| Use Case | Manual | Flowable | Deboot |
|----------|--------|----------|--------|
| **Aplikasi CRUD sederhana** | ✅ | ❌ | ❌ |
| **Workflow 2-3 langkah** | ✅ | ✅ | ✅ |
| **Workflow kompleks** | ❌ | ✅ | ✅ |
| **Enterprise scale** | ❌ | ✅ | ✅ |
| **Regulasi ketat** | ❌ | ✅ | ✅ |
| **MVP/POC cepat** | ❌ | ❌ | ✅ |
| **Tim kecil** | ✅ | ❌ | ✅ |
| **Budget terbatas** | ✅ | ✅ | ✅ |
| **Visualisasi penting** | ❌ | ✅ | ❌ |
| **Pembelajaran BPM** | ❌ | ✅ | ❌ |

---

## 4. Rekomendasi Akhir

### 4.1 Untuk Pembelajaran
- **Manual Version** - Memahami dasar workflow dan state machine
- **Flowable Version** - Memahami BPMN dan workflow engine

### 4.2 Untuk Proyek Production
- **Skala kecil**: Deboot Version atau Manual Version
- **Skala menengah**: Deboot Version atau Flowable CE
- **Skala besar/Enterprise**: Flowable Enterprise

### 4.3 Untuk Kondisi Khusus
- **Budget sangat terbatas**: Manual Version
- **Perlu compliance tinggi**: Flowable Enterprise
- **Perlu pengembangan cepat**: Deboot Version
- **Butuh visualisasi**: Flowable Version

---

## 5. Ringkasan Perbandingan Kuantitatif

| Metrik | Manual | Flowable | Deboot |
|--------|--------|----------|--------|
| **Backend LOC** | 1,731 | 1,563 | 1,482 |
| **Frontend LOC** | 1,364 | 1,364 | 1,364 |
| **Total LOC** | 3,095 | 3,023 | 2,846 |
| **File Count** | 26 | 23 | 22 |
| **Dependency** | Minimal | Medium | Medium |
| **Complexity** | Tinggi | Medium | Rendah |
| **Setup Time** | Cepat | Medium | Medium |

---

## 6. Catatan

Proyek ini dibuat untuk tujuan riset dan pembelajaran. Untuk implementasi production, diperlukan evaluasi lebih lanjut berdasarkan kebutuhan spesifik organisasi.

### Catatan Penting:
- Semua versi mengimplementasikan use case yang sama untuk perbandingan yang adil
- Frontend identik untuk semua versi
- Perbedaan utama ada di backend (workflow implementation)
- Deboot diimplementasikan sebagai service-based approach, bukan framework terpisah
