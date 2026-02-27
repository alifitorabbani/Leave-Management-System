# STUDI PERBANDINGAN IMPLEMENTASI WORKFLOW ENGINE

## Analisis Pendekatan Manual, Flowable BPM, dan Deboot (Service-Based)

**Proyek Internal R&D | PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono — Backend Engineer Intern (Workflow Research)**

---

## 📋 Deskripsi Proyek

Repository ini merupakan proyek penelitian dan pengembangan internal yang dibuat selama magang di **PT. Len Industri (Persero)**, sebuah Badan Usaha Milik Negara (BUMN) yang bergerak di bidang teknologi dan industri pertahanan.

Proyek ini membandingkan **tiga pendekatan implementasi workflow** untuk sistem pengajuan cuti perusahaan:

1. **Manual Workflow** — Implementasi dengan state machine berbasis kode (if-else, enum)
2. **Flowable BPM** — Implementasi menggunakan Flowable BPMN Engine (Community Edition)
3. **Deboot Framework** — Implementasi menggunakan Flowable dengan pendekatan yang disederhanakan (service-based tanpa BPMN XML)

Tujuan utama adalah menganalisis kelebihan, kekurangan, kompleksitas, dan kesesuaian masing-masing pendekatan untuk konteks enterprise.

> **📝 Catatan Penting**: Deboot dalam proyek ini diimplementasikan sebagai **pendekatan service-based** menggunakan Flowable secara langsung, bukan sebagai framework terpisah. Ini menunjukkan bagaimana Flowable dapat digunakan dengan cara yang lebih sederhana tanpa memerlukan pengetahuan BPMN XML.

---

## 🏢 Konteks Perusahaan

| Aspek | Detail |
|-------|--------|
| **Perusahaan** | PT. Len Industri (Persero) |
| **Divisi** | IT Governance & Development |
| **Penulis** | Alifito Rabbani Cahyono |
| **Peran** | Backend Engineer Intern — Workflow & BPM Research |
| **Tipe Proyek** | Proyek Riset Internal (R&D) |

---

## � Tujuan Project

1. **Perbandingan Teknis** — Mengukur perbedaan kompleksitas kode (LOC), maintainability, dan skalabilitas antar pendekatan workflow
2. **Evaluasi BPM Engine** — Memahami kapan BPMN engine diperlukan vs manual workflow
3. **Rekomendasi Enterprise** — Memberikan insight untuk pemilihan solusi workflow di lingkungan enterprise
4. **Proof of Concept** — Mendemonstrasikan implementasi nyata dari setiap pendekatan
5. **Pembelajaran Internal** — Menjadi bahan ajar untuk tim developer di PT. Len Industri

---

## 📁 Struktur Monorepo

```
leave-workflow-comparison/
│
├── manual-version/                    # Versi 1: Manual Workflow
│   ├── backend/                      # Spring Boot application
│   │   ├── src/main/java/.../
│   │   ├── pom.xml
│   │   └── src/main/resources/
│   │       └── application.properties
│   └── frontend/                     # React + TypeScript + Vite
│       ├── src/
│       │   ├── pages/
│       │   ├── services/
│       │   └── main.tsx
│       └── package.json
│
├── flowable-version/                 # Versi 2: Flowable BPM
│   ├── backend/                      # Spring Boot + Flowable
│   │   ├── src/main/java/.../
│   │   ├── pom.xml
│   │   └── src/main/resources/
│   │       └── application.properties
│   ├── frontend/                     # React + TypeScript + Vite
│   │   ├── src/
│   │   └── package.json
│   └── bpmn/                         # BPMN definition files
│       └── leave-approval.bpmn20.xml
│
├── deboot-version/                   # Versi 3: Deboot (Simplified Flowable)
│   ├── backend/                      # Spring Boot + Flowable
│   │   ├── src/main/java/.../
│   │   ├── pom.xml
│   │   └── src/main/resources/
│   │       └── application.properties
│   └── frontend/                     # React + TypeScript + Vite
│       ├── src/
│       │   ├── pages/
│       │   ├── services/
│       │   └── main.tsx
│       └── package.json
│
├── docs/                             # Dokumentasi (Bahasa Indonesia)
│   ├── arsitektur.md
│   ├── cara-kerja-workflow.md
│   ├── bukti-implementasi.md
│   ├── laporan-loc.md
│   ├── lisensi-dan-pricing.md
│   ├── analisis-enterprise.md
│   └── perbandingan.md
│
├── README.md                         # Dokumen ini
└── .gitignore
```

