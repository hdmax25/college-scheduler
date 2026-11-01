# College Scheduler 🎓

Aplikasi Android native untuk manajemen jadwal perkuliahan, tugas, ujian, dan daftar agenda (*to-do list*) akademik mahasiswa. Dibangun dengan prinsip **offline-first**, mengadopsi standar desain modern **Material 3 (M3)**, dan seluruh antarmuka disajikan dalam **Bahasa Indonesia**.

Proyek ini dikembangkan mengacu pada arsitektur dan spesifikasi kebutuhan fungsional dari repository referensi [AlexT101/college-scheduler](https://github.com/AlexT101/college-scheduler) dengan peningkatan kualitas UI/UX, validasi input yang ketat, dan proteksi penghapusan data.

---

## 📌 Daftar Isi

1. [Fitur Utama](#-fitur-utama)
2. [Aturan Bisnis & Prinsip Desain](#-aturan-bisnis--prinsip-desain)
3. [Arsitektur & Struktur Proyek](#-arsitektur--struktur-proyek)
4. [Teknologi & Lingkungan Pengembangan](#-teknologi--lingkungan-pengembangan)
5. [Panduan Menjalankan & Membangun](#-panduan-menjalankan--membangun)
6. [Pengujian Otomatis & Kualitas Kode](#-pengujian-otomatis--kualitas-kode)
7. [Status Pengembangan & Rencana Lanjutan](#-status-pengembangan--rencana-lanjutan)
8. [Dokumentasi Terkait](#-dokumentasi-terkait)

---

## ✨ Fitur Utama

Aplikasi memiliki 4 menu navigasi utama pada bilah bawah (*Bottom Navigation*):

### 1. Ringkasan (*Overview*)
- **Daftar To-Do Terpadu (FR-5, US-8):** Menggabungkan seluruh data Tugas, Ujian, dan Pekerjaan Lain ke dalam satu layar ringkasan terpadu.
- **Badge Tipe Berwarna:** Setiap item memiliki penanda visual yang kontras (Tugas: *Biru/Primary*, Ujian: *Oranye/Tertiary*, Pekerjaan Lain: *Hijau/Secondary*).
- **Urutan Kronologis:** Secara otomatis diurutkan berdasarkan tenggat waktu atau waktu pelaksanaan terdekat.
- **Tombol Tambah Fleksibel:** Tombol aksi mengambang (*FAB*) memunculkan dialog pemilihan tipe item (`Tugas`, `Ujian`, atau `Pekerjaan`) sebelum diarahkan ke form pembuatan.
- **Aksi Cepat:** Mendukung penandaan selesai langsung (*checkbox*), klik baris untuk mengubah, dan tekan lama untuk menghapus item.

### 2. Jadwal Kelas (*Classes*)
- **Informasi Lengkap Perkuliahan (FR-1, US-1):** Menampilkan nama mata kuliah, jadwal hari, rentang jam (`HH:mm`), ruang kelas, kode seksi/kelas paralel, dan nama dosen pengampu.
- **Form Interaktif Kelas:**
  - Pemilih hari perkuliahan multi-pilihan (*multi-choice dialog*).
  - Pemilih jam 24 jam interaktif (`MaterialTimePicker`).
  - Validasi ketat: nama wajib diisi dan jam selesai harus setelah jam mulai.
- **Kondisi Kosong (*Empty State*):** Menampilkan ilustrasi dan tombol ajakan saat belum ada kelas yang didaftarkan.

### 3. Daftar Tugas (*Assignments*)
- **Manajemen Tenggat (FR-2, US-2):** Menampilkan nama tugas, mata kuliah terkait, dan tenggat waktu berformat lokal Indonesia (`d MMM yyyy, HH:mm`).
- **Penanda Selesai Visual (FR-4, US-6):** Mencentang tugas memberikan umpan balik visual instan berupa teks dicoret (*strike-through*) dan kartu agak meredup (*opacity*).
- **Pengurutan Fleksibel (FR-3, US-5):** Pengguna dapat mengurutkan daftar berdasarkan:
  - *Nama* (A ke Z)
  - *Tenggat Waktu* (terdekat ke terjauh)
  - *Mata Kuliah* (alfabetis mata kuliah)
- **Penyaringan Status (FR-4, US-7):** Pilihan filter chip responsif: `Semua`, `Belum Selesai`, dan `Selesai`.
- **Kondisi Filter Kosong:** Menampilkan pesan *"Tidak ada item yang cocok"* yang berbeda dari kondisi *"Belum ada data"*.

### 4. Daftar Ujian (*Exams*)
- **Jadwal & Lokasi Ujian (FR-2, US-3):** Menampilkan jadwal pelaksanaan ujian, mata kuliah, serta informasi ruang/lokasi ujian.
- **Status Pelaksanaan (BR-5):** Dilengkapi penanda checkbox selesai untuk mencatat ujian yang telah dilewati.
- **Pengurutan & Filter:** Mendukung fitur sortir dan filter status yang konsisten dengan layar tugas.

### 5. Pekerjaan Lain (*Work Tasks*)
- **Pekerjaan Akademik Tambahan (FR-2, US-4):** Mengakomodasi kegiatan di luar tugas dan ujian (seperti membeli buku, membentuk kelompok, atau membaca modul) yang dikelola secara terpusat melalui halaman Ringkasan.

---

## 🛡️ Aturan Bisnis & Prinsip Desain

| Aturan | Implementasi pada Aplikasi |
|---|---|
| **BR-1: Offline-First** | 100% berjalan tanpa koneksi internet. `AndroidManifest.xml` tidak meminta izin `android.permission.INTERNET`, dan tidak ada ketergantungan pada backend eksternal atau Firebase. |
| **BR-2: Konfirmasi Hapus** | Tidak ada penghapusan langsung secara instan. Setiap pemicu hapus (tekan lama baris maupun tombol hapus di toolbar form) wajib melalui `ConfirmDialog` dengan opsi *Hapus* dan *Batal*. |
| **BR-3: Status Selesai Persisten** | Status `isCompleted` tersimpan secara mandiri untuk tiap item dan tersinkronisasi antar layar. |
| **BR-4: Lokalisasi Penuh** | Seluruh teks antarmuka (label, placeholder, pesan validasi, teks dialog, dan toast) menggunakan Bahasa Indonesia yang terpusat di `res/values/strings.xml`. |
| **BR-5: Kesetaraan Ujian** | Status penyelesaian pada Ujian diperlakukan setara dengan Tugas dan Pekerjaan. |

---

## 🏛️ Arsitektur & Struktur Proyek

Aplikasi menerapkan pola **Single Activity Architecture** yang bersih dan terstruktur:

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/myapplication/
│   ├── MainActivity.java             # Host navigasi utama (BottomNavigationView)
│   ├── OverviewFragment.java         # Layar Ringkasan gabungan
│   ├── ClassesFragment.java          # Layar Daftar Kelas
│   ├── AssignmentsFragment.java      # Layar Daftar Tugas
│   ├── ExamsFragment.java            # Layar Daftar Ujian
│   ├── ClassFormActivity.java        # Form Tambah/Ubah Kelas
│   ├── ItemFormActivity.java         # Form Polimorfik (Tugas / Ujian / Pekerjaan)
│   ├── adapter/
│   │   ├── ClassAdapter.java         # Adapter RecyclerView Kelas
│   │   ├── AssignmentAdapter.java    # Adapter RecyclerView Tugas
│   │   ├── ExamAdapter.java          # Adapter RecyclerView Ujian
│   │   └── OverviewAdapter.java      # Adapter RecyclerView Ringkasan dengan badge tipe
│   ├── data/
│   │   └── InMemoryRepository.java   # Single Source of Truth sementara & data dummy
│   ├── model/
│   │   ├── CourseClass.java          # Model data Kelas
│   │   ├── Assignment.java           # Model data Tugas
│   │   ├── Exam.java                 # Model data Ujian
│   │   ├── WorkTask.java             # Model data Pekerjaan Lain
│   │   └── OverviewItem.java         # Wrapper seragam untuk tampilan Ringkasan
│   └── util/
│       ├── ConfirmDialog.java        # Helper dialog konfirmasi hapus (Material 3)
│       ├── DateFormatUtils.java      # Parser & formatter tanggal-jam lokal Indonesia
│       ├── ItemSorter.java           # Logika sorting & filtering di lapisan UI
│       └── ValidationUtils.java      # Utilitas validasi input form
└── res/
    ├── drawable/                     # Vektor ikon Material Design & background badge
    ├── layout/                       # File layout XML deklaratif
    ├── menu/                         # Menu BottomNavigation & Toolbar Form
    └── values/
        ├── colors.xml                # Definisi palet warna Material 3
        ├── dimens.xml                # Spacing, padding, dan ukuran tipografi
        ├── strings.xml               # Lokalisasi string Bahasa Indonesia
        └── themes.xml                # Tema Light & Dark Mode Material 3
```

---

## 🛠️ Teknologi & Lingkungan Pengembangan

- **Platform:** Android Native
- **Bahasa Pemrograman:** Java (JDK 17)
- **Min SDK:** Android 7.0 (API Level 24)
- **Target / Compile SDK:** Android 16 (API Level 37)
- **Build System:** Gradle Kotlin DSL (`build.gradle.kts`)
- **Pustaka Utama:**
  - `androidx.appcompat:appcompat`
  - `com.google.android.material:material:1.13.0` (Material Design 3)
  - `androidx.constraintlayout:constraintlayout`
  - `androidx.activity:activity-ktx`
- **Pustaka Pengujian:** JUnit 4, AndroidX Test Runner, Espresso Core.

---

## 🚀 Panduan Menjalankan & Membangun

### Prasyarat
1. Telah terpasang **Android Studio Ladybug (atau versi lebih baru)** atau JDK 17+.
2. Emulator Android atau perangkat fisik dengan USB Debugging aktif (Min API 24).

### Perintah Gradle (CLI)

Gunakan `gradlew` pada root folder proyek:

```bash
# 1. Menjalankan seluruh Unit Test otomatis
./gradlew testDebugUnitTest

# 2. Melakukan kompilasi dan membuat APK Debug
./gradlew assembleDebug

# Output file APK berada di:
# app/build/outputs/apk/debug/app-debug.apk

# 3. Menjalankan analisis statis kode (Android Lint)
./gradlew lintDebug
```

*Catatan untuk pengguna Windows PowerShell:*
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
.\gradlew testDebugUnitTest assembleDebug
```

---

## 🧪 Pengujian Otomatis & Kualitas Kode

Proyek ini dilengkapi dengan suite pengujian otomatis menyeluruh:

| Test Suite | File Pengujian | Cakupan Pengujian | Status |
|---|---|---|---|
| **Repository CRUD** | `InMemoryRepositoryTest.java` | Operasi Tambah, Baca, Ubah, Hapus untuk Kelas, Tugas, Ujian, Pekerjaan Lain, serta agregasi Ringkasan. | ✅ 100% Lolos |
| **Validasi Input** | `ValidationUtilsTest.java` | Pengecekan teks kosong/spasi, perbandingan urutan jam, format waktu 24 jam, dan kelengkapan tanggal. | ✅ 100% Lolos |
| **Format Tanggal** | `DateFormatUtilsTest.java` | Parsing string ISO `yyyy-MM-dd HH:mm` ke format lokal Indonesia `d MMM yyyy, HH:mm` dan fallback aman. | ✅ 100% Lolos |
| **Sort & Filter** | `ItemSorterTest.java` | Pengurutan Nama, Tanggal, Mata Kuliah, penyaringan status (Semua/Selesai/Belum), dan sortir Ringkasan. | ✅ 100% Lolos |

Analisis Lint menghasilkan **0 Error** dan memastikan kepatuhan penuh terhadap standar aksesibilitas serta tata letak Android.

---

## 📋 Status Pengembangan & Rencana Lanjutan

- [x] **FE-01:** Kerangka Navigasi Bottom Navigation & Fragment Skeleton
- [x] **FE-02:** Bahasa Indonesia, `strings.xml`, & Desain Tema Dasar Material 3
- [x] **FE-03:** Model Data & `InMemoryRepository` (Data Dummy Awal)
- [x] **FE-04:** Layar Daftar Kelas & Komponen Tampilan
- [x] **FE-05:** Form Tambah & Ubah Kelas dengan Validasi Waktu
- [x] **FE-06:** Layar Daftar Tugas & Penanda Selesai
- [x] **FE-07:** Form Item Terpadu (Tugas, Ujian dengan Kolom Lokasi, Pekerjaan)
- [x] **FE-08:** Layar Daftar Ujian
- [x] **FE-09:** Kontrol Pengurutan (*Sort*) & Penyaringan (*Filter*)
- [x] **FE-10:** Dialog Konfirmasi Hapus Reusable (`ConfirmDialog`)
- [x] **FE-11:** Layar Ringkasan Terpadu (*Overview To-Do List*)
- [x] **FE-12:** Audit Kualitas Front End & Catatan Serah Terima Back End
- [ ] **Back End (Rencana Selanjutnya):** Integrasi basis data lokal Room/SQLite untuk persistensi data permanen antar sesi aplikasi.

---

## 📚 Dokumentasi Terkait

- [Catatan Serah Terima ke Back End](docs/serah-terima-backend.md) — Rincian kontrak publik repository dan petunjuk integrasi database.
- [Checklist Uji Akhir Front End](docs/checklist-uji-frontend.md) — Matriks verifikasi seluruh skenario pengujian UI.
- [Product Requirement Document (PRD)](brief/PRD-college-scheduler.md) — Dokumen spesifikasi kebutuhan produk v0.1.
- [Brief Proyek](brief/brief-college-scheduler.md) — Dokumen panduan dan batasan proyek.

---

*Dikembangkan untuk Tugas Mata Kuliah Pemrograman Bergerak (Mobile Programming).*
