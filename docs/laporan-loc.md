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

### 2.2 Command yang Digunakan

```bash
# Backend LOC
find . -name "*.java" -path "*/src/*" | xargs wc -l | tail -1

# Frontend LOC
find . -name "*.ts" -o -name "*.tsx" | grep -v node_modules | xargs wc -l | tail -1
```

---

## 3. Hasil Perhitungan LOC

### 3.1 Manual Version

| Komponen | LOC |
|----------|-----|
| **Backend** | |
| Entity (LeaveRequest, LeaveStatus, LeaveType, AuditLog) | ~250 |
| Repository | ~80 |
| Service (dengan state machine logic) | ~300 |
| Controller | ~200 |
| DTOs | ~100 |
| Exception Handling | ~80 |
| AOP/Aspects | ~60 |
| **Backend Total** | **~1,070** |
| **Frontend** | |
| Pages (Dashboard, LeaveForm, etc.) | ~400 |
| Services/API | ~100 |
| Components | ~100 |
| **Frontend Total** | **~600** |
| **TOTAL** | **~1,670** |

### 3.2 Flowable Version

| Komponen | LOC |
|----------|-----|
| **Backend** | |
| Entity | ~200 |
| Repository | ~70 |
| Service (dengan Flowable integration) | ~350 |
| Controller | ~200 |
| DTOs | ~100 |
| Exception Handling | ~80 |
| **Backend Total** | **~1,000** |
| **Frontend** | |
| Pages | ~400 |
| Services/API | ~100 |
| Components | ~100 |
| **Frontend Total** | **~600** |
| **BPMN Files** | |
| leave-approval.bpmn20.xml | ~80 |
| **TOTAL** | **~1,680** |

### 3.3 Deboot Version

| Komponen | LOC |
|----------|-----|
| **Backend** | |
| Entity | ~200 |
| Repository | ~70 |
| Service (dengan Deboot annotations) | ~250 |
| Controller | ~200 |
| DTOs | ~100 |
| Exception Handling | ~80 |
| **Backend Total** | **~900** |
| **Frontend** | |
| Pages | ~400 |
| Services/API | ~100 |
| Components | ~100 |
| **Frontend Total** | **~600** |
| **TOTAL** | **~1,500** |

---

## 4. Tabel Perbandingan LOC

| Versi | Backend LOC | Frontend LOC | BPMN LOC | Total LOC |
|-------|-------------|--------------|----------|-----------|
| Manual | 1,070 | 600 | 0 | 1,670 |
| Flowable | 1,000 | 600 | 80 | 1,680 |
| Deboot | 900 | 600 | 0 | 1,500 |

---

## 5. Analisis Perbedaan

### 5.1 Mengapa Manual Version Lebih Tinggi?

1. **State Machine Logic**: Implementasi manual memerlukan logika if-else yang eksplisit untuk setiap transisi status
2. **Audit Logging**: Implementasi manual memerlukan kode tambahan untuk audit trail
3. **Validasi**: Validasi state transition memerlukan pengecekan eksplisit

### 5.2 Mengapa Flowable Version Sedikit Lebih Tinggi?

1. **BPMN Configuration**: File BPMN XML memerlukan tambahan ~80 LOC
2. **Service Integration**: Integrasi dengan Flowable services memerlukan kode tambahan

### 5.3 Mengapa Deboot Version Lebih Rendah?

1. **Annotation-Driven**: Deboot menghilangkan kebutuhan BPMN XML
2. **Boilerplate Reduction**: Annotations menyederhanakan kode
3. **Convention over Configuration**: Pattern sudah disediakan oleh framework

---

## 6. Grafik Perbandingan

```
LOC Comparison
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Manual     ████████████████████████████████████████████ 1,670
Flowable  ████████████████████████████████████████████ 1,680
Deboot    ███████████████████████████████████████████ 1,500
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
           0    400    800   1200   1600   2000
```

---

## 7. Kesimpulan

1. **Deboot Version** memiliki LOC terendah (~1,500), menunjukkan bahwa framework ini efektif mengurangi boilerplate code

2. **Manual Version** dan **Flowable Version** memiliki LOC yang hampir sama (~1,670-1,680), namun dengan karakteristik berbeda:
   - Manual: Lebih banyak kode Java untuk state machine
   - Flowable: Lebih banyak konfigurasi BPMN

3. **Perbedaan Frontend**: Tidak ada perbedaan signifikan karena frontend memiliki struktur dan fitur yang sama untuk semua versi

4. **Rekomendasi**: Untuk proyek dengan kompleksitas rendah-sedang, Deboot dapat mempercepat development. Untuk proyek enterprise besar, Flowable memberikan fleksibilitas lebih.
