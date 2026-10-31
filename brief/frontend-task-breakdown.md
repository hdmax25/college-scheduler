# College Scheduler: Rencana Task Front End

> Dokumen ini memecah pengerjaan **front end saja** menjadi task kecil yang bisa dikerjakan satu per satu bersama AI coding assistant. Front end harus selesai dan lolos uji sebelum back end dikerjakan.
> Sumber: `brief-college-scheduler.md` dan `PRD-college-scheduler.md` (v0.1). Jika keduanya bertentangan, tanyakan ke pemilik proyek; jangan memilih sendiri. Bahasa: Indonesia; istilah teknis tetap Inggris.

---

## 0. Cara Memakai Dokumen Ini

### 0.1 Batas front end dan back end

Brief menyatakan aplikasi tidak memakai server, login, atau Firebase. Maka pembagian di dokumen ini adalah sebagai berikut. `[ASUMSI]` Ubah jika pembagian di kelasmu berbeda.

| | Front end (dokumen ini) | Back end (dokumen terpisah, nanti) |
|---|---|---|
| Isi | Layout XML, Activity/Fragment, adapter, navigasi, dialog, date/time picker, tampilan validasi, empty state, teks Indonesia | Penyimpanan lokal permanen (Room/SQLite/lainnya), operasi simpan/ubah/hapus ke database, logika urut dan filter di level data, persistensi status selesai |
| Data selama front end | **Data dummy di memori** lewat satu kelas penghubung (lihat FE-03) | Mengganti data dummy dengan penyimpanan sungguhan |

Kunci agar back end mudah dipasang: semua layar front end hanya berbicara ke **satu kelas repository sementara** (in-memory). Saat back end dikerjakan, isi kelas itu diganti tanpa mengubah layar.

### 0.2 Aturan umum untuk semua task

- Kerjakan **satu task per sesi AI**. Jangan lanjut sebelum build sukses dan testing steps lolos.
- Nama file, nama paket, dan struktur folder di dokumen ini adalah **usulan**. Karena brief mewajibkan mengikuti struktur repo acuan, sesuaikan dengan hasil verifikasi di FE-00.
- Semua teks tampilan masuk ke `strings.xml`, tidak ditulis langsung di layout atau kode.
- Hal bertanda `[BELUM TERVERIFIKASI]` di brief (cara penyimpanan, nama field, relasi mata kuliah, XML atau Compose, daftar Activity/Fragment) **tidak boleh ditebak**. AI harus bertanya dulu.
- Diagram (use case, flowchart, ERD) tidak dibuat AI; kamu isi sendiri.
- Jangan menambah fitur yang masuk daftar Batasan Versi Ini (brief Bagian 10, PRD Bagian 6.2): notifikasi, login, kategori/prioritas, ekspor, widget, pencarian teks, integrasi kalender/LMS.
- **Offline-first** (PRD Bagian 10, BR-1): tidak ada permission `INTERNET`, library jaringan, atau Firebase.
- **Sederhana dan mudah dijelaskan** (PRD Bagian 10): struktur kode tidak dirombak dari acuan, tidak ada lapisan abstraksi di luar satu repository sementara.
- **Versi library**: pakai versi yang dipakai repo acuan; cek dokumentasi resmi sebelum memperbarui, jangan mengandalkan ingatan untuk nomor versi.
- Jangan merombak task sebelumnya tanpa memberi tahu. Jika perlu mengubah hasil task lama, sebutkan file dan alasannya.
- Setiap task punya baris **Acuan PRD** di bagian Penjelasan (ID fitur, user story, aturan bisnis) supaya bisa ditelusuri ke dokumen sumber.

### 0.3 Prompt pembuka (tempel di awal setiap sesi AI)

```
Konteks: Saya mengerjakan tugas kuliah aplikasi Android native "College Scheduler"
(Java, Android Studio, Gradle Kotlin DSL) yang mengikuti repository acuan
AlexT101/college-scheduler. Saya melampirkan brief proyek (brief-college-scheduler.md)
dan PRD (PRD-college-scheduler.md).
Kita hanya mengerjakan FRONT END; back end/penyimpanan permanen dikerjakan nanti.
Aturan: kerjakan hanya task yang saya minta, jangan menambah fitur di luar brief dan PRD (terutama yang ada di daftar
Batasan Versi Ini), jangan merombak struktur repo acuan, semua teks di strings.xml dan berbahasa
Indonesia. Jika ada yang membingungkan, brief dan PRD bertentangan, atau berstatus
[BELUM TERVERIFIKASI],
JANGAN menebak: berhenti dan tanyakan dengan format Masalah, Pilihan A/B/C,
plus-minus tiap opsi, Rekomendasi. Tunggu jawaban saya sebelum lanjut.
```

### 0.4 Daftar task

| Kode | Task | Acuan PRD / Tahap brief |
|---|---|---|
| FE-00 | Setup dan verifikasi repo acuan | Tahap 1-2; PRD Bagian 11, 13, 14 |
| FE-01 | Kerangka navigasi dan layar kosong | PRD 8.1 (layar 1-4) |
| FE-02 | Bahasa Indonesia, `strings.xml`, dan tema dasar | Tahap 3; BR-4; PRD 10 |
| FE-03 | Model UI dan repository data dummy | PRD 6.1 (atribut data); BR-3, BR-5 |
| FE-04 | Layar daftar Kelas | FR-1; US-1 |
| FE-05 | Form Kelas (tambah dan ubah) | FR-1; US-1; PRD 9.1 |
| FE-06 | Layar daftar Tugas | FR-2, FR-4; US-2, US-6 |
| FE-07 | Form Item (Tugas, Ujian, Pekerjaan) | FR-2; US-2, US-3, US-4; PRD 9.1 |
| FE-08 | Layar daftar Ujian | FR-2, FR-4; US-3; BR-5 |
| FE-09 | Pengurutan dan filter | FR-3, FR-4; US-5, US-7 |
| FE-10 | Hapus item dengan dialog konfirmasi | FR-1, FR-2; BR-2; PRD 8.2 |
| FE-11 | Layar Ringkasan | FR-5, FR-2; US-4, US-8; BR-2 |
| FE-12 | Rapikan kondisi kosong/error dan uji akhir front end | Tahap 4-6 sisi tampilan; PRD 8.2, 9.3, 9.4, 10 |

Task FE-03 sampai FE-11 bersama-sama mengisi Tahap 4 brief (sisi tampilan); FE-12 mencakup bagian tampilan Tahap 5 dan 6. Bagian penyimpanan permanen (FR-6, US-9, BR-3) dikerjakan di dokumen back end.

### 0.5 Alur git yang disarankan

Inisialisasi repository sendiri (bukan fork langsung dari repo acuan), cantumkan repo asli sebagai referensi di README. Gunakan branch `main` yang stabil, satu branch per task, merge ke `main` setelah testing steps lolos.

---

