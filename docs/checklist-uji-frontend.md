# Checklist Uji Akhir Front End (FE-12)

Dokumen ini memverifikasi seluruh fungsionalitas antarmuka (Front End) aplikasi **College Scheduler** sesuai acuan PRD v0.1 dan `brief-college-scheduler.md`.

---

## 1. Verifikasi Batasan & Lingkungan (Non-Fungsional & BR-1)

| No | Kriteria Pengujian | Status | Keterangan |
|---|---|---|---|
| 1.1 | Tidak ada permission `android.permission.INTERNET` di `AndroidManifest.xml` (BR-1) | [x] Lolos | Aplikasi 100% offline-first |
| 1.2 | Tidak ada library jaringan (Retrofit, OkHttp, Volley) atau Firebase di `build.gradle.kts` | [x] Lolos | Hanya dependensi AndroidX dan Material 3 |
| 1.3 | Seluruh teks antarmuka berada di `strings.xml` dalam Bahasa Indonesia (BR-4) | [x] Lolos | Tidak ada hardcoded string di XML layout maupun Java |
| 1.4 | Tema Material 3 konsisten dengan dukungan Light/Dark mode | [x] Lolos | Palet warna container M3 terkonfigurasi di `themes.xml` & `colors.xml` |

---

## 2. Verifikasi Alur CRUD & Validasi

### 2.1 Modul Kelas (Class) - FR-1, US-1
| No | Skenario Uji | Status | Keterangan |
|---|---|---|---|
| 2.1.1 | Daftar kelas menampilkan data contoh (nama, hari, jam, ruang, seksi, dosen) | [x] Lolos | Ditampilkan via `ClassAdapter` |
| 2.1.2 | Tombol tambah (+) membuka form kelas dengan judul "Tambah Kelas" | [x] Lolos | `ClassFormActivity` mode tambah |
| 2.1.3 | Validasi nama kelas wajib dan tidak boleh kosong setelah `trim` | [x] Lolos | Muncul pesan error di bawah input |
| 2.1.4 | Pemilih hari kuliah menggunakan dialog multi-pilihan berbasis `R.array.days_of_week` | [x] Lolos | Minimal satu hari wajib dipilih |
| 2.1.5 | Pemilih jam 24 jam (`MaterialTimePicker`) untuk jam mulai & selesai | [x] Lolos | Format `HH:mm` |
| 2.1.6 | Validasi jam selesai harus setelah jam mulai | [x] Lolos | Muncul error jika jam selesai <= jam mulai |
| 2.1.7 | Klik baris kelas membuka mode ubah dengan data lama terisi | [x] Lolos | Memperbarui data di repositori |
| 2.1.8 | Tekan lama atau tombol hapus memunculkan dialog konfirmasi (BR-2) | [x] Lolos | `ConfirmDialog.showDeleteClass()` |
| 2.1.9 | Saat semua kelas terhapus, muncul empty state & tombol "Tambah Kelas Sekarang" | [x] Lolos | Tampilan kosong terisolasi dengan rapi |

### 2.2 Modul Tugas (Assignment) - FR-2, US-2, FR-3, FR-4
| No | Skenario Uji | Status | Keterangan |
|---|---|---|---|
| 2.2.1 | Daftar tugas menampilkan nama, mata kuliah, tenggat (format Indonesia), dan checkbox | [x] Lolos | Ditampilkan via `AssignmentAdapter` |
| 2.2.2 | Kolom lokasi tersembunyi pada form Tugas | [x] Lolos | Kolom lokasi hanya untuk jenis Ujian |
| 2.2.3 | Validasi nama, mata kuliah, tanggal, dan jam wajib diisi | [x] Lolos | Error terarah pada masing-masing field |
| 2.2.4 | Centang checkbox menandai status selesai (efek coret & pudar) | [x] Lolos | Sinkron dengan repository |
| 2.2.5 | Pengurutan berdasarkan Nama (A-Z), Tenggat (terdekat), Mata Kuliah | [x] Lolos | `ItemSorter` berjalan di UI layer |
| 2.2.6 | Filter status: Semua / Belum Selesai / Selesai | [x] Lolos | Chip filter responsif |
| 2.2.7 | Tampilan hasil filter kosong ("Tidak ada item yang cocok") | [x] Lolos | Berbeda dari kondisi data belum ada |
| 2.2.8 | Tekan lama baris atau menu toolbar hapus memicu dialog konfirmasi (BR-2) | [x] Lolos | `ConfirmDialog.showDeleteItem()` |

### 2.3 Modul Ujian (Exam) - FR-2, US-3, BR-5
| No | Skenario Uji | Status | Keterangan |
|---|---|---|---|
| 2.3.1 | Daftar ujian menampilkan nama, waktu ujian, mata kuliah, lokasi, dan status selesai | [x] Lolos | Sesuai BR-5 |
| 2.3.2 | Form Ujian menampilkan input Lokasi | [x] Lolos | Nilai tersimpan dan tampil di baris |
| 2.3.3 | Pengurutan dan filter status pada Ujian | [x] Lolos | Konsisten dengan modul Tugas |
| 2.3.4 | Tekan lama atau menu toolbar hapus memicu dialog konfirmasi (BR-2) | [x] Lolos | `ConfirmDialog.showDeleteItem()` |

### 2.4 Modul Ringkasan (Overview) - FR-5, US-4, US-8
| No | Skenario Uji | Status | Keterangan |
|---|---|---|---|
| 2.4.1 | Menampilkan gabungan Tugas, Ujian, dan Pekerjaan dalam satu daftar to-do | [x] Lolos | 12 data contoh awal tampil seragam |
| 2.4.2 | Badge jenis item berwarna jelas (Tugas: Biru, Ujian: Oranye, Pekerjaan: Hijau) | [x] Lolos | `bg_badge_pill` dengan kontras M3 |
| 2.4.3 | Urutan awal berdasarkan tenggat/waktu terdekat | [x] Lolos | Terurut kronologis otomatis |
| 2.4.4 | FAB tambah (+) memunculkan dialog pilihan jenis item (Tugas, Ujian, Pekerjaan) | [x] Lolos | Membuka form sesuai tipe yang dipilih |
| 2.4.5 | Centang selesai di Ringkasan langsung tersinkron ke daftar Tugas/Ujian | [x] Lolos | Berbagi data di `InMemoryRepository` |
| 2.4.6 | Klik baris membuka mode ubah item sesuai jenisnya | [x] Lolos | Mendukung CRUD Pekerjaan (US-4) |
| 2.4.7 | Tekan lama memunculkan dialog konfirmasi hapus (BR-2) | [x] Lolos | Item terhapus dan list ter-refresh |
| 2.4.8 | Empty state tampil saat ketiga jenis item kosong | [x] Lolos | Tombol tambah memicu dialog pilih jenis |

---

## 3. Hasil Pengujian Unit Test Otomatis

- `InMemoryRepositoryTest`: 5 skenario lolos (Initial count, Class CRUD, Assignment CRUD & toggle, Exam CRUD & toggle, WorkTask CRUD & toggle, Overview items aggregation).
- `ValidationUtilsTest`: 7 skenario lolos (NonEmpty, TimeAfter, ValidTimeFormat, CompleteDateTime).
- `DateFormatUtilsTest`: 4 skenario lolos (Format ISO datetime, ISO date, fallback, locale id-ID).
- `ItemSorterTest`: 7 skenario lolos (Sort assignments, sort exams, filter status, empty filter state, sort overview items).
- **Hasil Build & Lint:** `.\gradlew testDebugUnitTest assembleDebug lintDebug` ➔ **0 ERROR, BUILD SUCCESSFUL**.

