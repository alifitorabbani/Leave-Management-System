# Laporan Lines of Code (LOC)

**Proyek: Workflow Engine Comparison**  
**Perusahaan: PT. Len Industri (Persero)**  
**Pengembang: Alifito Rabbani Cahyono**  
**Konteks: Proyek magang — Riset internal workflow orchestration**

---

## 1. Pendahuluan

Dokumen ini melaporkan perhitungan Lines of Code (LOC) untuk setiap versi implementasi sistem pengajuan cuti. LOC dihitung untuk membandingkan kompleksitas kode antar pendekatan.

---

## 2. Metodologi Perhitungan

### 2.1 File yang Dihitung

**Backend (Java):**
- Semua file `.java` di bawah `src/main/java`
- Exclude: `target/`, `.class`

**Frontend (TypeScript/React):**
- Semua file `.ts` dan `.tsx` di bawah `src/`
- Exclude: `node_modules/`, `build/`

**BPMN (XML):**
- Semua file `.xml` di bawah folder `bpmn/`

### 2.2 Command yang Digunakan

```bash
# Backend LOC
find . -name "*.java" -path "*/src/*" | xargs wc -l | tail -1

# Frontend LOC
find . -name "*.ts" -o -name "*.tsx" | grep -v node_modules | xargs wc -l | tail -1

# BPMN LOC
find . -name "*.xml" -path "*/bpmn/*" | xargs wc -l | tail -1
```

---

## 3. Hasil Perhitungan LOC

### 3.1 Manual Version

| Komponen | File Count | LOC |
|----------|-----------|-----|
| **Backend (Java)** | | |
| Entity | 4 | ~300 |
| Repository | 2 | ~200 |
| Service | 1 | ~400 |
| Controller | 1 | ~250 |
| DTOs | 3 | ~250 |
| Exception | 4 | ~200 |
| AOP/Aspects | 1 | ~100 |
| Config | 1 | ~31 |
| DataInitializer | 1 | ~100 |
| **Backend Total** | **18** | **1,731** |
| **Frontend (TS/TSX)** | | |
| Pages | 4 | ~800 |
| Services | 1 | ~100 |
| App & Main | 2 | ~200 |
| CSS | 1 | ~264 |
| **Frontend Total** | **8** | **1,364** |
| **TOTAL** | **26** | **3,095** |

### 3.2 Flowable Version

| Komponen | File Count | LOC |
|----------|-----------|-----|
| **Backend (Java)** | | |
| Entity | 4 | ~280 |
| Repository | 2 | ~200 |
| Service | 1 | ~450 |
| Controller | 1 | ~200 |
| DTOs | 3 | ~200 |
| Config | 2 | ~133 |
| DataInitializer | 1 | ~100 |
| **Backend Total** | **14** | **1,563** |
| **Frontend (TS/TSX)** | | |
| Pages | 4 | ~800 |
| Services | 1 | ~100 |
| App & Main | 2 | ~200 |
| CSS | 1 | ~264 |
| **Frontend Total** | **8** | **1,364** |
| **BPMN Files** | | |
| leave-approval.bpmn20.xml | 1 | 96 |
| **TOTAL** | **23** | **3,023** |

### 3.3 Deboot Version

| Komponen | File Count | LOC |
|----------|-----------|-----|
| **Backend (Java)** | | |
| Entity | 4 | ~280 |
| Repository | 2 | ~220 |
| Service | 1 | ~350 |
| Controller | 1 | ~200 |
| DTOs | 3 | ~200 |
| Config | 2 | ~132 |
| DataInitializer | 1 | ~100 |
| **Backend Total** | **14** | **1,482** |
| **Frontend (TS/TSX)** | | |
| Pages | 4 | ~800 |
| Services | 1 | ~100 |
| App & Main | 2 | ~200 |
| CSS | 1 | ~264 |
| **Frontend Total** | **8** | **1,364** |
| **TOTAL** | **22** | **2,846** |

---

## 4. Tabel Perbandingan LOC

### 4.1 Ringkasan Total LOC