## FE-00 Setup dan Verifikasi Repo Acuan

### 1. Judul
Clone, build, jalankan repo acuan, dan catat hasil verifikasi.

### 2. Sub bab detail
- 2.1 Clone dan build di Android Studio
- 2.2 Catat versi tool
- 2.3 Verifikasi butir `[BELUM TERVERIFIKASI]` yang berkaitan dengan UI
- 2.4 Catat lisensi, cek syarat dosen soal kemiripan dengan repo acuan, dan inisialisasi repository sendiri

### 3. Penjelasan
**Apa yang dilakukan:** Menjalankan aplikasi acuan di emulator atau perangkat, lalu membaca source code untuk menjawab: UI memakai XML atau Compose, daftar Activity/Fragment apa saja, nama layout, library UI yang dipakai (RecyclerView, Material, dan sebagainya), dan nama paket. Catat juga versi Android Gradle Plugin, Gradle, `minSdk`, `targetSdk`.
**Supaya apa:** Semua task berikutnya bergantung pada fakta ini. Tanpa verifikasi, AI akan menebak dan hasilnya tidak cocok dengan struktur acuan.
**Acuan PRD:** Tahap 1-2 (Bagian 12), Bagian 11 (Teknologi), Bagian 13 (risiko lisensi dan orisinalitas), Bagian 14 (Pertanyaan Terbuka no. 1-5).

### 4. Prompt AI
Tugas ini sebagian besar dikerjakan olehmu sendiri (Bagian 12 brief). Prompt hanya untuk membantu membaca kode:

```
[Tempel prompt pembuka]
Task FE-00. Saya melampirkan struktur folder dan isi file utama repo acuan
(AndroidManifest.xml, build.gradle.kts, daftar Activity/Fragment, folder layout).
Tolong rangkum dalam tabel: (1) UI memakai XML atau Compose, (2) daftar
Activity/Fragment dan fungsinya, (3) library UI dan versinya, (4) nama paket,
(5) bagaimana navigasi antar layar dilakukan, (6) di mana data dikelola.
Hanya rangkum yang benar-benar terlihat di file yang saya lampirkan; tandai
"tidak terlihat" untuk sisanya. Jangan menebak.
```

### 5. File yang dibuat
- `docs/verifikasi-repo-acuan.md`: catatan hasil verifikasi (tabel versi tool, daftar layar, library, status tiap butir `[BELUM TERVERIFIKASI]`). Isi dengan hasil dari AI yang sudah kamu cek sendiri.
- `.gitignore` dan `README.md` repository sendiri (jika belum ada), README mencantumkan repo acuan sebagai sumber.

### 6. File yang diubah
- Tidak ada file kode yang diubah.
- `brief-college-scheduler.md`: perbarui Bagian 1 dan 15, hapus tanda `[BELUM TERVERIFIKASI]` yang sudah terjawab.
- `PRD-college-scheduler.md`: perbarui Bagian 6.1 (atribut dan relasi), 9.2 (BR-5), 11, dan 14; catat perubahan di Bagian 15 (Riwayat Dokumen).

### 7. Testing steps
1. Gradle sync selesai tanpa error.
2. Aplikasi acuan terpasang dan terbuka di emulator.
3. Semua layar acuan bisa dibuka dan kamu tahu nama layar tiap-tiapnya.
4. `docs/verifikasi-repo-acuan.md` terisi dan tidak ada butir yang diisi dari tebakan.
5. Pertanyaan Terbuka no. 1-5 di PRD Bagian 14 sudah terjawab atau tercatat belum terjawab, dan brief serta PRD sudah diperbarui.

### 8. Branch dan commit message
- Branch: `chore/fe-00-setup-verifikasi`
- Commit: `chore: initial project setup from reference repo`
- Commit: `docs: add reference repo verification notes`

---

## FE-01 Kerangka Navigasi dan Layar Kosong

### 1. Judul
Navigasi utama dan layar kosong untuk 4 layar inti.

### 2. Sub bab detail
- 2.1 Menentukan pola navigasi (mengikuti acuan)
- 2.2 Membuat layar kosong: Ringkasan, Kelas, Tugas, Ujian
- 2.3 Perpindahan antar layar

### 3. Penjelasan
**Apa yang dilakukan:** Membuat rangka aplikasi: satu Activity utama dengan navigasi (misalnya bottom navigation) yang berpindah antara empat layar. Tiap layar hanya berisi judul sementara.
**Supaya apa:** Semua task berikutnya tinggal mengisi layar yang sudah punya tempat dan jalur navigasi, sehingga tidak ada rombak struktur di tengah jalan. Pola navigasi mengikuti hasil FE-00; jika acuan memakai pola lain, ikuti acuan.
**Acuan PRD:** Bagian 8.1 (layar 1-4: Ringkasan, Kelas, Tugas, Ujian).

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-01: kerangka navigasi. Berdasarkan catatan verifikasi repo acuan
(terlampir), buat navigasi utama dengan 4 layar kosong: Ringkasan, Kelas, Tugas,
Ujian, mengikuti pola navigasi yang sudah dipakai repo acuan. Tiap layar cukup
menampilkan judul. Jangan membuat isi daftar atau form. Jelaskan file apa yang
dibuat dan diubah, lalu beri langkah uji. Jika pola navigasi acuan tidak jelas,
tanyakan dulu dengan format Masalah/Pilihan/Plus-Minus/Rekomendasi.
```

### 5. File yang dibuat `[nama menyesuaikan acuan]`
- `OverviewFragment.java` dan `fragment_overview.xml`: layar Ringkasan kosong dengan judul.
- `ClassesFragment.java` dan `fragment_classes.xml`: layar Kelas kosong.
- `AssignmentsFragment.java` dan `fragment_assignments.xml`: layar Tugas kosong.
- `ExamsFragment.java` dan `fragment_exams.xml`: layar Ujian kosong.
- `res/menu/bottom_nav_menu.xml`: empat item menu dengan ikon.
- `res/navigation/nav_graph.xml` (jika acuan memakai Navigation component).

### 6. File yang diubah
- `MainActivity.java` dan `activity_main.xml`: menampung container layar dan komponen navigasi, menetapkan layar awal (Ringkasan).
- `AndroidManifest.xml`: hanya jika Activity baru ditambahkan.

### 7. Testing steps
1. Build sukses dan aplikasi terbuka ke layar Ringkasan.
2. Ketuk tiap item navigasi: empat layar bergantian tanpa crash.
3. Putar layar (rotate): tidak crash dan posisi layar tetap.
4. Tombol Back tidak menutup aplikasi secara tak terduga.

### 8. Branch dan commit message
- Branch: `feature/fe-01-navigasi`
- Commit: `feat(ui): add main navigation with four empty screens`

---

## FE-02 Bahasa Indonesia, strings.xml, dan Tema Dasar

### 1. Judul
Lokalisasi teks ke Bahasa Indonesia dan tema dasar.

### 2. Sub bab detail
- 2.1 Inventaris teks bawaan acuan yang masih Inggris
- 2.2 Memindahkan semua teks ke `strings.xml`
- 2.3 Warna, gaya, dan ikon dasar

### 3. Penjelasan
**Apa yang dilakukan:** Mengganti semua teks tampilan menjadi Bahasa Indonesia dan memastikan tidak ada teks yang tertulis langsung di layout atau kode. Menetapkan warna dan gaya dasar sehingga layar berikutnya konsisten.
**Supaya apa:** Menghindari teks Inggris tersisa dan mencegah penggantian teks berulang di setiap task. Aturan brief: semua teks di `strings.xml`.
**Acuan PRD:** BR-4 (antarmuka Indonesia, semua teks di `strings.xml`), Bagian 5 (istilah baku), Bagian 10 (lokalisasi), Bagian 14 no. 6 (nama aplikasi).

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-02: lokalisasi. Terlampir semua file layout dan strings.xml saat ini.
Ubah semua teks tampilan ke Bahasa Indonesia, pindahkan teks yang tertulis
langsung di layout atau Java ke strings.xml dengan nama resource yang konsisten
(awalan per layar, misalnya classes_title, exam_empty). Tetapkan warna dan style
dasar di colors.xml dan themes.xml. Jangan mengubah logika. Berikan daftar teks
yang diganti dan file yang tersentuh. Istilah yang dipakai: Kelas, Tugas, Ujian,
Pekerjaan, Ringkasan, tenggat, mata kuliah (sesuai Bagian 5 PRD). Nilai app_name:
"College Scheduler" kecuali saya putuskan lain.
```