---

## 📊 Statistik LOC (Lines of Code)

| Versi | Backend (Java) | Frontend (TS/TSX) | BPMN/XML | Total |
|-------|---------------|-------------------|----------|-------|
| **Manual** | 1,731 LOC | 1,364 LOC | - | **3,095 LOC** |
| **Flowable** | 1,563 LOC | 1,364 LOC | 96 LOC | **3,023 LOC** |
| **Deboot** | 1,482 LOC | 1,364 LOC | - | **2,846 LOC** |

### Analisis LOC:
- **Deboot Version** memiliki LOC terendah karena menggunakan pendekatan service yang lebih ringkas
- **Manual Version** memiliki LOC tertinggi karena implementasi state machine secara eksplisit
- **Frontend** identik untuk semua versi karena UI tidak bergantung pada pendekatan workflow

---

## 🗄️ Konfigurasi Database

Setiap versi menggunakan **database PostgreSQL terpisah** untuk memastikan isolasi dan perbandingan yang adil:

| Versi | Database Name | Port Default |
|-------|---------------|--------------|
| Manual | `leave_manual_db` | 5432 |
| Flowable | `leave_flowable_db` | 5432 |
| Deboot | `leave_deboot_db` | 5432 |

### Setup PostgreSQL Lokal

```sql
-- Buat database untuk Manual Version
CREATE DATABASE leave_manual_db;

-- Buat database untuk Flowable Version
CREATE DATABASE leave_flowable_db;

-- Buat database untuk Deboot Version
CREATE DATABASE leave_deboot_db;

-- Verifikasi
\l
```

Pastikan PostgreSQL berjalan di `localhost:5432` dengan username `postgres` dan password sesuai konfigurasi lokal Anda.

---

## 🔌 Konfigurasi Port

### Backend Ports

| Versi | Port | Endpoint Base |
|-------|------|---------------|
| Manual | 8100 | `http://localhost:8100/api` |
| Flowable | 8200 | `http://localhost:8200/api` |
| Deboot | 8300 | `http://localhost:8300/api` |

### Frontend Ports

| Versi | Port | URL |
|-------|------|-----|
| Manual | 3100 | `http://localhost:3100` |
| Flowable | 3200 | `http://localhost:3200` |
| Deboot | 3300 | `http://localhost:3300` |

---

## ⚙️ Use Case: Sistem Pengajuan Cuti

Semua versi mengimplementasikan **use case identik** untuk memastikan perbandingan yang adil:

### Aktor
- **Employee** — Pegawai yang mengajukan cuti
- **Manager** — Atasan yang melakukan approval level 1
- **HR** — Human Resources yang melakukan approval final

### Alur Workflow
```
[Employee Submit] → [Manager Approval] → [HR Approval] → [Final Status]
                           │                   │
                           └─ REJECT ──────────┘
```

### Entity: LeaveRequest
| Field | Tipe | Deskripsi |
|-------|------|-----------|
| id | UUID | Primary key |
| employeeName | String | Nama pegawail |
| employeeId | String | ID Pegawai |
| department | String | Departemen |
| leaveType | Enum | Jenis cuti (ANNUAL, SICK, PERSONAL, MATERNITY) |
| startDate | LocalDate | Tanggal mulai cuti |
| endDate | LocalDate | Tanggal akhir cuti |
| reason | String | Alasan pengajuan cuti |
| status | Enum | Status pengajuan |
| managerComment | String | Komentar manager |
| hrComment | String | Komentar HR |
| approvedByManager | String | Nama manager yang approve |
| approvedByHR | String | Nama HR yang approve |
| managerApprovalDate | LocalDateTime | Waktu approval manager |
| hrApprovalDate | LocalDateTime | Waktu approval HR |
| totalDays | Integer | Total hari cuti |
| workflowInstanceId | String | ID proses workflow (Flowable) |
| createdAt | Timestamp | Waktu pembuatan |
| updatedAt | Timestamp | Waktu terakhir update |

