# Leave Management System — Workflow Engine Comparison

**Proyek Internal R&D | PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono — Backend Engineer Intern (Workflow Research)**

---

## 📋 Deskripsi Proyek

Repository ini merupakan proyek penelitian dan pengembangan internal yang dibuat selama magang di **PT. Len Industri (Persero)**, sebuah Badan Usaha Milik Negara (BUMN) yang bergerak di bidang teknologi dan industri pertahanan.

Proyek ini membandingkan **tiga pendekatan implementasi workflow** untuk sistem pengajuan cuti perusahaan:

1. **Manual Workflow** — Implementasi dengan state machine berbasis kode (if-else, enum)
2. **Flowable BPM** — Implementasi menggunakan Flowable BPMN Engine (Community Edition)
3. **Deboot Framework** — Implementasi menggunakan Deboot (akselerator di atas Flowable)

Tujuan utama adalah menganalisis kelebihan, kekurangan, kompleksitas, dan kesesuaian masing-masing pendekatan untuk konteks enterprise.

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

## 🎯 Tujuan Project

1. **Perbandingan Teknis** — Mengukur perbedaan kompleksitas kode (LOC), maintainability, dan skalabilitas antar pendekatan workflow
2. **Evaluasi BPM Engine** — Memahami kapan BPMN engine diperlukan vs manual workflow
3. **Rekomendasi Enterprise** — Memberikan insight untuk pemilihan solusi workflow di lingkungan enterprise
4. **Proof of Concept** — Mendemonstrasikan implementasi nyata dari setiap pendekatan

---

## 📁 Struktur Monorepo

```
leave-workflow-comparison/
│
├── manual-version/                    # Versi 1: Manual Workflow
│   ├── backend/                      # Spring Boot application
│   │   ├── src/main/java/.../
│   │   └── pom.xml
│   └── frontend/                     # React + TypeScript
│       ├── src/
│       └── package.json
│
├── flowable-version/                 # Versi 2: Flowable BPM
│   ├── backend/                      # Spring Boot + Flowable
│   │   ├── src/main/java/.../
│   │   └── pom.xml
│   ├── frontend/                     # React + TypeScript
│   │   ├── src/
│   │   └── package.json
│   └── bpmn/                         # BPMN definition files
│       └── leave-approval.bpmn20.xml
│
├── deboot-version/                   # Versi 3: Deboot Framework
│   ├── backend/                      # Spring Boot + Deboot
│   │   ├── src/main/java/.../
│   │   └── pom.xml
│   ├── frontend/                     # React + TypeScript
│   │   ├── src/
│   │   └── package.json
│   └── bpmn/                         # BPMN definition files
│       └── leave-approval.bpmn20.xml
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
└── README.md                         # Dokumen ini
```

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
| leaveType | Enum | Jenis cuti (ANNUAL, SICK, PERSONAL) |
| startDate | LocalDate | Tanggal mulai cuti |
| endDate | LocalDate | Tanggal akhir cuti |
| reason | String | Alasan pengajuan cuti |
| status | Enum | Status pengajuan |
| createdAt | Timestamp | Waktu pembuatan |
| updatedAt | Timestamp | Waktu terakhir update |

### Status Enum
- `PENDING` — Menunggu approval manager
- `MANAGER_APPROVED` — Approved manager, menunggu HR
- `MANAGER_REJECTED` — Ditolak manager
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
npm start
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
npm start
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
npm start
```

---

## 📊 Ringkasan Perbandingan

| Aspek | Manual | Flowable | Deboot |
|-------|--------|----------|--------|
| **Kompleksitas Kode** | Tinggi | Medium | Rendah |
| **LOC (Backend)** | ~1500 | ~1800 | ~1200 |
| **Fleksibilitas** | Penuh | Tinggi | Medium |
| **Visualisasi** | ❌ | ✅ | ✅ |
| **History Tracking** | Manual | Otomatis | Otomatis |
| **Learning Curve** | Rendah | Tinggi | Medium |
| **Setup Effort** | Minimal | Medium | Medium |

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

---

## 📸 Screenshot Placeholder

> _Screenshot aplikasi akan ditambahkan setelah deployment berhasil._

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

## 💡 Insight Akademik & Industri

### Kapan Manual Workflow?
- Proses bisnis sederhana dan stabil
- Tim kecil dengan kebutuhan terbatas
- Budget terbatas untuk lisensi

### Kapan Flowable BPM?
- Proses bisnis kompleks dengan banyak variasi
- Perlu visualisasi dan audit trail
- Kebutuhan compliance dan governance
- Skala enterprise

### Kapan Deboot Framework?
- Ingin manfaat BPM tanpa kompleksitas Flowable
- Tim dengan skill terbatas di BPMN
- Pengembangan cepat dengan pattern yang sudah disediakan

---

## ⚠️ Disclaimer

> Proyek ini dibuat sebagai **proyek riset internal** selama magang di PT. Len Industri (Persero).  
> Tujuan utama adalah pembelajaran dan penelitian, bukan untuk produksi.  
> Deboot Framework diasumsikan sebagai framework akselerator di atas Flowable untuk tujuan perbandingan.

---

## 📄 Lisensi

Proyek ini bersifat **internal** dan hanya untuk tujuan riset dan pembelajaran.  
Konten tidak untuk didistribusikan tanpa izin dari PT. Len Industri (Persero).

---

**Dibuat oleh Alifito Rabbani Cahyono**  
**PT. Len Industri (Persero) — Backend Engineer Intern**