### 5. File yang dibuat
- Tidak ada file wajib. Opsional: `res/values/dimens.xml` bila belum ada.

### 6. File yang diubah
- `res/values/strings.xml`: berisi seluruh teks Indonesia (judul layar, label form, tombol, pesan error, teks kosong, teks dialog) dan `app_name` (sementara "College Scheduler" `[ASUMSI]`, ikuti keputusan PRD Bagian 14 no. 6).
- `res/values/colors.xml` dan `res/values/themes.xml`: warna utama dan gaya komponen.
- Semua `layout/*.xml` dan file Java yang punya teks tertulis langsung: ganti dengan referensi `@string/...` atau `getString(...)`.
- `res/menu/bottom_nav_menu.xml`: label menu Indonesia.

### 7. Testing steps
1. Jalankan aplikasi: semua layar berbahasa Indonesia.
2. Cari teks Inggris dengan fitur *Find in Files* (misalnya kata "Add", "Delete", "Class"): tidak ada yang tersisa di layout/kode.
3. Jalankan *Lint* (Analyze > Inspect Code) dan pastikan tidak ada peringatan hardcoded text.
4. Tampilan tetap rapi pada layar kecil (uji emulator ukuran berbeda).
5. Nama aplikasi di launcher sesuai keputusan nama aplikasi.

### 8. Branch dan commit message
- Branch: `feature/fe-02-lokalisasi-tema`
- Commit: `feat(i18n): translate all UI text to Indonesian`
- Commit: `style: set base colors and theme`

---

## FE-03 Model UI dan Repository Data Dummy

### 1. Judul
Kelas model dan repository sementara di memori.

### 2. Sub bab detail
- 2.1 Kelas model: Kelas, Tugas, Ujian, Pekerjaan
- 2.2 Repository in-memory sebagai satu-satunya pintu data
- 2.3 Data contoh untuk pengembangan tampilan

### 3. Penjelasan
**Apa yang dilakukan:** Membuat kelas model sesuai Bagian 7 brief dan satu kelas repository yang menyimpan daftar di memori (ArrayList) dengan fungsi ambil, tambah, ubah, hapus, dan tandai selesai. Isi dengan beberapa data contoh.
**Supaya apa:** Layar bisa dikembangkan dan diuji sekarang tanpa database. Saat back end dikerjakan, hanya isi repository yang diganti.
**Catatan `[BELUM TERVERIFIKASI]`:** Nama field, tipe data, relasi mata kuliah (teks bebas atau foreign key), dan status selesai pada Ujian. Pakai hasil FE-00. Jika belum pasti, pakai rancangan `[ASUMSI]` Bagian 7 brief dan tulis relasi sebagai "terhubung melalui nama mata kuliah". Data hilang saat aplikasi ditutup; itu **memang disengaja** di tahap ini.
**Acuan PRD:** Bagian 6.1 (atribut data per jenis), BR-3 (status selesai bersifat per item), BR-5 (status selesai pada Ujian, `[BELUM TERVERIFIKASI]`). Atribut Ujian tetap memuat status selesai sampai FE-00 memastikan lain.

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-03: model dan repository sementara. Buat kelas model Java untuk Kelas,
Tugas, Ujian, Pekerjaan sesuai Bagian 7 brief (gunakan nama field hasil
verifikasi repo acuan yang saya lampirkan; jika berbeda dari brief, ikuti
verifikasi dan beri tahu saya). Buat satu kelas repository in-memory dengan
operasi: ambil semua, tambah, ubah, hapus, tandai selesai. Isi 3-4 data contoh
tiap jenis. Pisahkan repository ini di satu kelas agar nanti mudah diganti dengan
penyimpanan permanen. Jangan memakai Room/SQLite sekarang. Tanyakan dulu jika
relasi mata kuliah belum jelas.
```

### 5. File yang dibuat `[nama menyesuaikan acuan]`
- `model/CourseClass.java`: nama, hari, jam mulai/selesai, ruang, seksi, dosen.
- `model/Assignment.java`: nama, tenggat, mata kuliah, status selesai.
- `model/Exam.java`: nama, waktu, mata kuliah, lokasi, status selesai.
- `model/WorkTask.java`: nama, tenggat, mata kuliah, status selesai (nama kelas dipilih agar tidak bentrok dengan `Task` bawaan Android/Java).
- `data/InMemoryRepository.java`: penyimpan daftar di memori (pola singleton sederhana) berisi data contoh.

### 6. File yang diubah
- Tidak ada layar yang diubah. Opsional: `OverviewFragment.java` sementara menulis jumlah data ke Logcat untuk membuktikan repository bekerja.

### 7. Testing steps
1. Build sukses.
2. Tambahkan log sementara: jumlah data contoh tiap jenis sesuai yang dibuat.
3. Uji cepat tambah, ubah, hapus, tandai selesai lewat log atau unit test sederhana; hasilnya benar.
4. Hapus log sementara sebelum commit.

### 8. Branch dan commit message
- Branch: `feature/fe-03-model-repository-dummy`
- Commit: `feat(model): add Class, Assignment, Exam, Task models`
- Commit: `feat(data): add in-memory repository with sample data`

---

## FE-04 Layar Daftar Kelas

### 1. Judul
Daftar Kelas dengan kondisi kosong.

### 2. Sub bab detail
- 2.1 Daftar (RecyclerView) dan tampilan satu baris kelas
- 2.2 Teks kondisi kosong dan tombol tambah
- 2.3 Menghubungkan ke repository

### 3. Penjelasan
**Apa yang dilakukan:** Menampilkan semua Kelas dari repository dalam daftar. Tiap baris menampilkan nama, hari dan jam, ruang, dosen. Jika kosong, tampil "Belum ada data" dan tombol tambah. Tombol tambah mengarah ke Form Kelas (dibuat di FE-05; sementara cukup tombolnya terlihat).
**Supaya apa:** Memenuhi FR-1 sisi tampilan dan menjadi pola daftar yang dipakai ulang di layar Tugas dan Ujian.
**Acuan PRD:** FR-1, US-1, PRD 8.2 (daftar kosong).

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-04: layar daftar Kelas. Isi ClassesFragment dengan RecyclerView yang
membaca dari InMemoryRepository. Buat layout satu baris (nama, hari dan jam,
ruang, dosen). Saat daftar kosong tampilkan teks "Belum ada data" dan tombol
tambah. Sediakan tombol tambah (FAB) yang belum mengarah ke mana-mana. Ikuti pola
adapter yang dipakai repo acuan. Jangan membuat form. Jelaskan file yang dibuat
dan diubah, dan langkah uji.
```