### Status Enum
- `PENDING` — Menunggu approval manager
- `MANAGER_APPROVED` — Approved manager, menunggu HR
- `MANAGER_REJECTED` — Ditolak manager (final)
- `HR_APPROVED` — Approved HR (final)
- `HR_REJECTED` — Ditolak HR (final)

---

## 🚀 Cara Menjalankan

### Prasyarat

- **Java** 17+
- **Node.js** 18+
- **PostgreSQL** 14+ (lokal)
- **Maven** 3.8+

---

### Manual Version

**Backend:**
```bash
cd manual-version/backend
./mvnw spring-boot:run
# Atau: mvn spring-boot:run
```

**Frontend:**
```bash
cd manual-version/frontend
npm install
npm run dev
```

---

### Flowable Version

**Backend:**
```bash
cd flowable-version/backend
./mvnw spring-boot:run
# Atau: mvn spring-boot:run
```

**Frontend:**
```bash
cd flowable-version/frontend
npm install
npm run dev
```

---

### Deboot Version

**Backend:**
```bash
cd deboot-version/backend
./mvnw spring-boot:run
# Atau: mvn spring-boot:run
```

**Frontend:**
```bash
cd deboot-version/frontend
npm install
npm run dev
```

---

## 📊 Ringkasan Perbandingan

| Aspek | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| **Kompleksitas Kode** | Tinggi | Medium | Rendah |
| **LOC (Backend)** | 1,731 | 1,563 | 1,482 |
| **Fleksibilitas** | Penuh | Tinggi | Tinggi |
| **Visualisasi** | ❌ | ✅ | ❌ |
| **History Tracking** | Manual | Otomatis | Otomatis |
| **Learning Curve** | Rendah | Tinggi | Medium |
| **Setup Effort** | Minimal | Medium | Medium |
| **BPMN Support** | ❌ | ✅ | ✅ (via Flowable) |
| **Dependency** | Spring Boot only | Flowable 6.8.0 | Flowable 6.8.0 |

### Detail Fitur per Versi

#### Manual Version Features
- ✅ JWT Authentication & Authorization
- ✅ User Registration & Login
- ✅ Submit Leave Request
- ✅ Manager Approval/Rejection
- ✅ HR Approval/Rejection
- ✅ State Machine (Enum-based)
- ✅ Manual Audit Trail
- ✅ Role-based Access Control (Employee, Manager, HR)

#### Flowable Version Features
- ✅ JWT Authentication & Authorization
- ✅ User Registration & Login  
- ✅ Submit Leave Request (with BPMN process)
- ✅ Manager Approval/Rejection (via Flowable Task)
- ✅ HR Approval/Rejection (via Flowable Task)
- ✅ BPMN 2.0 Process Definition
- ✅ Automatic History Tracking
- ✅ Process Variables
- ✅ Candidate Groups (managers, hr)

#### Deboot Version Features
- ✅ JWT Authentication & Authorization
- ✅ User Registration & Login
- ✅ Submit Leave Request (direct Flowable)
- ✅ Manager Approval/Rejection (via TaskService)
- ✅ HR Approval/Rejection (via TaskService)
- ✅ Service-based Workflow (no BPMN XML)
- ✅ Automatic History (via Flowable)
- ✅ Direct Flowable API Usage

---

## 📚 Dokumentasi

Semua dokumentasi tersedia di folder [`docs/`](docs/):

