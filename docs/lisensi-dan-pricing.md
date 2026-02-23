# Lisensi dan Pricing

**Proyek: Workflow Engine Comparison**  
**Perusahaan: PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono**  
**Konteks: Proyek magang — Riset internal workflow orchestration**

---

## 1. Manual Version

### 1.1 Lisensi

- **Tipe**: Proprietary (Internal)
- **Biaya**: Tidak ada (gratis untuk penggunaan internal)
- **Modifikasi**: Penuh (kode sepenuhnya milik perusahaan)

### 1.2 Pertimbangan

| Aspek | Keterangan |
|-------|------------|
| Biaya Awal | Rendah (hanya development cost) |
| Biaya Maintenance | Tinggi (perlu developer internal) |
| Support | Tidak ada (internal only) |
| Compliance | Manual - perlu implementasi sendiri |

---

## 2. Flowable BPM

### 2.1 Lisensi Flowable

Flowable tersedia dalam dua edisi:

#### Community Edition (Open Source)
- **Lisensi**: Apache License 2.0
- **Biaya**: Gratis
- **Fitur**: 
  - Core BPMN engine
  - DMN, Form, CMMN
  - REST API
  - Spring Boot integration

#### Enterprise Edition (Commercial)
- **Lisensi**: Commercial License
- **Biaya**: Mulai dari ~$5,000 - $50,000/tahun (estimasi)
- **Fitur Tambahan**:
  - Enterprise support
  - Advanced analytics
  - Clustering & high availability
  - Modeler visual
  - Audit & compliance tools

### 2.2 Estimasi Harga dalam IDR (2024)

| Komponen | Estimasi Harga (IDR) |
|----------|---------------------|
| Community Edition | Gratis |
| Enterprise Basic | ~Rp 85.000.000/tahun |
| Enterprise Professional | ~Rp 170.000.000/tahun |
| Enterprise Premium | ~Rp 425.000.000/tahun |

### 2.3 Pertimbangan

| Aspek | Keterangan |
|-------|------------|
| Biaya Awal | Rendah (Community Edition gratis) |
| Biaya Maintenance | Medium (bisa menggunakan Community) |
| Support | Community (gratis) / Enterprise (berbayar) |
| Compliance | Built-in audit trail |

---

## 3. Deboot Framework

### 3.1 Asumsi Lisensi

Deboot dalam proyek ini diasumsikan sebagai framework akselerator di atas Flowable. Model lisensi yang diasumsikan:

#### Asumsi Pricing Model
- **Tipe**: Commercial Framework
- **Biaya**: ~$2,000 - $10,000/tahun (estimasi)
- **Model**: Subscription-based

### 3.2 Estimasi Harga dalam IDR (2024)

| Komponen | Estimasi Harga (IDR) |
|----------|---------------------|
| Deboot License | ~Rp 34.000.000 - 170.000.000/tahun |
| Flowable Community | Gratis |
| **Total** | **~Rp 34.000.000 - 170.000.000/tahun** |

### 3.3 Pertimbangan

| Aspek | Keterangan |
|-------|------------|
| Biaya Awal | Medium (lisensi framework) |
| Biaya Maintenance | Rendah (boilerplate lebih sedikit) |
| Support | Vendor-provided (asumsi) |
| Compliance | Built-in (menggunakan Flowable) |

---

## 4. Perbandingan Total Cost of Ownership (TCO)

### 4.1 Estimasi Biaya 3 Tahun

| Versi | Tahun 1 | Tahun 2 | Tahun 3 | Total |
|-------|---------|---------|---------|-------|
| Manual | Rp 50.000.000 | Rp 25.000.000 | Rp 25.000.000 | Rp 100.000.000 |
| Flowable CE | Rp 50.000.000 | Rp 15.000.000 | Rp 15.000.000 | Rp 80.000.000 |
| Flowable Ent | Rp 250.000.000 | Rp 200.000.000 | Rp 200.000.000 | Rp 650.000.000 |
| Deboot | Rp 200.000.000 | Rp 100.000.000 | Rp 100.000.000 | Rp 400.000.000 |

*Catatan: Estimasi termasuk biaya development, maintenance, dan infrastruktur*

---

## 5. Rekomendasi

### 5.1 Berdasarkan Budget

| Budget | Rekomendasi |
|--------|-------------|
| < Rp 50jt/tahun | Manual atau Flowable CE |
| Rp 50-150jt/tahun | Deboot |
| > Rp 150jt/tahun | Flowable Enterprise |

### 5.2 Berdasarkan Kebutuhan

| Kebutuhan | Rekomendasi |
|-----------|-------------|
| Proses sederhana | Manual |
| Enterprise-grade | Flowable Enterprise |
| Pengembangan cepat | Deboot |
| Open source wajib | Flowable CE |

---

## 6. Disclaimer

- Estimasi harga di atas adalah projections berdasarkan informasi publik dan dapat berbeda
- Untuk harga resmi, silakan hubungi vendor respective
- Lisensi dan pricing dapat berubah sewaktu-waktu