### 5. File yang dibuat
- `adapter/ClassAdapter.java`: mengisi baris dari daftar Kelas dan menyediakan callback klik.
- `res/layout/item_class.xml`: tampilan satu baris Kelas.

### 6. File yang diubah
- `ClassesFragment.java`: memasang RecyclerView, adapter, mengambil data dari repository, mengatur tampil/sembunyi teks kosong.
- `fragment_classes.xml`: RecyclerView, teks kosong, FAB tambah.
- `strings.xml`: teks kosong dan label baris.

### 7. Testing steps
1. Buka layar Kelas: data contoh tampil lengkap dan terbaca.
2. Kosongkan data contoh sementara: teks "Belum ada data" dan tombol tambah muncul.
3. Gulir daftar panjang (tambah 20 data contoh): halus dan tidak ada baris terpotong.
4. Rotate layar: daftar tetap tampil.

### 8. Branch dan commit message
- Branch: `feature/fe-04-daftar-kelas`
- Commit: `feat(ui): add class list screen with empty state`

---

## FE-05 Form Kelas (Tambah dan Ubah)

### 1. Judul
Form Kelas dengan validasi tampilan.

### 2. Sub bab detail
- 2.1 Kolom form dan pemilih hari/jam
- 2.2 Validasi dan pesan error di dekat kolom
- 2.3 Mode tambah dan mode ubah
- 2.4 Simpan ke repository dan kembali ke daftar

### 3. Penjelasan
**Apa yang dilakukan:** Membuat form untuk nama kelas, hari, jam mulai dan selesai, ruang, seksi, dosen. Validasi sesuai Bagian 8 brief `[ASUMSI]`: nama wajib (setelah `trim`), jam selesai harus setelah jam mulai, ruang/seksi/dosen opsional. Pesan error Indonesia tampil di bawah kolom yang salah. Form yang sama dipakai untuk tambah dan ubah.
**Supaya apa:** Memenuhi FR-1 (tambah dan ubah) dan aturan validasi.
**Acuan PRD:** FR-1, US-1, Bagian 9.1 (aturan validasi), acceptance criteria FR-1.
**Catatan:** Cara memilih hari (satu hari atau beberapa hari) belum disebut jelas di brief. Jika repo acuan tidak jelas, AI harus bertanya.

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-05: form Kelas. Buat layar form (tambah dan ubah dengan layar yang sama)
dengan kolom: nama, hari, jam mulai, jam selesai (pakai time picker), ruang,
seksi, dosen. Validasi: nama wajib setelah trim; jam selesai harus setelah jam
mulai; sisanya opsional. Pesan error berbahasa Indonesia tampil di bawah kolom
yang salah. Saat simpan valid, panggil repository lalu kembali ke daftar yang
langsung menampilkan perubahan. Hubungkan tombol tambah (FE-04) dan klik baris
untuk mode ubah. Jika cara memilih hari (satu atau banyak) tidak jelas dari
brief/acuan, tanyakan dulu dengan format pilihan A/B/C.
```

### 5. File yang dibuat
- `ClassFormActivity.java` (atau Fragment, ikuti acuan): logika form, validasi, mode tambah/ubah.
- `res/layout/activity_class_form.xml`: kolom input, tombol simpan.
- `util/ValidationUtils.java` (opsional): fungsi validasi teks wajib dan urutan waktu, dipakai ulang di FE-07.

### 6. File yang diubah
- `ClassesFragment.java`: tombol tambah membuka form; klik baris membuka form mode ubah; daftar dimuat ulang setelah kembali.
- `ClassAdapter.java`: meneruskan klik baris.
- `AndroidManifest.xml`: mendaftarkan Activity form (jika Activity).
- `strings.xml`: label form dan pesan error.

### 7. Testing steps
1. Tambah Kelas valid: muncul di daftar.
2. Simpan dengan nama kosong atau hanya spasi: error di bawah kolom nama, data tidak tersimpan.
3. Jam selesai sebelum jam mulai: error tampil.
4. Ubah Kelas: nilai lama terisi di form, setelah simpan daftar ikut berubah.
5. Tekan Back saat form terisi: tidak crash dan tidak menyimpan data setengah jadi.

### 8. Branch dan commit message
- Branch: `feature/fe-05-form-kelas`
- Commit: `feat(ui): add class form for create and edit`
- Commit: `feat(validation): validate class name and time order`

---

## FE-06 Layar Daftar Tugas

### 1. Judul
Daftar Tugas dengan penanda selesai.

### 2. Sub bab detail
- 2.1 Daftar dan baris Tugas (nama, tenggat, mata kuliah)
- 2.2 Penanda selesai (checkbox)
- 2.3 Kondisi kosong dan tombol tambah

### 3. Penjelasan
**Apa yang dilakukan:** Menampilkan Tugas dari repository. Tiap baris punya checkbox selesai; ketika dicentang, tampilan berubah (misalnya teks dicoret) dan status berubah di repository. Kondisi kosong sama seperti Kelas.
**Supaya apa:** Memenuhi FR-2 (tampilan) dan FR-4 (tandai selesai).
**Acuan PRD:** FR-2, FR-4, US-2, US-6. BR-3 (status selesai tersimpan permanen) baru terpenuhi setelah back end; di tahap ini status hanya bertahan selama aplikasi terbuka.

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-06: layar daftar Tugas. Isi AssignmentsFragment dengan RecyclerView dari
repository. Tiap baris: nama, tenggat (format tanggal dan jam Indonesia), mata
kuliah, dan checkbox selesai. Item selesai tampil berbeda (misalnya dicoret dan
lebih pudar). Centang menyimpan status lewat repository. Tampilkan "Belum ada
data" dan tombol tambah saat kosong. Pakai ulang pola adapter dari ClassAdapter.
Belum membuat form, urut, atau filter.
```

