# PRD: College Scheduler

| Item | Isi |
|---|---|
| Versi dokumen | 0.1 (Draft) |
| Tanggal | 31 Oktober 2026 |
| Status | Draft untuk review |
| Pemilik produk | *diisi pemilik proyek* |
| Sumber | `brief-college-scheduler.md`; repository acuan [AlexT101/college-scheduler](https://github.com/AlexT101/college-scheduler) |
| Konteks | Proyek tugas mata kuliah *(diisi pemilik proyek)*; tenggat dan anggota tim *(diisi pemilik proyek)* |

**Legenda tanda**
- `[ASUMSI]`: keputusan default yang belum dikonfirmasi. Ubah jika tidak sesuai.
- `[BELUM TERVERIFIKASI]`: hal yang belum bisa dipastikan dari README repo acuan dan harus dicek di source code sebelum dianggap fakta.

---

## 1. Ringkasan Produk

College Scheduler adalah aplikasi Android native untuk mengelola jadwal akademik mahasiswa. Pengguna mencatat **Kelas**, **Tugas**, **Ujian**, dan **Pekerjaan lain**, lalu mengurutkan, memfilter, dan menandainya selesai. Satu halaman **Ringkasan** menampilkan semua tugas, ujian, dan pekerjaan sebagai satu daftar to-do. Semua data tersimpan lokal di perangkat, tanpa backend, login, atau Firebase.

Produk ini dirancang mengikuti repository acuan dan sengaja dibatasi pada fitur yang tercantum di README repo tersebut.

## 2. Problem Statement

Mahasiswa harus mengingat jadwal kuliah, tugas, ujian, dan pekerjaan lain dengan tenggat berbeda. Informasinya tersebar di grup chat, LMS, dan catatan pribadi, sehingga:

- tenggat mudah terlewat,
- sulit menentukan mana yang harus dikerjakan lebih dulu,
- sulit memantau mana yang sudah dan belum selesai.

**Pertanyaan yang harus dijawab produk:**
1. Bagaimana menyimpan jadwal kuliah, tugas, dan ujian dalam satu aplikasi?
2. Bagaimana mahasiswa melihat dan mengurutkan item berdasarkan tenggat, nama, atau mata kuliah?
3. Bagaimana mahasiswa menandai dan memantau item yang sudah dan belum selesai?

## 3. Tujuan dan Non-Tujuan

### 3.1 Tujuan
- Pengguna dapat menambah, melihat, mengubah, dan menghapus Kelas, Tugas, Ujian, dan Pekerjaan.
- Pengguna dapat mengurutkan item berdasarkan nama, tenggat, atau mata kuliah.
- Pengguna dapat menandai item selesai dan menyaring item selesai atau belum selesai.
- Pengguna dapat melihat semua tugas, ujian, dan pekerjaan dalam satu halaman Ringkasan.
- Data tetap ada setelah aplikasi ditutup dan dibuka kembali.

### 3.2 Non-Tujuan (versi ini)
Lihat Bagian 6.2 untuk daftar lengkap. Intinya: tidak ada notifikasi, login, sinkronisasi, kategori/prioritas, ekspor, widget, integrasi kalender/LMS, atau pencarian teks.

### 3.3 Indikator Keberhasilan
Karena ini proyek tugas tanpa analitik, keberhasilan diukur dengan **kriteria selesai** (Bagian 9.4). Target metrik kuantitatif (misalnya jumlah pengguna atau retensi) tidak ada di brief dan tidak ditetapkan di sini.

## 4. Target Pengguna

### 4.1 Aktor
**Aktor tunggal: Pengguna (mahasiswa).** Tidak ada login dan tidak ada aktor lain.

### 4.2 Persona `[ASUMSI]`
*Brief tidak memuat persona; berikut gambaran singkat yang diturunkan dari latar belakang masalah. Validasi atau ganti dengan data pengguna nyata jika ada.*

| Atribut | Isi |
|---|---|
| Nama persona | Mahasiswa aktif |
| Konteks | Mengikuti beberapa mata kuliah dalam satu semester, masing-masing dengan tugas dan ujian |
| Kebutuhan | Satu tempat untuk melihat semua jadwal dan tenggat, tahu apa yang harus dikerjakan lebih dulu |
| Masalah saat ini | Informasi tersebar di grup chat, LMS, dan catatan pribadi; tenggat terlewat |
| Perangkat | Ponsel Android; bekerja offline karena data lokal |

## 5. Istilah

| Istilah | Arti |
|---|---|
| Kelas | Satu mata kuliah terjadwal (nama, hari dan jam, ruang, seksi, dosen) |
| Tugas (Assignment) | Pekerjaan dengan nama, tenggat (tanggal dan jam), dan mata kuliah |
| Ujian (Exam) | Ujian dengan nama, tanggal dan jam, mata kuliah, dan lokasi |
| Pekerjaan (Task) | Pekerjaan lain dengan nama, tenggat (tanggal dan jam), dan mata kuliah |
| Item | Sebutan umum untuk Tugas, Ujian, dan Pekerjaan |
| Ringkasan (Overview) | Halaman yang menampilkan semua item dalam satu daftar |

## 6. Fitur dan Batasan

### 6.1 Fitur Utama

| ID | Fitur | Deskripsi | Prioritas `[ASUMSI]` |
|---|---|---|---|
| FR-1 | Pengelolaan Kelas | CRUD Kelas; data: nama, hari dan waktu, ruang/lokasi, seksi, dosen; daftar Kelas di halaman tersendiri | Must |
| FR-2 | Pengelolaan Tugas, Ujian, Pekerjaan | CRUD ketiga jenis item; Tugas dan Ujian punya halaman sendiri | Must |
| FR-3 | Pengurutan | Urut berdasarkan nama, tenggat, atau mata kuliah | Must |
| FR-4 | Status selesai dan filter | Tandai item selesai; filter selesai atau belum selesai | Must |
| FR-5 | Halaman Ringkasan | Satu daftar to-do berisi semua Tugas, Ujian, dan Pekerjaan | Must |
| FR-6 | Penyimpanan lokal | Data bertahan setelah aplikasi ditutup | Must |

Semua fungsi di atas tercantum di README repo acuan.

**Atribut data per item**

| Jenis | Atribut |
|---|---|
| Kelas | nama, hari kuliah, jam mulai, jam selesai, ruang, seksi, dosen |
| Tugas | nama, tenggat (tanggal+jam), mata kuliah, status selesai |
| Ujian | nama, tanggal+jam, mata kuliah, lokasi, status selesai `[BELUM TERVERIFIKASI]` |
| Pekerjaan | nama, tenggat (tanggal+jam), mata kuliah, status selesai |

*Nama field, tipe data, dan primary key masih rancangan `[ASUMSI]` sampai source code repo acuan dicek.*

**Relasi:** field mata kuliah pada item apakah teks bebas atau foreign key ke Kelas `[BELUM TERVERIFIKASI]`. Sampai dicek, dokumentasikan sebagai "terhubung melalui nama mata kuliah".

### 6.2 Batasan Versi Ini (Out of Scope)
- Tidak ada notifikasi atau pengingat (tidak tercantum di README repo acuan).
- Tidak ada login, akun, backend, Firebase, atau sinkronisasi antar perangkat.
- Data hanya ada di satu perangkat dan hilang jika aplikasi dihapus.
- Tidak ada kategori, label, atau prioritas.
- Tidak ada berbagi atau ekspor data.
- Tidak ada pengulangan jadwal otomatis, integrasi kalender perangkat, atau integrasi LMS.
- Tidak ada widget layar utama.
- Tidak ada pencarian teks; pengguna memakai pengurutan dan filter.

### 6.3 Pengembangan Lanjutan (di luar versi ini)

| Prioritas | Pengembangan |
|---|---|
| Tinggi | Notifikasi pengingat tenggat (sekitar 24 jam sebelumnya), dijadwalkan lokal tanpa server |
| Sedang | Notifikasi jadwal kuliah (hari ini/besok, menyebut mata kuliah dan dosen) |
| Sedang | Filter tambahan; tampilan jadwal per hari atau per minggu |
| Rendah | Backup dan ekspor data, widget, sinkronisasi antar perangkat |

## 7. Alur Pengguna dan User Story

### 7.1 Alur Utama
1. **Menambah item:** buka halaman jenis item → tekan tombol tambah → isi data → simpan. Jika tidak valid, tampil pesan error; jika valid, item tersimpan dan muncul di daftar.
2. **Mengubah/menghapus item:** pilih item → ubah data lalu simpan, atau hapus.
3. **Menandai selesai:** tandai item selesai; status tersimpan.
4. **Mengurutkan dan memfilter:** pilih kriteria urut (nama, tenggat, mata kuliah) dan filter (selesai/belum); daftar diperbarui.
5. **Melihat ringkasan:** buka halaman Ringkasan untuk melihat semua tugas, ujian, dan pekerjaan.

*Ruang untuk diagram (flowchart / use case / activity diagram) diisi pemilik proyek:*

> `[Gambar diagram di sini]`

### 7.2 User Story

| ID | Sebagai | Saya ingin | Agar | Fitur |
|---|---|---|---|---|
| US-1 | Mahasiswa | menambah, melihat, mengubah, dan menghapus Kelas | jadwal kuliah saya tercatat di satu tempat | FR-1 |
| US-2 | Mahasiswa | menambah, melihat, mengubah, dan menghapus Tugas | saya tahu tugas apa yang harus dikumpulkan dan kapan | FR-2 |
| US-3 | Mahasiswa | menambah, melihat, mengubah, dan menghapus Ujian | saya tahu kapan dan di mana ujian berlangsung | FR-2 |
| US-4 | Mahasiswa | menambah, melihat, mengubah, dan menghapus Pekerjaan lain | pekerjaan di luar tugas dan ujian tetap terpantau | FR-2 |
| US-5 | Mahasiswa | mengurutkan item berdasarkan nama, tenggat, atau mata kuliah | saya bisa menentukan prioritas pengerjaan | FR-3 |
| US-6 | Mahasiswa | menandai item selesai | saya tahu progres saya | FR-4 |
| US-7 | Mahasiswa | memfilter item selesai atau belum selesai | saya fokus pada yang belum dikerjakan | FR-4 |
| US-8 | Mahasiswa | melihat semua tugas, ujian, dan pekerjaan dalam satu halaman | saya punya gambaran menyeluruh | FR-5 |
| US-9 | Mahasiswa | data saya tetap ada setelah aplikasi ditutup | saya tidak perlu input ulang | FR-6 |

### 7.3 Use Case (ringkas)

| Use case | Aktor | Hasil |
|---|---|---|
| Kelola Kelas | Pengguna | Kelas tersimpan/berubah/terhapus |
| Kelola Tugas / Ujian / Pekerjaan | Pengguna | Item tersimpan/berubah/terhapus |
| Urutkan dan filter daftar | Pengguna | Daftar sesuai kriteria |
| Tandai selesai | Pengguna | Status item tersimpan |
| Lihat Ringkasan | Pengguna | Daftar gabungan tampil |

## 8. Layar dan Kondisi Khusus

### 8.1 Daftar Layar `[ASUMSI: nama layar mengikuti README, belum diverifikasi di kode]`

| No | Layar | Isi utama |
|---|---|---|
| 1 | Ringkasan (Overview) | Daftar gabungan Tugas, Ujian, dan Pekerjaan |
| 2 | Kelas | Daftar Kelas, tombol tambah |
| 3 | Tugas | Daftar Tugas, urut, filter, tandai selesai |
| 4 | Ujian | Daftar Ujian |
| 5 | Form Kelas | Nama, hari dan jam, ruang, seksi, dosen |
| 6 | Form Item | Nama, tenggat/waktu, mata kuliah (lokasi untuk ujian) |

### 8.2 Kondisi Kosong dan Error

| Situasi | Perilaku |
|---|---|
| Daftar kosong | Teks "Belum ada data" dan tombol tambah |
| Input tidak valid | Pesan error di bawah kolom yang salah |
| Hapus item | Dialog konfirmasi sebelum dihapus |
| Hasil filter kosong | Teks "Tidak ada item yang cocok" |

## 9. Acceptance Criteria dan Aturan Bisnis

### 9.1 Aturan Validasi `[ASUMSI]`
*Repo acuan tidak menjelaskan validasi; ini usulan untuk versi tugas.*

| Field | Aturan |
|---|---|
| Nama (kelas, tugas, ujian, pekerjaan) | Wajib; tidak boleh kosong setelah `trim` |
| Mata kuliah (pada item) | Wajib |
| Tenggat / waktu | Tanggal dan jam harus terisi lengkap |
| Hari dan jam kelas | Jam selesai harus setelah jam mulai |
| Ruang, seksi, dosen, lokasi ujian | Opsional |

Pesan error berbahasa Indonesia, ditampilkan di dekat kolom yang salah.

### 9.2 Aturan Bisnis Umum
- BR-1: Semua data disimpan lokal; tidak ada komunikasi ke server.
- BR-2: Penghapusan item selalu didahului dialog konfirmasi.
- BR-3: Status selesai bersifat per item dan tersimpan permanen.
- BR-4: Bahasa antarmuka Indonesia `[ASUMSI]`; semua teks berada di `strings.xml`.
- BR-5: Status selesai pada Ujian berlaku seperti item lain `[BELUM TERVERIFIKASI]`.

### 9.3 Acceptance Criteria per Fitur

| ID | Kriteria (Given / When / Then) |
|---|---|
| FR-1 | **Given** form Kelas terisi valid, **when** pengguna menyimpan, **then** Kelas muncul di daftar Kelas dan tetap ada setelah aplikasi dibuka ulang. Edit dan hapus (dengan konfirmasi) memperbarui daftar. |
| FR-2 | **Given** form Tugas/Ujian/Pekerjaan terisi valid, **when** disimpan, **then** item muncul di daftar jenisnya. **Given** nama atau mata kuliah kosong, **when** disimpan, **then** muncul pesan error dan item tidak tersimpan. |
| FR-3 | **Given** daftar berisi beberapa item, **when** pengguna memilih urut nama / tenggat / mata kuliah, **then** daftar berurutan sesuai kriteria. |
| FR-4 | **Given** sebuah item, **when** ditandai selesai, **then** statusnya berubah dan tersimpan. **When** filter "selesai" atau "belum selesai" dipilih, **then** hanya item sesuai status yang tampil; bila tidak ada, tampil "Tidak ada item yang cocok". |
| FR-5 | **Given** ada Tugas, Ujian, dan Pekerjaan, **when** pengguna membuka Ringkasan, **then** ketiganya tampil dalam satu daftar. |
| FR-6 | **Given** ada data tersimpan, **when** aplikasi ditutup lalu dibuka lagi, **then** semua data masih ada. |

### 9.4 Kriteria Selesai (Definition of Done)
- [ ] Kelas, Tugas, Ujian, dan Pekerjaan dapat ditambah, dilihat, diubah, dan dihapus.
- [ ] Item dapat diurutkan berdasarkan nama, tenggat, dan mata kuliah.
- [ ] Item dapat ditandai selesai dan difilter (selesai atau belum).
- [ ] Halaman Ringkasan menampilkan semua tugas, ujian, dan pekerjaan.
- [ ] Data tidak hilang setelah aplikasi ditutup dan dibuka lagi.
- [ ] Input tidak valid menampilkan pesan error yang jelas.

## 10. Kebutuhan Non-Fungsional `[ASUMSI]`
*Tidak disebut di brief; usulan minimal untuk proyek tugas.*
- **Offline-first:** seluruh fungsi berjalan tanpa internet.
- **Sederhana:** tidak merombak arsitektur repo acuan.
- **Dapat dijelaskan:** struktur kode cukup sederhana untuk dipresentasikan.
- **Lokalisasi:** semua teks di `strings.xml`.

## 11. Teknologi dan Tools

| Bagian | Pilihan | Status |
|---|---|---|
| Platform | Android native | Ditetapkan |
| Bahasa | Java | Ditetapkan (sesuai README repo acuan) |
| UI | Layout Android bawaan | `[BELUM TERVERIFIKASI: XML atau Compose]` |
| IDE dan build | Android Studio, Gradle Kotlin DSL | Ditetapkan (`build.gradle.kts` di repo acuan) |
| Penyimpanan data | Lokal di perangkat | `[BELUM TERVERIFIKASI: Room / SQLite / lainnya]` |
| `minSdk` / `targetSdk` | Mengikuti repo acuan | `[BELUM TERVERIFIKASI]` |
| Backend | Tidak ada | Ditetapkan |
| Arsitektur | Mengikuti struktur repo acuan; jangan dirombak | Ditetapkan |
| Version control | Git, repository sendiri; commit tiap tahap; cantumkan repo asli sebagai referensi | Ditetapkan |

**Aturan teknis:** gunakan versi library yang dipakai repo acuan; cek dokumentasi resmi sebelum memperbarui versi apa pun.

## 12. Rencana dan Milestone

| Tahap | Pekerjaan | Keluaran yang dapat diuji |
|---|---|---|
| 1 | Clone repo acuan, build, jalankan; verifikasi isi kode | Aplikasi acuan berjalan di emulator; daftar hal terverifikasi |
| 2 | Pelajari struktur kode dan alur data | Ringkasan struktur kode |
| 3 | Sesuaikan teks dan tampilan ke Bahasa Indonesia | Semua teks berbahasa Indonesia |
| 4 | Uji dan rapikan CRUD, urut, filter, selesai | Semua fungsi inti berjalan |
| 5 | Pastikan data permanen; rapikan validasi dan kondisi kosong | Data tetap ada setelah aplikasi ditutup |
| 6 | Uji akhir dan penyusunan laporan | Kriteria selesai terpenuhi |

Tenggat dan pembagian peran: *diisi pemilik proyek*.

## 13. Risiko dan Dependensi

| Risiko / dependensi | Dampak | Mitigasi |
|---|---|---|
| Source code repo acuan belum diverifikasi (penyimpanan, field, relasi, status selesai Ujian) | PRD dan ERD bisa tidak akurat | Selesaikan Tahap 1-2 dan perbarui dokumen ini |
| Lisensi repo acuan tidak terlihat saat dicek | Risiko penggunaan ulang kode | Periksa lisensi; cantumkan repo asli sebagai sumber acuan di laporan |
| Kemiripan dengan repo acuan | Isu orisinalitas tugas | Cantumkan sumber secara jelas; pastikan syarat dosen mengizinkan |
| Syarat dosen (UML, ERD, format laporan) belum diketahui | Dokumen bisa tidak sesuai | Isi dari pemilik proyek |
| Data hilang jika aplikasi dihapus | Pengguna kehilangan data | Diterima di versi ini; backup/ekspor di pengembangan lanjutan |

## 14. Pertanyaan Terbuka

1. Mata kuliah pada item: teks bebas atau foreign key ke Kelas?
2. Penyimpanan data di repo acuan: Room, SQLite, atau lainnya?
3. UI memakai XML atau Compose?
4. Apakah Ujian mendukung status selesai?
5. Nilai `minSdk` dan `targetSdk`, serta versi Android Gradle Plugin dan Gradle.
6. Nama aplikasi: tetap "College Scheduler" atau nama sendiri?
7. Syarat dosen: UML apa saja, ERD, dan format laporan?
8. Mata kuliah, tenggat, dan anggota tim.

## 15. Riwayat Dokumen

| Versi | Tanggal | Perubahan |
|---|---|---|
| 0.1 | 31 Okt 2026 | Draft awal dari `brief-college-scheduler.md` |
