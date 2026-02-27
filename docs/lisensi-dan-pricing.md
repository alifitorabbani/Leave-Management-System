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
- **Dependency**: Spring Boot (Open Source, Apache License 2.0)

### 1.2 Pertimbangan

| Aspek | Keterangan |
|-------|------------|
| Biaya Awal | Rendah (hanya development cost) |
| Biaya Maintenance | Tinggi (perlu developer internal) |
| Support | Tidak ada (internal only) |
| Compliance | Manual - perlu implementasi sendiri |
| Vendor Lock-in | Tidak ada |

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
  - Basic history & audit

#### Enterprise Edition (Commercial)

- **Lisensi**: Commercial License
- **Biaya**: Mulai dari ~$5,000 - $50,000/tahun (estimasi)
- **Fitur Tambahan**:
  - Enterprise support
  - Advanced analytics
  - Clustering & high availability
  - Modeler visual
  - Audit & compliance tools
  - Process mining

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
| Vendor Lock-in | Medium - proprietary extensions |

---

## 3. Deboot Framework

### 3.1 Catatan Penting

Deboot dalam proyek ini **diimplementasikan sebagai pendekatan service-based menggunakan Flowable secara langsung**, bukan sebagai framework terpisah. Dengan kata lain, ini adalah cara penggunaan Flowable tanpa BPMN XML.

### 3.2 Model Lisensi (Jika Menggunakan Framework Eksternal)

Jika Deboot diasumsikan sebagai framework terpisah:

| Model | Estimasi |
|-------|----------|
| **Tipe** | Commercial Framework |
| **Biaya** | ~$2,000 - $10,000/tahun (estimasi) |
| **Model** | Subscription-based |

### 3.3 Estimasi Harga dalam IDR (2024)

| Komponen | Estimasi Harga (IDR) |
|----------|---------------------|
| Deboot License (asumsi) | ~Rp 34.000.000 - 170.000.000/tahun |
| Flowable Community | Gratis |
| **Total** | **~Rp 34.000.000 - 170.000.000/tahun** |

### 3.4 Pertimbangan

| Aspek | Keterangan |
|-------|------------|
| Biaya Awal | Medium (jika menggunakan framework berbayar) |
| Biaya Maintenance | Rendah (boilerplate lebih sedikit) |
| Support | Vendor-provided (jika ada) |
| Compliance | Built-in (menggunakan Flowable) |
| Vendor Lock-in | Tinggi |

---

## 4. Perbandingan Total Cost of Ownership (TCO)

### 4.1 Estimasi Biaya 3 Tahun

| Versi | Tahun 1 | Tahun 2 | Tahun 3 | Total |
|-------|---------|---------|---------|-------|
| Manual | Rp 50.000.000 | Rp 25.000.000 | Rp 25.000.000 | Rp 100.000.000 |
| Flowable CE | Rp 50.000.000 | Rp 15.000.000 | Rp 15.000.000 | Rp 80.000.000 |
| Flowable Ent | Rp 250.000.000 | Rp 200.000.000 | Rp 200.000.000 | Rp 650.000.000 |
| Deboot* | Rp 200.000.000 | Rp 100.000.000 | Rp 100.000.000 | Rp 400.000.000 |

*Catatan: Estimasi Deboot mencakup lisensi framework (asumsi) + development cost

*Catatan: Estimasi termasuk biaya development, maintenance, dan infrastruktur*

### 4.2 Asumsi Perhitungan

| Komponen | Manual | Flowable CE | Flowable Ent | Deboot |
|----------|--------|-------------|--------------|--------|
| Development Cost | Rp 30jt | Rp 30jt | Rp 30jt | Rp 30jt |
| Infrastructure | Rp 10jt/thn | Rp 10jt/thn | Rp 10jt/thn | Rp 10jt/thn |
| Maintenance | Rp 10jt/thn | Rp 5jt/thn | Rp 5jt/thn | Rp 5jt/thn |
| License Fee | - | - | Rp 180jt/thn | Rp 120jt/thn |

---

## 5. Rekomendasi

### 5.1 Berdasarkan Budget

| Budget | Rekomendasi |
|--------|-------------|
| < Rp 50jt/tahun | Manual atau Flowable CE |
| Rp 50-150jt/tahun | Deboot (jika menggunakan framework) |
| > Rp 150jt/tahun | Flowable Enterprise |

### 5.2 Berdasarkan Kebutuhan

| Kebutuhan | Rekomendasi |
|-----------|-------------|
| Proses sederhana | Manual |
| Enterprise-grade | Flowable Enterprise |
| Pengembangan cepat | Deboot (service-based) |
| Open source wajib | Flowable CE |
| Visualisasi workflow | Flowable |
| Audit trail otomatis | Flowable/Deboot |

### 5.3 Berdasarkan Ukuran Organisasi

| Ukuran Organisasi | Rekomendasi |
|-------------------|-------------|
| Startup (< 10 org) | Manual |
| SMB (10-100 org) | Deboot atau Flowable CE |
| Enterprise (> 100 org) | Flowable Enterprise |

---

## 6. Perbandingan Fitur vs Biaya

| Fitur | Manual | Flowable CE | Deboot* |
|-------|--------|-------------|---------|
| **Biaya** | Rendah | Gratis | Medium |
| Workflow Engine | ❌ | ✅ | ✅ |
| BPMN Support | ❌ | ✅ | ✅ |
| Visual Designer | ❌ | ❌ | ❌ |
| Audit Trail | Manual | Auto | Auto |
| Clustering | ❌ | ❌ | ❌ |
| Enterprise Support | ❌ | ❌ | ❌ |
| Process Mining | ❌ | ❌ | ❌ |

*Deboot dalam konteks ini adalah implementasi service-based, bukan framework terpisah

---

## 7. Disclaimer

- Estimasi harga di atas adalah projections berdasarkan informasi publik dan dapat berbeda
- Untuk harga resmi, silakan hubungi vendor respective
- Lisensi dan pricing dapat berubah sewaktu-waktu
- Deboot diimplementasikan sebagai pendekatan service-based di proyek ini