### 5. File yang dibuat
- `adapter/AssignmentAdapter.java`: mengisi baris, menangani centang dan klik.
- `res/layout/item_assignment.xml`: baris Tugas dengan checkbox.
- `util/DateFormatUtils.java`: format tanggal dan jam Indonesia (dipakai ulang di layar lain).

### 6. File yang diubah
- `AssignmentsFragment.java` dan `fragment_assignments.xml`: RecyclerView, teks kosong, FAB tambah.
- `strings.xml`: teks baris dan teks kosong.

### 7. Testing steps
1. Data contoh Tugas tampil dengan format tanggal Indonesia.
2. Centang satu Tugas: tampilan berubah. Pindah ke layar lain lalu kembali: status tetap (selama aplikasi tidak ditutup).
3. Hilangkan centang: tampilan kembali normal.
4. Kosongkan data: teks kosong dan tombol tambah muncul.

### 8. Branch dan commit message
- Branch: `feature/fe-06-daftar-tugas`
- Commit: `feat(ui): add assignment list with completion checkbox`

---

## FE-07 Form Item (Tugas, Ujian, Pekerjaan)

### 1. Judul
Satu form untuk tiga jenis item dengan date dan time picker.

### 2. Sub bab detail
- 2.1 Penentuan jenis item pada form
- 2.2 Kolom dan picker tanggal/jam
- 2.3 Validasi dan pesan error
- 2.4 Mode tambah dan ubah

### 3. Penjelasan
**Apa yang dilakukan:** Membuat satu form untuk Tugas, Ujian, dan Pekerjaan, dengan kolom nama, tanggal, jam, mata kuliah, dan **lokasi hanya untuk Ujian**. Mata kuliah dipilih dari daftar Kelas atau diketik `[BELUM TERVERIFIKASI]`. Validasi sesuai Bagian 8: nama wajib, mata kuliah wajib, tanggal dan jam harus lengkap.
**Supaya apa:** Memenuhi FR-2 dan menghindari tiga form yang hampir sama.
**Acuan PRD:** FR-2, US-2, US-3, US-4, Bagian 9.1 (nama dan mata kuliah wajib, tanggal dan jam lengkap, lokasi Ujian opsional), acceptance criteria FR-2 (nama atau mata kuliah kosong: error dan item tidak tersimpan). US-4 menuntut CRUD Pekerjaan; karena Pekerjaan hanya tampil di Ringkasan, ubah dan hapus Pekerjaan dikerjakan di FE-11.
**Catatan `[ASUMSI]`:** Pekerjaan (Task) tidak punya halaman tersendiri menurut brief. Asumsi: Pekerjaan dibuat dari tombol tambah di Ringkasan (FE-11) dengan memilih jenis "Pekerjaan". Konfirmasi ke dirimu sendiri setelah memeriksa repo acuan.

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-07: form item. Buat satu form dengan parameter jenis (Tugas, Ujian,
Pekerjaan) dan mode (tambah atau ubah). Kolom: nama, tanggal (date picker), jam
(time picker), mata kuliah, dan lokasi hanya saat jenis Ujian. Validasi: nama
wajib setelah trim, mata kuliah wajib, tanggal dan jam harus terisi lengkap;
pesan error Indonesia di bawah kolom. Simpan lewat repository lalu kembali ke
layar asal. Hubungkan tombol tambah di layar Tugas dan klik baris untuk ubah.
Layar Ujian dan Ringkasan dihubungkan di task lain. Jika mata kuliah harus
dipilih dari daftar Kelas atau boleh teks bebas belum jelas, tanyakan dulu dengan
format pilihan A/B/C beserta plus-minus.
```

### 5. File yang dibuat
- `ItemFormActivity.java` (atau Fragment sesuai acuan): logika form untuk tiga jenis.
- `res/layout/activity_item_form.xml`: kolom input; kolom lokasi disembunyikan kecuali Ujian.
- `util/PickerUtils.java` (opsional): pembungkus date/time picker.

### 6. File yang diubah
- `AssignmentsFragment.java` dan `AssignmentAdapter.java`: tombol tambah dan klik baris membuka form.
- `ValidationUtils.java`: tambah validasi tanggal/jam lengkap dan mata kuliah wajib.
- `AndroidManifest.xml`: mendaftarkan Activity form.
- `strings.xml`: label dan pesan error.

### 7. Testing steps
1. Tambah Tugas valid: muncul di daftar dengan tenggat benar.
2. Kosongkan nama, mata kuliah, atau tanggal/jam satu per satu: error tepat di kolomnya.
3. Buka form jenis Ujian: kolom lokasi muncul; jenis Tugas: kolom lokasi tidak ada.
4. Ubah Tugas: nilai lama terisi, perubahan tampil di daftar.
5. Zona waktu dan format jam (24 jam) konsisten antara form dan daftar.

### 8. Branch dan commit message
- Branch: `feature/fe-07-form-item`
- Commit: `feat(ui): add item form for assignment, exam, and task`
- Commit: `feat(validation): validate name, course, and date/time`

---

## FE-08 Layar Daftar Ujian

### 1. Judul
Daftar Ujian.

### 2. Sub bab detail
- 2.1 Baris Ujian (nama, waktu, mata kuliah, lokasi)
- 2.2 Kondisi kosong dan tombol tambah
- 2.3 Penanda selesai pada Ujian `[BELUM TERVERIFIKASI]`

### 3. Penjelasan
**Apa yang dilakukan:** Menampilkan Ujian dengan pola yang sama seperti Tugas, ditambah lokasi. Penanda selesai pada Ujian mengikuti hasil verifikasi README/source (brief belum memastikan).
**Supaya apa:** Melengkapi FR-2 untuk Ujian dan memakai ulang pola yang sudah teruji di FE-06.
**Acuan PRD:** FR-2, US-3, BR-5 (`[BELUM TERVERIFIKASI]`: status selesai pada Ujian berlaku seperti item lain).

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-08: layar daftar Ujian. Isi ExamsFragment dengan pola yang sama seperti
layar Tugas: nama, waktu ujian (format Indonesia), mata kuliah, lokasi. Tombol
tambah dan klik baris memakai ItemFormActivity dengan jenis Ujian. Kondisi
kosong sama seperti layar lain. Untuk checkbox selesai pada Ujian, ikuti hasil
verifikasi repo acuan; jika belum diverifikasi, tanyakan dulu dengan format
pilihan A/B/C dan jangan menebak.
```

### 5. File yang dibuat
- `adapter/ExamAdapter.java`
- `res/layout/item_exam.xml`