| File | Deskripsi |
|------|-----------|
| [`arsitektur.md`](docs/arsitektur.md) | Penjelasan arsitektur setiap versi |
| [`cara-kerja-workflow.md`](docs/cara-kerja-workflow.md) | Cara kerja workflow di setiap versi |
| [`bukti-implementasi.md`](docs/bukti-implementasi.md) | Bukti kode implementasi |
| [`laporan-loc.md`](docs/laporan-loc.md) | Laporan Lines of Code |
| [`lisensi-dan-pricing.md`](docs/lisensi-dan-pricing.md) | Analisis lisensi dan harga |
| [`analisis-enterprise.md`](docs/analisis-enterprise.md) | Analisis aspek enterprise |
| [`perbandingan.md`](docs/perbandingan.md) | Matrix perbandingan lengkap |
| [`audit-komprehensif.md`](docs/audit-komprehensif.md) | **BARU** - Audit lengkap semua versi |

---

## 🔍 Ringkasan

### Manual Version
- **Kompleksitas**: Tinggi
- **LOC Backend**: 1,731
- **Kelebihan**: Tidak ada dependency eksternal, kontrol penuh, mudah dipahami
- **Kekurangan**: Tidak ada visualisasi, audit trail manual, scaling terbatas
- **Cocok untuk**: Pembelajaran, proyek sederhana, budget terbatas

### Flowable Version  
- **Kompleksitas**: Medium
- **LOC Backend**: 1,563
- **Kelebihan**: Visual workflow, audit trail otomatis, BPMN standar industri
- **Kekurangan**: Kurva pembelajaran tinggi, overhead untuk proyek kecil
- **Cocok untuk**: Enterprise, compliance ketat, visualisasi penting

### Deboot Version
- **Kompleksitas**: Rendah
- **LOC Backend**: 1,482
- **Kelebihan**: Tidak perlu belajar BPMN, boilerplate sedikit, pengembangan cepat
- **Kekurangan**: Tidak ada visualisasi, vendor lock-in ke Flowable
- **Cocok untuk**: MVP/POC, tim kecil, pengembangan cepat

---

## 🔍 Cara Menghitung LOC

```bash
# Backend LOC (exclude target/)
find . -name "*.java" -path "*/src/*" | xargs wc -l | tail -1

# Frontend LOC (exclude node_modules/)
find . -name "*.ts" -o -name "*.tsx" | grep -v node_modules | xargs wc -l | tail -1

# Total
echo "Backend: $(find . -name '*.java' -path '*/src/*' | xargs wc -l | tail -1)"
echo "Frontend: $(find . \( -name '*.ts' -o -name '*.tsx' \) | grep -v node_modules | xargs wc -l | tail -1)"
```

---

## 🛠️ Tech Stack

### Backend

| Komponen | Manual Version | Flowable Version | Deboot Version |
|----------|----------------|------------------|----------------|
| Framework | Spring Boot 3.2.0 | Spring Boot 3.2.0 | Spring Boot 3.2.0 |
| BPM Engine | ❌ | Flowable 6.8.0 | Flowable 6.8.0 |
| Database | PostgreSQL | PostgreSQL | PostgreSQL |
| ORM | Spring Data JPA | Spring Data JPA | Spring Data JPA |
| Security | Spring Security + JWT | Spring Security + JWT | Spring Security + JWT |
| Build Tool | Maven | Maven | Maven |
| Java Version | 17+ | 17+ | 17+ |

### Frontend (Identik untuk semua versi)

| Komponen | Semua Versi |
|----------|-------------|
| Framework | React 18 |
| Language | TypeScript |
| Build Tool | Vite |
| Styling | Tailwind CSS |
| HTTP Client | Axios |
| Routing | React Router |
| Date Handling | date-fns |
| Notifications | react-hot-toast |

---

## 🔑 Default Credentials

Setelah menjalankan aplikasi, berikut akun yang dapat digunakan untuk testing:

| Role | Username | Password | Port |
|------|----------|----------|------|
| Employee | employee1 | password123 | 8100/8200/8300 |
| Employee | employee2 | password123 | 8100/8200/8300 |
| Employee | employee3 | password123 | 8100/8200/8300 |
| Manager | manager1 | password123 | 8100/8200/8300 |
| Manager | manager2 | password123 | 8100/8200/8300 |
| HR | hr1 | password123 | 8100/8200/8300 |
| HR | hr2 | password123 | 8100/8200/8300 |