| Versi | Backend LOC | Frontend LOC | BPMN LOC | Total LOC |
|-------|-------------|--------------|----------|-----------|
| Manual | 1,731 | 1,364 | - | **3,095** |
| Flowable | 1,563 | 1,364 | 96 | **3,023** |
| Deboot | 1,482 | 1,364 | - | **2,846** |

### 4.2 Perbandingan Backend Saja

| Versi | Backend LOC | Persentase |
|-------|-------------|------------|
| Manual | 1,731 | 100% |
| Flowable | 1,563 | 90.3% |
| Deboot | 1,482 | 85.6% |

### 4.3 Persentase Reduction dari Manual

| Versi | LOC Reduction | Persentase |
|-------|----------------|------------|
| Manual | - | Baseline |
| Flowable | 168 LOC | 9.7% |
| Deboot | 249 LOC | 14.4% |

---

## 5. Analisis Perbedaan

### 5.1 Mengapa Manual Version Lebih Tinggi?

1. **State Machine Logic**: Implementasi manual memerlukan logika if-else yang eksplisit untuk setiap transisi status
2. **Audit Logging**: Implementasi manual memerlukan kode tambahan untuk audit trail manual
3. **Validasi**: Validasi state transition memerlukan pengecekan eksplisit
4. **Exception Handling**: Lebih banyak kode untuk menangani invalid state transitions
5. **AOP/Aspects**: Adanya LoggingAspect untuk logging

### 5.2 Mengapa Flowable Version Sedikit Lebih Rendah?

1. **Engine-based Logic**: Logika workflow di-handle oleh Flowable engine
2. **BPMN Configuration**: File BPMN XML memindahkan beberapa logika ke konfigurasi
3. **Service Integration**: Integrasi dengan Flowable services相对 lebih ringkas

### 5.3 Mengapa Deboot Version Paling Rendah?

1. **No BPMN XML**: Deboot tidak memerlukan file definisi proses BPMN
2. **Service-based**: Semua logika workflow ada di service class
3. **Direct Flowable Usage**: Menggunakan Flowable secara langsung tanpa abstraksi额外
4. **Minimal Boilerplate**: Tidak ada konfigurasi BPMN yang perlu di-maintain

---

## 6. Grafik Perbandingan

### 6.1 Total LOC Comparison

```
Total LOC Comparison (semua komponen)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Manual     ██████████████████████████████████████████████████████ 3,095
Flowable  █████████████████████████████████████████████████████ 3,023
Deboot    ███████████████████████████████████████████████████ 2,846
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
           0    600   1,200   1,800   2,400   3,000   3,600
```

### 6.2 Backend LOC Comparison

```
Backend LOC Comparison
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Manual     █████████████████████████████████████████████████████ 1,731
Flowable  ██████████████████████████████████████████████████ 1,563
Deboot    █████████████████████████████████████████████████ 1,482
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
           0    400    800   1,200   1,600   2,000
```

### 6.3 Perbandingan Komponen Backend

```
Komponen           Manual   Flowable   Deboot
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Entity               300       280       280
Repository           200       200       220
Service              400       450       350
Controller           250       200       200
DTOs                 250       200       200
Exception/Other      300       233       232
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
TOTAL             1,731     1,563     1,482
```

---

## 7. Kesimpulan

1. **Deboot Version** memiliki LOC terendah (2,846), menunjukkan bahwa pendekatan service-based efektif mengurangi kompleksitas kode

2. **Manual Version** memiliki LOC tertinggi (3,095) karena implementasi state machine secara eksplisit dengan if-else

3. **Flowable Version** berada di tengah (3,023), dengan tambahan 96 LOC untuk file BPMN XML

4. **Perbedaan Frontend**: Tidak ada perbedaan signifikan karena frontend memiliki struktur dan fitur yang sama untuk semua versi

5. **Rekomendasi**: 
   - Untuk proyek dengan kompleksitas rendah-sedang, Deboot dapat mempercepat development
   - Untuk proyek enterprise besar, Flowable memberikan fleksibilitas dan visualisasi lebih
   - Untuk pembelajaran dan understanding dasar, Manual version sangat baik