### 6. File yang diubah
- `ExamsFragment.java` dan `fragment_exams.xml`: RecyclerView, teks kosong, FAB.
- `strings.xml`: teks baris dan teks kosong.

### 7. Testing steps
1. Data contoh Ujian tampil lengkap termasuk lokasi.
2. Tambah dan ubah Ujian lewat form: lokasi tersimpan dan tampil.
3. Kosongkan data: teks kosong dan tombol tambah muncul.
4. Perilaku checkbox selesai sesuai keputusan dari verifikasi.

### 8. Branch dan commit message
- Branch: `feature/fe-08-daftar-ujian`
- Commit: `feat(ui): add exam list screen`

---

## FE-09 Pengurutan dan Filter

### 1. Judul
Kontrol urut dan filter pada daftar.

### 2. Sub bab detail
- 2.1 Pilihan urut: nama, tenggat, mata kuliah
- 2.2 Filter: semua, selesai, belum selesai
- 2.3 Tampilan hasil filter kosong

### 3. Penjelasan
**Apa yang dilakukan:** Menambah kontrol (menu, spinner, atau chip) di layar Tugas dan Ujian untuk memilih kriteria urut dan filter status. Daftar langsung diperbarui. Jika hasil filter kosong, tampil "Tidak ada item yang cocok".
**Supaya apa:** Memenuhi FR-3 dan FR-4. Di tahap ini urut/filter dilakukan di sisi tampilan atas daftar dari repository; back end nanti boleh memindahkannya ke query.
**Acuan PRD:** FR-3, FR-4, US-5, US-7, acceptance criteria FR-3 dan FR-4 (hanya item sesuai status yang tampil; hasil kosong: "Tidak ada item yang cocok").
**Catatan:** (1) Jika FE-00 menunjukkan Ujian tidak punya status selesai (BR-5), filter status hanya dipasang di layar Tugas dan urut tetap berlaku di Ujian. (2) Pekerjaan hanya tampil di Ringkasan; apakah Ringkasan juga mendapat urut/filter belum disebut di brief maupun PRD. Putuskan dulu (Lampiran A) sebelum menambahkannya, jangan ditebak.

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-09: urut dan filter. Pada layar Tugas dan Ujian, tambahkan kontrol untuk
urut berdasarkan nama, tenggat (atau waktu untuk Ujian), dan mata kuliah, serta
filter Semua / Selesai / Belum selesai. Daftar langsung diperbarui saat pilihan
berubah dan pilihan bertahan selama layar terbuka. Jika hasil kosong tampilkan
"Tidak ada item yang cocok" (berbeda dari "Belum ada data"). Ikuti jenis kontrol
UI yang dipakai repo acuan; jika tidak jelas, tanyakan dulu dengan format pilihan
A/B/C. Urutan dan filter diterapkan di lapisan tampilan, bukan di model.
```

### 5. File yang dibuat
- `util/ItemSorter.java` (opsional): fungsi pembanding untuk nama, tenggat, mata kuliah.
- `res/menu/menu_sort_filter.xml` atau layout kontrol (tergantung jenis kontrol).

### 6. File yang diubah
- `AssignmentsFragment.java`, `ExamsFragment.java`: pasang kontrol, terapkan urut dan filter sebelum daftar dikirim ke adapter.
- `fragment_assignments.xml`, `fragment_exams.xml`: tempat kontrol.
- `AssignmentAdapter.java`, `ExamAdapter.java`: metode memperbarui daftar.
- `strings.xml`: label kriteria dan teks hasil filter kosong.

### 7. Testing steps
1. Siapkan data dengan nama, tenggat, dan mata kuliah beragam.
2. Urut nama: A ke Z benar. Urut tenggat: terdekat di atas. Urut mata kuliah: terkelompok benar.
3. Centang beberapa item, lalu filter Selesai dan Belum selesai: daftar sesuai.
4. Filter yang menghasilkan nol item: teks "Tidak ada item yang cocok" tampil.
5. Ubah status centang saat filter aktif: daftar menyesuaikan.

### 8. Branch dan commit message
- Branch: `feature/fe-09-urut-filter`
- Commit: `feat(ui): add sort by name, due date, and course`
- Commit: `feat(ui): add completed/incomplete filter`

---

## FE-10 Hapus Item dengan Dialog Konfirmasi

### 1. Judul
Hapus Kelas, Tugas, dan Ujian dengan konfirmasi (hapus Pekerjaan di FE-11).

### 2. Sub bab detail
- 2.1 Cara memicu hapus (tekan lama, tombol di form, atau geser)
- 2.2 Dialog konfirmasi
- 2.3 Pembaruan daftar setelah hapus

### 3. Penjelasan
**Apa yang dilakukan:** Menambah aksi hapus pada Kelas, Tugas, dan Ujian; hapus Pekerjaan dipasang di Ringkasan pada FE-11 dengan helper yang sama. Sebelum dihapus muncul dialog konfirmasi berbahasa Indonesia. Setelah dihapus, daftar langsung diperbarui dan kondisi kosong tampil bila perlu.
**Supaya apa:** Menyelesaikan "D" pada CRUD (FR-1, FR-2) dan mencegah hapus tak sengaja (Bagian 13.2 brief). Cara memicu hapus mengikuti repo acuan.
**Acuan PRD:** FR-1, FR-2, BR-2 (hapus selalu didahului dialog konfirmasi), PRD 8.2. Tidak boleh ada jalur hapus yang melewati dialog.

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-10: hapus dengan konfirmasi. Tambahkan aksi hapus untuk Kelas, Tugas,
dan Ujian (cara pemicu mengikuti repo acuan; jika tidak jelas, tanyakan dulu
dengan format pilihan A/B/C). Sebelum hapus tampilkan dialog konfirmasi
berbahasa Indonesia dengan tombol Batal dan Hapus. Setelah dikonfirmasi, hapus
lewat repository dan perbarui daftar. Buat satu helper dialog yang dipakai
ulang di semua layar (Ringkasan menyusul di task lain). Jangan mengubah bagian lain.
```

### 5. File yang dibuat
- `util/ConfirmDialog.java`: helper dialog konfirmasi yang dipakai ulang.

### 6. File yang diubah
- `ClassesFragment.java`, `AssignmentsFragment.java`, `ExamsFragment.java`: memicu dialog dan memuat ulang daftar.
- Adapter terkait: meneruskan aksi hapus (jika pemicunya di baris).
- `ClassFormActivity.java`, `ItemFormActivity.java`: tombol hapus pada mode ubah (jika dipilih).
- `strings.xml`: judul, pesan, dan tombol dialog.

### 7. Testing steps
1. Picu hapus: dialog muncul. Tekan Batal: item tetap ada.
2. Tekan Hapus: item hilang dari daftar tanpa perlu membuka ulang layar.
3. Hapus item terakhir: teks "Belum ada data" tampil.
4. Hapus saat filter aktif: daftar tetap konsisten.
5. Rotate layar saat dialog terbuka: tidak crash.
6. Semua jalur hapus (baris, form mode ubah) lewat dialog; tidak ada yang menghapus langsung (BR-2).