---

## 💡 Rekomendasi Penggunaan

### Berdasarkan Jenis Organisasi

| Jenis Organisasi | Rekomendasi | Alasan |
|------------------|-------------|--------|
| Startup (< 10 orang) | **Manual** | Simpel, biaya rendah, tidak perlu expertise khusus |
| SMB (10-100 orang) | **Deboot** | Seimbang antara fitur dan kesederhanaan |
| Enterprise (> 100 orang) | **Flowable** | Skalabilitas, compliance, visualisasi |

### Berdasarkan Kebutuhan

| Kebutuhan | Rekomendasi |
|-----------|-------------|
| **Pembelajaran workflow** | Manual (untuk memahami dasar) |
| **Pembelajaran BPMN** | Flowable (untuk memahami BPMN) |
| **MVP/POC cepat** | Deboot |
| **Visualisasi penting** | Flowable |
| **Budget sangat terbatas** | Manual |
| **Compliance ketat** | Flowable Enterprise |
| **Tim kecil** | Manual atau Deboot |

---

## 🎓 Analogi & Perumpamaan

Untuk memahami perbedaan ketiga pendekatan ini, berikut analogi yang dapat membantu:

### 1. Perumpamaan: Membandingkan dengan Kehidupan Sehari-hari

| Pendekatan | Analogi | Penjelasan |
|------------|---------|------------|
| **Manual** | **Resep Masakan Tradisional** | Semua resep ditulis tangan,chef harus menghafal langkah-langkah, jika ingin mengubahmenu harus menulis ulang resep |
| **Flowable** | **Restoran dengan Sistem Profesional** | Proses masak terdokumentasi dalam SOP lengkap, ada visualisasi kitchen, setiap staf tahu apa yang harus dilakukan |
| **Deboot** | **Kitchen Modern dengan Chef AI** | Menggunakan peralatan modern (Flowable) tetapi dengan panduan yang lebih sederhana - cukup tekan tombol, makanan jadi |

### 2. Perumpamaan: Dalam Konteks Transportasi

| Pendekatan | Analogi | Penjelasan |
|------------|---------|------------|
| **Manual** | **Sepeda Manual** | Sederhana, murah, mudah diperbaiki, tapi terbatas kecepatan dan jarak |
| **Flowable** | **Pesawat Komersial** | Sistem kompleks dengan banyak fitur, perlu pelatihan khusus untuk mengoperasikannya, sangat powerful |
| **Deboot** | **Mobil Listrik** | Menggunakan teknologi modern (seperti Flowable) tetapi dengan pengoperasian yang lebih sederhana |

### 3. Perumpamaan: Dalam Konteks Menulis

| Pendekatan | Analogi | Penjelasan |
|------------|---------|------------|
| **Manual** | **Menulis dengan Pensil** | Langsung, sederhana, kontrol penuh, tapi perlu effort lebih untuk menghapus/mengubah |
| **Flowable** | **Menulis dengan Word Processor + Flowchart** | Dapat membuat diagram alur, undo/redo mudah, tapi perlu belajar fiturnya |
| **Deboot** | **Menulis dengan AI Assistant** | Banyak bantuan otomatis, lebih cepat, tapi perlu sedikit penyesuaian |

### 4. Kapan Menggunakan Analogi Ini?

- **Manual**: Seperti memilih **sepeda** untuk pergi ke toko dekat - sederhana dan cukup untuk kebutuhan dasar
- **Flowable**: Seperti memilih **pesawat** untuk perjalanan bisnis internasional - overkill untuk jarak dekat, tapi necessary untuk jarak jauh
- **Deboot**: Seperti memilih **mobil** untuk sehari-hari - compromise antara kemudahan dan kemampuan

### 5. Ringkasan Perumpamaan Visual

