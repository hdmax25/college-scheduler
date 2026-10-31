# Brief Proyek: College Scheduler (Versi Ringkas)

> **Pembaca**: pemilik proyek dan LLM (penyusun dokumen atau AI coding assistant).
> **Bahasa**: Indonesia; istilah teknis tetap Inggris.
> **Tanda `[ASUMSI]`**: keputusan default yang belum dikonfirmasi. Ubah jika tidak sesuai.
> **Tanda `[BELUM TERVERIFIKASI]`**: hal yang tidak disebut jelas di README repo acuan dan harus dicek di source code sebelum ditulis sebagai fakta.

---

## 0. Instruksi untuk LLM

- Aplikasi ini adalah **aplikasi Android native** yang dirancang **mengikuti repository acuan [AlexT101/college-scheduler](https://github.com/AlexT101/college-scheduler)** (Java, Android Studio, Gradle Kotlin DSL). Isi brief ini sengaja dibatasi pada fitur yang tercantum di README repo tersebut. Jangan menambahkan fitur dari Bagian 10 (Batasan) kecuali diminta.
- Aplikasi **tidak memakai backend, server, login, maupun Firebase**. Semua data tersimpan lokal di perangkat.
- Jika dipakai untuk **menyusun dokumen laporan**: gunakan hanya data di brief ini. Jika ada bagian yang diminta tugas tetapi datanya tidak ada atau bertanda `[BELUM TERVERIFIKASI]` / `[ASUMSI]`, **jangan mengarang**. Tanyakan ke pemilik proyek.
- Jika dipakai untuk **menulis kode**: kerjakan satu tahap per permintaan sesuai Bagian 14, dan setiap tahap harus bisa di-build dan dijalankan sebelum lanjut.
- **Jika ada yang membingungkan, jangan menebak.** Berhenti dan tanyakan ke pemilik proyek. Berlaku untuk: instruksi ambigu atau bertentangan, informasi yang belum ada (nama paket, versi library, cara penyimpanan data repo acuan), dan perubahan yang menyimpang dari brief.
- Format pertanyaan:
  1. **Masalah**: apa yang membingungkan dan di bagian brief mana.
  2. **Pilihan jawaban**: 2 sampai 4 opsi berlabel A, B, C.
  3. **Plus dan minus** setiap opsi (waktu pengerjaan, kerumitan, kemudahan dijelaskan saat presentasi).
  4. **Rekomendasi** beserta alasannya, sesuai tujuan proyek tugas yang sederhana.
  5. Tunggu jawaban sebelum melanjutkan.
- Jika pertanyaannya kecil dan jawabannya jelas dari brief, tidak perlu bertanya.
- Hal yang hanya bisa dikerjakan pemilik proyek (menjalankan di perangkat atau emulator, memeriksa source code repo acuan) ada di Bagian 12. Jangan berpura-pura sudah mengerjakannya.
- Bagian yang berupa diagram (flowchart, use case, ERD, dan sebagainya) cukup diberi ruang kosong; pemilik proyek mengisi gambarnya sendiri.

---

## 1. Informasi Proyek

| Item | Isi |
|---|---|
| Nama aplikasi | **College Scheduler** `[ASUMSI]` (sama dengan nama repo acuan; ganti jika ingin nama sendiri) |
| Jenis proyek | Tugas (mata kuliah: *diisi pemilik proyek*) |
| Tenggat / anggota tim | *diisi pemilik proyek* |
| Syarat dosen (UML, ERD, format laporan) | *diisi pemilik proyek* |
| Platform | Android native |
| Bahasa pemrograman | Java (sesuai README repo acuan) |
| Build system | Gradle Kotlin DSL (terlihat dari file `build.gradle.kts` di repo acuan) |
| `minSdk` | *mengikuti repo acuan* `[BELUM TERVERIFIKASI]` |
| Bahasa aplikasi | Indonesia `[ASUMSI]` (repo asli berbahasa Inggris) |
| Repository acuan | https://github.com/AlexT101/college-scheduler (Android, Java; dibuat untuk kelas Georgia Tech CS2340; 74 commit) |

## 2. Latar Belakang dan Masalah

Mahasiswa harus mengingat banyak hal sekaligus: jadwal kuliah, tugas, ujian, dan pekerjaan lain dengan tenggat berbeda. Informasi tersebut biasanya tersebar di grup chat, LMS, dan catatan pribadi, sehingga tenggat mudah terlewat dan sulit menentukan mana yang harus dikerjakan lebih dulu.

**Rumusan masalah:**
1. Bagaimana menyimpan jadwal kuliah, tugas, dan ujian dalam satu aplikasi?
2. Bagaimana mahasiswa dapat melihat dan mengurutkan item berdasarkan tenggat, nama, atau mata kuliah?
3. Bagaimana mahasiswa dapat menandai dan memantau item yang sudah dan belum selesai?

## 3. Solusi dan Tujuan

**Solusi:** College Scheduler adalah aplikasi Android untuk mengelola jadwal akademik. Pengguna mencatat **Kelas**, **Tugas (assignment)**, **Ujian (exam)**, dan **Pekerjaan lain (task)**. Item dapat diurutkan, difilter berdasarkan status selesai, dan ditandai selesai. Halaman ringkasan menampilkan seluruh tugas, ujian, dan pekerjaan dalam satu daftar to-do.

**Tujuan:**
- Pengguna dapat menambah, melihat, mengubah, dan menghapus kelas, tugas, ujian, dan pekerjaan.
- Pengguna dapat mengurutkan item berdasarkan nama, tenggat, atau mata kuliah.
- Pengguna dapat menandai item selesai dan menyaring item selesai atau belum selesai.
- Pengguna dapat melihat semua tugas, ujian, dan pekerjaan dalam satu halaman ringkasan.

## 4. Ruang Lingkup

Empat kelompok fitur (semuanya bersumber dari README repo acuan): **(1) pengelolaan Kelas, (2) pengelolaan Tugas, Ujian, dan Pekerjaan, (3) pengurutan, filter, dan penanda selesai, (4) halaman ringkasan.** Tidak ada login dan tidak ada notifikasi pada versi ini (lihat Bagian 10).

---

## 5. Istilah

- **Kelas**: satu mata kuliah yang dijadwalkan (nama, hari dan jam, ruang, seksi, dosen).
- **Tugas (Assignment)**: pekerjaan dengan nama, tenggat tanggal dan jam, dan mata kuliah.
- **Ujian (Exam)**: ujian dengan nama, tanggal dan jam, mata kuliah, dan lokasi.
- **Pekerjaan (Task)**: pekerjaan lain dengan nama, tenggat tanggal dan jam, dan mata kuliah.
- **Item**: sebutan umum untuk Tugas, Ujian, dan Pekerjaan.
- **Ringkasan (Overview)**: halaman yang menampilkan semua Tugas, Ujian, dan Pekerjaan dalam satu daftar.

## 6. Kebutuhan Fungsional

*(Semua fungsi di bagian ini tercantum di README repo acuan, kecuali yang diberi tanda lain.)*

### FR-1 Kelas
- Menambah, melihat, mengubah, dan menghapus Kelas.
- Data Kelas: nama kelas, waktu dan hari dalam seminggu, ruang dan lokasi, seksi, dosen.
- Daftar Kelas ditampilkan di halaman tersendiri.

### FR-2 Tugas, Ujian, dan Pekerjaan
- Menambah, melihat, mengubah, dan menghapus ketiga jenis item.
- Data **Tugas**: nama, tenggat (tanggal dan jam), mata kuliah.
- Data **Ujian**: nama, tanggal dan jam, mata kuliah, lokasi.
- Data **Pekerjaan**: nama, tenggat (tanggal dan jam), mata kuliah.
- Tugas dan Ujian masing-masing punya halaman tersendiri.

### FR-3 Pengurutan
- Item dapat diurutkan berdasarkan **nama**, **tenggat**, atau **mata kuliah**.

### FR-4 Status selesai dan filter
- Item dapat **ditandai selesai**.
- Item dapat **difilter** berdasarkan status: selesai atau belum selesai.

### FR-5 Halaman Ringkasan
- Satu halaman yang memuat seluruh Tugas, Ujian, dan Pekerjaan sebagai satu daftar to-do.

### FR-6 Penyimpanan data (lokal)
- Data tersimpan di perangkat dan tidak hilang saat aplikasi ditutup.
- Cara penyimpanan di repo acuan (Room, SQLite, atau lainnya) **[BELUM TERVERIFIKASI]**; wajib dicek di source code (Bagian 12) lalu ditulis di Bagian 7 dan Bagian 15.

---

## 7. Data

### 7.1 Entitas dan atribut

*(Atribut diambil dari README repo acuan. Nama field, tipe data, dan primary key adalah rancangan `[ASUMSI]` sampai source code dicek.)*

**Kelas (Class)**

| Field | Tipe `[ASUMSI]` | Keterangan |
|---|---|---|
| `id` | Integer (PK) | Pengenal unik |
| `name` | String | Nama kelas |
| `days` | String/daftar hari | Hari kuliah dalam seminggu |
| `startTime`, `endTime` | Waktu | Jam kuliah |
| `room` | String | Ruang dan lokasi |
| `section` | String | Seksi |
| `professor` | String | Nama dosen |

**Tugas (Assignment)**

| Field | Tipe `[ASUMSI]` | Keterangan |
|---|---|---|
| `id` | Integer (PK) | Pengenal unik |
| `name` | String | Nama tugas |
| `dueDateTime` | Tanggal dan jam | Tenggat |
| `course` | String | Mata kuliah |
| `isCompleted` | Boolean | Status selesai |

**Ujian (Exam)**

| Field | Tipe `[ASUMSI]` | Keterangan |
|---|---|---|
| `id` | Integer (PK) | Pengenal unik |
| `name` | String | Nama ujian |
| `dateTime` | Tanggal dan jam | Waktu ujian |
| `course` | String | Mata kuliah |
| `location` | String | Lokasi ujian |
| `isCompleted` | Boolean | Status selesai |

**Pekerjaan (Task)**

| Field | Tipe `[ASUMSI]` | Keterangan |
|---|---|---|
| `id` | Integer (PK) | Pengenal unik |
| `name` | String | Nama pekerjaan |
| `dueDateTime` | Tanggal dan jam | Tenggat |
| `course` | String | Mata kuliah |
| `isCompleted` | Boolean | Status selesai |

### 7.2 Relasi
- Tugas, Ujian, dan Pekerjaan memiliki field **mata kuliah**. Apakah field itu hanya teks bebas atau berelasi (foreign key) ke tabel Kelas **[BELUM TERVERIFIKASI]**. Sampai dicek, tulis relasinya sebagai "terhubung melalui nama mata kuliah" dan jangan menggambar foreign key.
- Status selesai pada Ujian: README hanya menyebut "mark items as complete" untuk item secara umum, sehingga berlaku untuk Ujian **[BELUM TERVERIFIKASI]**.

### 7.3 Penyimpanan
- Seluruh data disimpan lokal di perangkat; tidak ada cloud, server, atau akun.
- Konsekuensi: data tidak tersinkron antar perangkat dan hilang jika aplikasi dihapus.

---

## 8. Aturan Validasi `[ASUMSI]`

*(Repo acuan tidak menjelaskan aturan validasi; aturan berikut adalah usulan untuk versi tugas.)*

| Field | Aturan |
|---|---|
| Nama (kelas, tugas, ujian, pekerjaan) | Wajib, tidak boleh kosong setelah `trim` |
| Mata kuliah (pada item) | Wajib |
| Tenggat / waktu | Tanggal dan jam harus terisi lengkap |
| Hari dan jam kelas | Jam selesai harus setelah jam mulai |
| Ruang, seksi, dosen, lokasi ujian | Opsional |

Pesan error ditampilkan dalam bahasa Indonesia di dekat kolom yang salah.

---

## 9. Alur Utama (untuk diagram)

*(Bagian ini disediakan agar dokumen dapat memuat flowchart, use case, atau activity diagram. Gambarnya diisi sendiri oleh pemilik proyek.)*

1. **Menambah item**: pengguna membuka halaman jenis item, menekan tombol tambah, mengisi data, lalu menyimpan. Jika data tidak valid, tampil pesan error. Jika valid, item tersimpan dan muncul di daftar.
2. **Mengubah / menghapus item**: pengguna memilih item, mengubah data lalu menyimpan, atau menghapus item.
3. **Menandai selesai**: pengguna menandai item selesai; status tersimpan.
4. **Mengurutkan dan memfilter**: pengguna memilih kriteria urut (nama, tenggat, mata kuliah) dan filter (selesai atau belum); daftar diperbarui.
5. **Melihat ringkasan**: pengguna membuka halaman Ringkasan untuk melihat semua tugas, ujian, dan pekerjaan.

**Aktor tunggal:** Pengguna (mahasiswa). Tidak ada login dan tidak ada aktor lain.

---

## 10. Batasan Versi Ini

- Tidak ada **notifikasi** atau pengingat, karena fitur ini tidak tercantum di README repo acuan.
- Tidak ada login, akun, backend, Firebase, atau sinkronisasi antar perangkat.
- Data hanya ada di satu perangkat dan hilang jika aplikasi dihapus.
- Tidak ada kategori, label, atau prioritas.
- Tidak ada berbagi atau ekspor data.
- Tidak ada pengulangan jadwal otomatis atau integrasi dengan kalender perangkat atau LMS.
- Tidak ada widget layar utama.
- Tidak ada pencarian teks; pengguna memakai pengurutan dan filter.

## 11. Pengembangan Lanjutan

| Prioritas | Pengembangan |
|---|---|
| Tinggi | Notifikasi pengingat tenggat (misalnya sekitar 24 jam sebelum tenggat), dijadwalkan lokal di perangkat tanpa server |
| Sedang | Notifikasi jadwal kuliah (kuliah hari ini atau besok, menyebut mata kuliah dan dosen) |
| Sedang | Filter tambahan dan tampilan jadwal per hari atau per minggu |
| Rendah | Backup dan ekspor data, widget, sinkronisasi antar perangkat |

---

## 12. Hal yang Dikerjakan Pemilik Proyek

1. Clone repo acuan lalu buka di Android Studio, tunggu Gradle sync, dan jalankan di emulator atau perangkat. Catat versi Android Gradle Plugin, Gradle, `minSdk`, dan `targetSdk` yang dipakai.
2. Periksa source code untuk memverifikasi semua butir `[BELUM TERVERIFIKASI]`: cara penyimpanan data, nama dan tipe field, relasi mata kuliah, status selesai pada Ujian, daftar Activity atau Fragment, serta library yang dipakai.
3. Perbarui Bagian 1, 7, 15 dengan hasil pemeriksaan, lalu hapus tanda `[BELUM TERVERIFIKASI]` yang sudah terjawab.
4. Isi data yang ditandai *diisi pemilik proyek* di Bagian 1.
5. Perhatikan **lisensi** repo acuan (tidak terlihat file lisensi saat dicek) dan cantumkan repo asli sebagai sumber acuan di laporan.
6. Siapkan gambar diagram sendiri bila diminta tugas.

---

## 13. Layar dan Kondisi Khusus

### 13.1 Daftar layar `[ASUMSI: nama layar mengikuti deskripsi README, belum diverifikasi di kode]`

| No | Layar | Isi utama |
|---|---|---|
| 1 | Ringkasan (Overview) | Daftar gabungan Tugas, Ujian, dan Pekerjaan |
| 2 | Kelas | Daftar Kelas, tombol tambah |
| 3 | Tugas | Daftar Tugas, urut, filter, tandai selesai |
| 4 | Ujian | Daftar Ujian |
| 5 | Form Kelas | Nama, hari dan jam, ruang, seksi, dosen |
| 6 | Form Item | Nama, tenggat atau waktu, mata kuliah (lokasi untuk ujian) |

### 13.2 Kondisi kosong dan error

| Situasi | Perilaku |
|---|---|
| Daftar kosong | Teks "Belum ada data" dan tombol tambah |
| Input tidak valid | Pesan error di bawah kolom yang salah |
| Hapus item | Dialog konfirmasi sebelum dihapus |
| Hasil filter kosong | Teks "Tidak ada item yang cocok" |

---

## 14. Rencana Pengerjaan dan Kriteria Selesai

### 14.1 Tahap

| Tahap | Pekerjaan | Keluaran yang dapat diuji |
|---|---|---|
| 1 | Clone repo acuan, build, dan jalankan; verifikasi isi kode | Aplikasi acuan berjalan di emulator; daftar hal terverifikasi |
| 2 | Pelajari struktur kode dan alur data (model, adapter, penyimpanan) | Ringkasan struktur kode |
| 3 | Sesuaikan teks dan tampilan ke bahasa Indonesia | Semua teks berbahasa Indonesia |
| 4 | Uji dan rapikan fitur Kelas, Tugas, Ujian, Pekerjaan (CRUD, urut, filter, selesai) | Semua fungsi inti berjalan |
| 5 | Pastikan data tersimpan permanen dan rapikan validasi serta kondisi kosong | Data tetap ada setelah aplikasi ditutup |
| 6 | Uji akhir dan penyusunan laporan | Kriteria selesai terpenuhi |

### 14.2 Kriteria selesai
- [ ] Kelas, Tugas, Ujian, dan Pekerjaan dapat ditambah, dilihat, diubah, dan dihapus.
- [ ] Item dapat diurutkan berdasarkan nama, tenggat, dan mata kuliah.
- [ ] Item dapat ditandai selesai dan difilter (selesai atau belum).
- [ ] Halaman Ringkasan menampilkan semua tugas, ujian, dan pekerjaan.
- [ ] Data tidak hilang setelah aplikasi ditutup dan dibuka lagi.
- [ ] Input yang tidak valid menampilkan pesan error yang jelas.

---

## 15. Aturan Teknis

| Bagian | Pilihan |
|---|---|
| Bahasa dan UI | Java, layout Android bawaan (mengikuti repo acuan) `[BELUM TERVERIFIKASI: XML atau Compose]` |
| IDE dan build | Android Studio, Gradle Kotlin DSL |
| Penyimpanan data | Lokal di perangkat `[BELUM TERVERIFIKASI: Room / SQLite / lainnya]` |
| Backend | Tidak ada |
| Arsitektur | Mengikuti struktur repo acuan; jangan dirombak |

Aturan:
- **Versi library**: pakai versi yang dipakai repo acuan; cek dokumentasi resmi sebelum memperbarui dan jangan mengandalkan ingatan untuk nomor versi.
- Semua teks di `strings.xml`.
- **Git**: inisialisasi repository sendiri, commit tiap tahap selesai, dan cantumkan repo asli sebagai referensi.
- Kerjakan hanya tahap yang diminta; jangan merombak tahap sebelumnya tanpa memberi tahu.