### 8. Branch dan commit message
- Branch: `feature/fe-10-hapus-konfirmasi`
- Commit: `feat(ui): add reusable delete confirmation dialog`
- Commit: `feat(ui): wire delete action for classes and items`

---

## FE-11 Layar Ringkasan

### 1. Judul
Ringkasan: daftar gabungan Tugas, Ujian, dan Pekerjaan.

### 2. Sub bab detail
- 2.1 Menggabungkan tiga jenis item dalam satu daftar
- 2.2 Tampilan baris dengan penanda jenis
- 2.3 Tombol tambah Pekerjaan `[ASUMSI]`
- 2.4 Interaksi: centang selesai, klik untuk ubah, dan hapus dengan konfirmasi

### 3. Penjelasan
**Apa yang dilakukan:** Menampilkan semua Tugas, Ujian, dan Pekerjaan sebagai satu daftar to-do. Tiap baris diberi penanda jenis (misalnya label kecil). Urutan awal menurut tenggat terdekat `[ASUMSI]`. Centang selesai dan klik untuk ubah memakai form FE-07. Tombol tambah di sini memungkinkan membuat Pekerjaan (dan item lain) karena Pekerjaan tidak punya halaman sendiri. Karena itu ubah dan hapus Pekerjaan (US-4) juga dilakukan dari sini; hapus memakai `ConfirmDialog` dari FE-10.
**Supaya apa:** Memenuhi FR-5 dan menjadi satu-satunya tempat Pekerjaan terlihat.
**Acuan PRD:** FR-5, FR-2, FR-4, US-4, US-8, BR-2, acceptance criteria FR-5 (ketiga jenis tampil dalam satu daftar).

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-11: layar Ringkasan. Isi OverviewFragment dengan satu daftar gabungan
Tugas, Ujian, dan Pekerjaan dari repository. Tiap baris punya penanda jenis,
nama, tenggat/waktu, mata kuliah, dan checkbox selesai. Urutan awal menurut
tenggat terdekat. Klik baris membuka ItemFormActivity sesuai jenisnya. Tombol
tambah menawarkan pilihan jenis (Tugas, Ujian, Pekerjaan) lalu membuka form.
Aksi hapus memakai ConfirmDialog dari FE-10. Kondisi kosong sama seperti layar lain.
Karena RecyclerView memuat tiga jenis
item, jelaskan pendekatan adapter yang dipilih; jika ada lebih dari satu cara
yang masuk akal, tanyakan dulu dengan format pilihan A/B/C.
```

### 5. File yang dibuat
- `adapter/OverviewAdapter.java`: adapter yang mengenali tiga jenis item.
- `res/layout/item_overview.xml`: baris dengan penanda jenis (atau tiga layout bila adapter multi-view-type).
- `model/OverviewItem.java` (opsional): pembungkus seragam untuk tiga jenis item di daftar gabungan.

### 6. File yang diubah
- `OverviewFragment.java` dan `fragment_overview.xml`: RecyclerView gabungan, teks kosong, FAB tambah dengan pilihan jenis, aksi hapus lewat `ConfirmDialog`.
- `InMemoryRepository.java`: tambah fungsi ambil gabungan (jika diperlukan).
- `strings.xml`: label jenis dan teks kosong.

### 7. Testing steps
1. Ringkasan menampilkan jumlah item sama dengan total Tugas + Ujian + Pekerjaan.
2. Tiap jenis punya penanda yang jelas dan berbeda.
3. Tambah Pekerjaan lewat tombol tambah: muncul di Ringkasan.
4. Centang item di Ringkasan lalu buka layar Tugas: status sinkron.
5. Hapus semua data: kondisi kosong tampil.
6. Ubah Pekerjaan dari Ringkasan: nilai lama terisi, perubahan tampil di daftar (US-4).
7. Hapus Pekerjaan dari Ringkasan: dialog konfirmasi muncul, Batal menjaga item, Hapus menghilangkannya (BR-2).

### 8. Branch dan commit message
- Branch: `feature/fe-11-ringkasan`
- Commit: `feat(ui): add overview screen with combined to-do list`
- Commit: `feat(ui): add task creation from overview`

---

## FE-12 Rapikan Kondisi Kosong/Error dan Uji Akhir Front End

### 1. Judul
Penyelesaian front end dan serah terima ke back end.

### 2. Sub bab detail
- 2.1 Audit kondisi kosong, error, dan dialog di semua layar
- 2.2 Audit teks dan tampilan (konsistensi, layar kecil, rotate)
- 2.3 Uji menyeluruh alur utama (Bagian 9 brief atau 7.1 PRD)
- 2.4 Catatan serah terima untuk back end

### 3. Penjelasan
**Apa yang dilakukan:** Memeriksa seluruh layar terhadap Bagian 13.2 brief atau 8.2 PRD (daftar kosong, input tidak valid, konfirmasi hapus, hasil filter kosong), merapikan tampilan yang tidak konsisten, lalu menulis catatan serah terima: daftar fungsi repository yang harus diganti back end dan perilaku yang diharapkan.
**Supaya apa:** Menjadi gerbang "front end selesai" sebelum back end dimulai. Kriteria selesai brief yang berkaitan dengan tampilan terpenuhi; kriteria "data tidak hilang setelah aplikasi ditutup" memang **belum** dipenuhi sampai back end selesai.
**Acuan PRD:** Bagian 8.2, acceptance criteria 9.3 (FR-1 sampai FR-5 sisi tampilan), Definition of Done 9.4, Bagian 10 (offline-first dan sederhana), BR-1. FR-6, US-9, dan BR-3 diserahkan ke back end.

### 4. Prompt AI
```
[Tempel prompt pembuka]
Task FE-12: audit akhir front end. Saya melampirkan seluruh layout dan Fragment/
Activity. Periksa terhadap Bagian 13.2 dan 14.2 brief serta Bagian 8.2, 9.3, dan 9.4 PRD:
(1) kondisi kosong, (2)
pesan error validasi, (3) dialog konfirmasi hapus, (4) hasil filter kosong, (5)
teks yang masih tertulis langsung atau berbahasa Inggris, (6) ketidakkonsistenan
tampilan, (7) permission INTERNET atau library jaringan yang tidak perlu. Berikan daftar temuan bertingkat (perlu diperbaiki / opsional) tanpa
memperbaiki dulu. Lalu buat draf catatan serah terima: daftar fungsi di
InMemoryRepository beserta perilaku yang harus dipertahankan saat diganti
penyimpanan permanen. Jangan menambah fitur baru.
```

### 5. File yang dibuat
- `docs/serah-terima-backend.md`: daftar fungsi repository, parameter, hasil yang diharapkan, dan hal yang masih menggunakan data dummy.
- `docs/checklist-uji-frontend.md`: hasil uji akhir (centang per skenario).

### 6. File yang diubah
- Layout dan file Java yang ditemukan bermasalah di audit (perbaikan kecil saja).
- `strings.xml`: perbaikan teks bila ada.
- `README.md`: status "front end selesai, back end belum".

### 7. Testing steps
1. Jalankan seluruh alur Bagian 9 brief (7.1 PRD): tambah, ubah, hapus, tandai selesai, urut/filter, buka Ringkasan.
2. Uji di dua ukuran layar emulator dan satu kali rotate di tiap layar.
3. Coba input ekstrem: nama sangat panjang, spasi saja, tanggal jauh di masa lalu atau depan; tidak crash.
4. Tidak ada teks Inggris atau teks tertulis langsung tersisa (*Find in Files* dan Lint).
5. Acceptance criteria PRD 9.3 untuk FR-1 sampai FR-5 lolos pada data dummy (frasa "tetap ada setelah aplikasi dibuka ulang" pada FR-1 dan seluruh FR-6 menunggu back end).
6. `AndroidManifest.xml` tidak memuat permission `INTERNET` dan tidak ada library jaringan atau Firebase (BR-1, PRD Bagian 10).
7. `docs/checklist-uji-frontend.md` terisi, semua kriteria tampilan tercentang.

### 8. Branch dan commit message
- Branch: `chore/fe-12-audit-akhir`
- Commit: `fix(ui): polish empty and error states`
- Commit: `docs: add frontend test checklist and backend handoff notes`
- Setelah merge ke `main`: beri tag `frontend-done` (`git tag frontend-done`).

---

## Lampiran A. Ringkasan Keputusan yang Masih Perlu Kamu Putuskan

| Butir | Status | Dibahas di |
|---|---|---|
| UI memakai XML atau Compose | `[BELUM TERVERIFIKASI]` | FE-00 |
| Nama paket, nama Activity/Fragment acuan | `[BELUM TERVERIFIKASI]` | FE-00, semua task |
| Relasi mata kuliah: teks bebas atau pilih dari Kelas | `[BELUM TERVERIFIKASI]` | FE-03, FE-07 |
| Status selesai pada Ujian | `[BELUM TERVERIFIKASI]` | FE-08 |
| Cara memilih hari pada Kelas | belum disebut di brief | FE-05 |
| Pekerjaan dibuat dari Ringkasan | `[ASUMSI]` | FE-07, FE-11 |
| Urutan awal Ringkasan menurut tenggat | `[ASUMSI]` | FE-11 |
| Aturan validasi | `[ASUMSI]` (Bagian 8 brief, 9.1 PRD) | FE-05, FE-07 |
| Nama aplikasi (PRD Bagian 14 no. 6) | belum diputuskan; sementara "College Scheduler" `[ASUMSI]` | FE-02 |
| `minSdk`, `targetSdk`, versi AGP dan Gradle (PRD Bagian 14 no. 5) | `[BELUM TERVERIFIKASI]` | FE-00 |
| Cara memicu hapus (tekan lama, tombol, geser) | mengikuti acuan, `[BELUM TERVERIFIKASI]` | FE-10 |
| Urut dan filter untuk Pekerjaan di Ringkasan | belum disebut di brief maupun PRD; perlu keputusan | FE-09, FE-11 |
| Syarat dosen (UML, ERD, format laporan) dan data tim (PRD Bagian 14 no. 7-8) | diisi pemilik proyek | di luar task front end |

## Lampiran B. Kriteria Selesai (Brief 14.2 / PRD 9.4) yang Dicakup Front End

| Kriteria | Front end | Menunggu back end |
|---|---|---|
| Tambah, lihat, ubah, hapus Kelas, Tugas, Ujian, Pekerjaan (FR-1, FR-2) | Tampilan dan alur: Kelas FE-04, FE-05, FE-10; Tugas FE-06, FE-07, FE-10; Ujian FE-07, FE-08, FE-10; Pekerjaan FE-07, FE-11 | Simpan permanen |
| Urut berdasarkan nama, tenggat, mata kuliah (FR-3) | FE-09 | Opsional: pindah ke query |
| Tandai selesai dan filter (FR-4) | FE-06, FE-08, FE-09, FE-11 | Simpan status permanen (BR-3) |
| Halaman Ringkasan (FR-5) | FE-11 | Data dari penyimpanan permanen |
| Data tidak hilang saat aplikasi ditutup (FR-6, US-9) | Tidak tercakup | Seluruhnya back end |
| Pesan error input tidak valid (PRD 9.1) | FE-05, FE-07 | - |
| Konfirmasi sebelum hapus (BR-2) | FE-10, FE-11 | - |
| Offline-first dan semua teks di `strings.xml` (BR-1, BR-4, PRD 10) | FE-02, FE-12 | - |

## Lampiran C. Penelusuran User Story ke Task

| User story | Fitur | Task front end |
|---|---|---|
| US-1 CRUD Kelas | FR-1 | FE-04, FE-05, FE-10 |
| US-2 CRUD Tugas | FR-2 | FE-06, FE-07, FE-10 |
| US-3 CRUD Ujian | FR-2 | FE-07, FE-08, FE-10 |
| US-4 CRUD Pekerjaan | FR-2 | FE-07 (tambah), FE-11 (lihat, ubah, hapus dari Ringkasan) |
| US-5 Urut item | FR-3 | FE-09 |
| US-6 Tandai selesai | FR-4 | FE-06, FE-08, FE-11 |
| US-7 Filter selesai/belum | FR-4 | FE-09 |
| US-8 Halaman Ringkasan | FR-5 | FE-11 |
| US-9 Data tetap ada | FR-6 | Back end |

| Aturan bisnis | Task front end |
|---|---|
| BR-1 Data lokal, tanpa server | FE-12 (cek manifest dan library) |
| BR-2 Hapus selalu dengan konfirmasi | FE-10, FE-11 |
| BR-3 Status selesai per item dan permanen | Tampilan di FE-06, FE-08, FE-11; permanen di back end |
| BR-4 Antarmuka Indonesia, teks di `strings.xml` | FE-02, FE-12 |
| BR-5 Status selesai pada Ujian | FE-03, FE-08, FE-09 (`[BELUM TERVERIFIKASI]`) |

## Lampiran D. Riwayat Perubahan Dokumen

| Tanggal | Perubahan |
|---|---|
| 31 Okt 2026 | Disinkronkan dengan `PRD-college-scheduler.md` v0.1: sumber dan prompt pembuka memuat PRD; tabel task diberi acuan FR/US/BR; tiap task diberi baris Acuan PRD; aturan umum ditambah (offline-first, sederhana, versi library, batasan versi); hapus dan ubah Pekerjaan dipindah ke FE-11 (US-4, BR-2); FE-00 dan FE-12 diperluas; Lampiran A diperbarui; Lampiran B dan C ditambah. |