```
                    Kompleksitas
                         ▲
                         │
        Flowable        │    Pesawat Komersial
        (BPMN)         │    (Fitur lengkap,
         ─────────      │    tapi kompleks)
                         │
                         │
        Deboot  ────────┼────── Mobil Listrik
        (Service)      │    (Modern, mudah,
                         │    cukup powerful)
                         │
                         │
        Manual  ────────┴────── Sepeda
                         │    (Sederhana,
                         │    murah, terbatas)
                         │
                         └────────────────────────▶
                                    Biaya
```

**Garis bawahi**: Tidak ada yang lebih baik atau lebih buruk - semuanya tergantung konteks dan kebutuhan.

---

## 🔄 Code-based vs Web Modeler: Perbedaan Pendekatan

### Apa itu Flowable Web Modeler?

Flowable menyediakan **Flowable Modeler** (web-based) yang merupakan aplikasi web untuk membuat dan mengelola workflow secara visual. Ini adalah pendekatan GUI yang berbeda dari yang kita implementasikan di proyek ini.

### Perbandingan Pendekatan

| Aspek | Code-based (Proyek Ini) | Web Modeler |
|-------|-------------------------|-------------|
| **Pendekatan** | Tulis kode Java/XML | Drag & drop di browser |
| **Visualisasi** | File BPMN XML | GUI visual flowchart |
| **Team Collaboration** | Via version control (Git) | Real-time collaboration |
| **Deployment** | Build & restart aplikasi | Klik "Deploy" di web |
| **Versioning** | Git-based | Built-in versioning |
| **Learning Curve** | Tinggi (harus coding) | Rendah (GUI) |

### Perbandingan Detail: Flowable (BPMN) vs Deboot vs Web Modeler

| Fitur | Flowable (BPMN XML) | Deboot (Code) | Web Modeler |
|-------|---------------------|--------------|-------------|
| **Definisi Workflow** | File .bpmn20.xml | Kode Java Service | GUI Visual |
| **Penggunaan** | | | |
| Desain Proses | Manual edit XML | - | Drag & Drop |
| Deploy | Via RepositoryService | Auto-start | Klik button |
| Ubah Proses | Edit file + rebuild | Ubah kode + rebuild | Edit di web |
| **Kelebihan** | | | |
| Version Control | ✅ (text file) | ✅ | ❌ (database) |
| Code Review | ✅ | ✅ | ❌ |
| CI/CD Friendly | ✅ | ✅ | ❌ |
| Easy Debugging | Medium | Easy | Sulit |
| **Kekurangan** | | | |
| Learning Curve | Tinggi | Medium | Rendah |
| Visual Feedback | Perlu tool eksternal | ❌ | ✅ |
| Kolaborasi Tim | Via Git | Via Git | Real-time |

### Kapan Menggunakan Pendekatan Code-based?

| Situasi | Rekomendasi |
|---------|-------------|
| Proses sederhana | Code-based (lebih cepat) |
| Tim developer | Code-based |
| CI/CD pipeline | Code-based |
| Proses kompleks | Web Modeler |
| Non-technical user | Web Modeler |
| Perubahan frequent | Web Modeler |

### Kapan Menggunakan Web Modeler?

**Gunakan Web Modeler jika:**
1. Tim mencakup business analyst non-technical
2. Proses sering berubah-ubah
3. Butuh visualisasi real-time
4. Organisasi sudah memiliki infrastruktur Flowable
5. Collaboration antar department penting

**Gunakan Code-based jika:**
1. Tim small & technical
2. Proses sudah stabil
3. Butuh CI/CD automation
4. Version control penting
5. Budget/ressource terbatas

### Diagram Perbandingan Pendekatan

