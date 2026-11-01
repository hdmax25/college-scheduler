# College Scheduler

Aplikasi Android native manajemen jadwal perkuliahan, tugas, ujian, dan to-do list akademik mahasiswa. Dirancang offline-first dengan antarmuka Material 3 berbahasa Indonesia.

---

## Status Pengembangan

- **Tahap Front End (FE-01 s/d FE-12):** **SELESAI (Frontend-Done)** ✅
- **Tahap Back End (Persistensi Room / SQLite):** **SIAP DIKERJAKAN** ⏳

> **Catatan:** Pada tahap Front End, penyimpanan data menggunakan `InMemoryRepository` dengan data contoh. Data akan direset setiap kali aplikasi dimulai ulang sampai tim Back End mengintegrasikan Room/SQLite database lokal. Dokumen serah terima dapat dilihat di [`docs/serah-terima-backend.md`](docs/serah-terima-backend.md).

---

## Fitur Utama Front End

1. **Ringkasan (Overview):**
   - Daftar to-do gabungan dari Tugas, Ujian, dan Pekerjaan Lain.
   - Badge penanda jenis yang berbeda warna dan jelas.
   - Urutan otomatis berdasarkan tenggat terdekat.
   - Tombol tambah (+) dengan dialog pemilih jenis item.
   - Centang status selesai dan aksi hapus dengan konfirmasi.

2. **Daftar Kelas (Classes):**
   - Kartu kelas lengkap dengan jadwal, ruang, seksi, dan dosen pengampu.
   - Form tambah dan ubah kelas dengan validasi nama wajib dan urutan jam kuliah.
   - Pemilih hari kuliah multi-pilihan dan pemilih jam 24 jam.

3. **Daftar Tugas (Assignments):**
   - Menampilkan tenggat waktu format lokal Indonesia dan mata kuliah.
   - Kontrol pengurutan (Nama, Tenggat, Mata Kuliah).
   - Filter status penyelesaian (Semua, Belum Selesai, Selesai).
   - Penanganan kondisi filter kosong vs data kosong.

4. **Daftar Ujian (Exams):**
   - Menampilkan jadwal ujian, mata kuliah, dan lokasi ujian.
   - Kontrol urut dan filter status sesuai aturan bisnis BR-5.

5. **Keamanan Data & Validasi (BR-2):**
   - Tidak ada penghapusan langsung; seluruh aksi hapus melalui dialog konfirmasi `ConfirmDialog`.

---

## Prasyarat & Menjalankan Aplikasi

- **Bahasa:** Java
- **Build System:** Gradle Kotlin DSL
- **JDK:** OpenJDK 17
- **Target SDK:** Android 37 (Min SDK: 24 / Android 7.0 Nougat)

### Perintah Build & Pengujian
```bash
# Menjalankan unit test
./gradlew testDebugUnitTest

# Membuat APK Debug
./gradlew assembleDebug

# Menjalankan analisis lint
./gradlew lintDebug
```

---

## Dokumentasi Terkait
- [Catatan Serah Terima Back End](docs/serah-terima-backend.md)
- [Checklist Uji Front End](docs/checklist-uji-frontend.md)
- [PRD College Scheduler](brief/PRD-college-scheduler.md)
- [Brief Proyek](brief/brief-college-scheduler.md)