```
┌─────────────────────────────────────────────────────────────────────┐
│                    FLOWABLE IMPLEMENTATION OPTIONS                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│    ┌─────────────────────────────────────────────────────────┐      │
│    │                  WEB MODELER (GUI)                      │      │
│    │  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐ │      │
│    │  │ Design │  │ Deploy │  │ Monitor │  │  Edit   │ │      │
│    │  │ (Drag) │──│(Click) │──│(Web UI) │──│(Visual) │ │      │
│    │  └─────────┘  └─────────┘  └─────────┘  └─────────┘ │      │
│    └─────────────────────────────────────────────────────────┘      │
│                              │                                      │
│                              ▼                                      │
│    ┌─────────────────────────────────────────────────────────┐      │
│    │                 CODE-BASED                             │      │
│    │                                                          │      │
│    │  ┌──────────────────┐    ┌────────────────────────┐  │      │
│    │  │ FLOWABLE (BPMN)  │    │    DEBOOT (Service)    │  │      │
│    │  │                  │    │                        │  │      │
│    │  │ • .bpmn20.xml   │    │ • Java Service         │  │      │
│    │  │ • XML Editor    │    │ • RuntimeService      │  │      │
│    │  │ • BPMN Designer │    │ • TaskService          │  │      │
│    │  └────────┬─────────┘    └───────────┬────────────┘  │      │
│    │           │                           │                │      │
│    └───────────▼──────────────────────────▼────────────────┘      │
│                         │                                           │
│                         ▼                                           │
│              ┌─────────────────────┐                               │
│              │   MAVEN BUILD       │                               │
│              │   (mvn clean build) │                               │
│              └─────────────────────┘                               │
└─────────────────────────────────────────────────────────────────────┘
```

### Kesimpulan

Di proyek ini, kita menggunakan **code-based approach** karena:

1. **Tujuan Riset**: Memahami cara kerja Flowable secara internal
2. **Pembelajaran**: Lebih baik untuk memahami mekanisme Flowable
3. **Kontrol Penuh**: Dapat customisasi dengan tepat
4. **Portabilitas**: mudah di-versioning dan di-CI/CD-kan

Namun untuk production environment dengan tim yang mencakup business analyst, **Flowable Web Modeler** mungkin lebih tepat digunakan karena:

1. Kemudahan kolaborasi
2. Tidak perlu skill coding untuk perubahan minor
3. Visualisasi langsung
4. Deployment lebih cepat

---

## 📝 Kesimpulan dari Setiap Dokumentasi

Berikut ringkasan kesimpulan dari setiap dokumen dalam folder `docs/`:

### 1. Kesimpulan dari [`docs/arsitektur.md`](docs/arsitektur.md)

| Versi | Kapan Menggunakan |
|-------|------------------|
| **Manual** | Proses bisnis sederhana dan stabil, tim kecil tanpa keahlian BPMN, tidak memerlukan visualisasi workflow, budget terbatas |
| **Flowable** | Proses bisnis kompleks, memerlukan audit trail lengkap, kebutuhan compliance dan governance, memerlukan visualisasi workflow, skala enterprise |
| **Deboot** | Ingin manfaat BPM tanpa kompleksitas XML, tim dengan keahlian Java lebih baik, pengembangan cepat, perlu skalabilitas Flowable tanpa belajar BPMN |

### 2. Kesimpulan dari [`docs/laporan-loc.md`](docs/laporan-loc.md)

| Temuan |
|--------|
| **Deboot Version** memiliki LOC terendah (2,846), menunjukkan bahwa pendekatan service-based efektif mengurangi kompleksitas kode |
| **Manual Version** memiliki LOC tertinggi (3,095) karena implementasi state machine secara eksplisit dengan if-else |
| **Flowable Version** berada di tengah (3,023), dengan tambahan 96 LOC untuk file BPMN XML |
| Tidak ada perbedaan signifikan di frontend karena struktur dan fitur yang sama untuk semua versi |

### 3. Kesimpulan dari [`docs/analisis-enterprise.md`](docs/analisis-enterprise.md)

| Skor |
|------|
| **Flowable** mendapatkan skor tertinggi (4.1) karena scalability, compliance, dan collaboration |
| **Deboot** mendapatkan skor tinggi (3.9) dengan keunggulan pada kompleksitas rendah dan learning curve yang mudah |
| **Manual** cocok untuk proyek sederhana dengan budget terbatas |

**Faktor penentu:** Kebutuhan spesifik organisasi, budget, kompleksitas proses bisnis, kebutuhan compliance, kapabilitas tim

### 4. Kesimpulan dari [`docs/cara-kerja-workflow.md`](docs/cara-kerja-workflow.md)

| Versi | Karakteristik |
|-------|---------------|
| **Manual** | Simple tapi terbatas - semua logika di kode Java, state machine dengan if-else, audit trail manual |
| **Flowable** | Fleksibel dan powerful - workflow dalam BPMN XML, visualisasi proses, audit trail otomatis |
| **Deboot** | Penyederhanaan Flowable - tidak perlu belajar BPMN XML, semua logika di service code, audit trail otomatis dari Flowable |

### 5. Kesimpulan dari [`docs/bukti-implementasi.md`](docs/bukti-implementasi.md)

| Temuan |
|--------|
| **Manual Version** memerlukan logika if-else eksplisit untuk setiap transisi status, kompleksitas kode tertinggi (1,731 LOC) |
| **Flowable Version** mendelegasikan manajemen workflow ke engine BPMN dengan BPMN XML, memberikan visualisasi dan audit trail otomatis |
| **Deboot Version** menggunakan Flowable langsung melalui service code tanpa BPMN XML, memberikan keseimbangan antara simplicity dan fungsionalitas |
| Setiap pendekatan memiliki trade-off antara kompleksitas, fleksibilitas, dan kemudahan maintenance |

### 6. Kesimpulan dari [`docs/audit-komprehensif.md`](docs/audit-komprehensif.md)

| Cocok Untuk |
|-------------|
| **Manual**: Proyek pembelajaran dasar workflow, aplikasi dengan workflow sangat sederhana, tim dengan keterbatasan resources |
| **Flowable**: Enterprise dengan kebutuhan kompleks, kebutuhan compliance dan audit ketat, organisasi familiar dengan BPMN |
| **Deboot**: Tim yang ingin manfaat Flowable tanpa kompleksitas BPMN, pengembangan MVP/POC cepat, proyek dengan timeline ketat |

### Catatan Penting:

- Semua tiga versi mengimplementasikan use case yang **identik** untuk perbandingan yang adil
- Frontend **identik** untuk semua versi karena UI tidak bergantung pada pendekatan backend
- Perbedaan utama terletak di **backend (workflow implementation)**
- Deboot adalah **pendekatan service-based**, bukan framework terpisah

---

## ✅ Kesimpulan Akhir

### Kapan Manual Workflow?
- Proses bisnis sederhana dan stabil
- Tim kecil dengan kebutuhan terbatas
- Budget terbatas untuk lisensi
- Tidak memerlukan visualisasi workflow
- Ingin kontrol penuh atas logika bisnis

### Kapan Flowable BPM?
- Proses bisnis kompleks dengan banyak variasi
- Perlu visualisasi dan audit trail
- Kebutuhan compliance dan governance
- Skala enterprise
- Tim yang memiliki keahlian BPMN
- Memerlukan process mining capabilities

### Kapan Deboot Framework?
- Ingin manfaat BPM tanpa kompleksitas Flowable penuh
- Tim dengan skill terbatas di BPMN
- Pengembangan cepat dengan pattern yang sudah disediakan
- Perlu skalabilitas Flowable tanpa belajar BPMN XML
- Ingin service-based approach yang lebih familiar bagi developer

---

## ⚠️ Disclaimer

> Proyek ini dibuat sebagai **proyek riset internal** selama magang di PT. Len Industri (Persero).  
> Tujuan utama adalah pembelajaran dan penelitian, bukan untuk produksi.  
> Deboot Framework dalam konteks ini diimplementasikan sebagai pendekatan service-based di atas Flowable (bukan framework eksternal), untuk menunjukkan bagaimana Flowable dapat digunakan dengan cara yang lebih sederhana.

---

## 📄 Lisensi

Proyek ini bersifat **internal** dan hanya untuk tujuan riset dan pembelajaran.  

---

**Dibuat oleh Alifito Rabbani Cahyono**  
**PT. Len Industri (Persero) — Backend Engineer Intern**  
