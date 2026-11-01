# Catatan Serah Terima Front End ke Back End

Dokumen ini disusun sebagai gerbang penyelesaian tahap **Front End** (FE-01 s/d FE-12) dan panduan bagi tim **Back End** untuk mengimplementasikan persistensi data permanen (Room / SQLite) sesuai PRD v0.1 dan `brief-college-scheduler.md`.

---

## 1. Status Penyelesaian Front End

- **Status:** **Front End Selesai (Frontend-Done)**.
- **Data Layer Saat Ini:** Menggunakan `InMemoryRepository` (in-memory singleton) dengan data dummy statis saat aplikasi dimulai.
- **Kondisi Data:** Data saat ini **belum tersimpan permanen**. Jika aplikasi ditutup (process killed), data akan kembali ke data dummy awal. Tugas utama tim Back End adalah mengganti mekanisme penyimpanan internal `InMemoryRepository` ke basis data lokal (Room / SQLite) tanpa merusak signature publik yang dipakai oleh Activity dan Fragment.

---

## 2. Kontrak API Repository (`InMemoryRepository`)

Seluruh layar UI (`ClassesFragment`, `AssignmentsFragment`, `ExamsFragment`, `OverviewFragment`, `ClassFormActivity`, `ItemFormActivity`) mengakses data **hanya** melalui `InMemoryRepository.getInstance()`. Tim Back End dapat mempertahankan antarmuka/metode ini atau membuat DAO/Repository Room dengan signature berikut:

### 2.1 Entitas Kelas (`CourseClass`)
| Metode | Parameter | Return Type | Perilaku yang Diharapkan |
|---|---|---|---|
| `getAllClasses()` | - | `List<CourseClass>` | Mengembalikan seluruh kelas yang tersimpan, urut berdasarkan waktu pembuatan atau nama. |
| `getClassById(long id)` | `long id` | `CourseClass` | Mengembalikan kelas berdasarkan ID, atau `null` jika tidak ditemukan. |
| `addClass(CourseClass item)` | `CourseClass item` | `void` | Menyimpan kelas baru dan mengisi `id` unik yang di-generate (auto-increment). |
| `updateClass(CourseClass updated)` | `CourseClass updated` | `boolean` | Memperbarui kelas dengan ID yang sama. Mengembalikan `true` jika berhasil. |
| `deleteClass(long id)` | `long id` | `boolean` | Menghapus kelas berdasarkan ID. Mengembalikan `true` jika berhasil. |

### 2.2 Entitas Tugas (`Assignment`)
| Metode | Parameter | Return Type | Perilaku yang Diharapkan |
|---|---|---|---|
| `getAllAssignments()` | - | `List<Assignment>` | Mengembalikan seluruh tugas. |
| `getAssignmentById(long id)` | `long id` | `Assignment` | Mengembalikan tugas berdasarkan ID, atau `null`. |
| `addAssignment(Assignment item)` | `Assignment item` | `void` | Menyimpan tugas baru dengan ID unik otomatis. |
| `updateAssignment(Assignment updated)`| `Assignment updated` | `boolean` | Memperbarui data tugas. Mengembalikan `true` jika berhasil. |
| `deleteAssignment(long id)` | `long id` | `boolean` | Menghapus tugas berdasarkan ID. |
| `setAssignmentCompleted(long id, boolean isCompleted)` | `long id, boolean isCompleted` | `boolean` | Memperbarui status selesai tugas dan menyimpan secara permanen (BR-3). |

### 2.3 Entitas Ujian (`Exam`)
| Metode | Parameter | Return Type | Perilaku yang Diharapkan |
|---|---|---|---|
| `getAllExams()` | - | `List<Exam>` | Mengembalikan seluruh ujian. |
| `getExamById(long id)` | `long id` | `Exam` | Mengembalikan ujian berdasarkan ID, atau `null`. |
| `addExam(Exam item)` | `Exam item` | `void` | Menyimpan ujian baru dengan ID unik otomatis. |
| `updateExam(Exam updated)` | `Exam updated` | `boolean` | Memperbarui data ujian termasuk field lokasi. |
| `deleteExam(long id)` | `long id` | `boolean` | Menghapus ujian berdasarkan ID. |
| `setExamCompleted(long id, boolean isCompleted)` | `long id, boolean isCompleted` | `boolean` | Memperbarui status selesai ujian (BR-5). |

### 2.4 Entitas Pekerjaan Lain (`WorkTask`)
| Metode | Parameter | Return Type | Perilaku yang Diharapkan |
|---|---|---|---|
| `getAllTasks()` | - | `List<WorkTask>` | Mengembalikan seluruh pekerjaan lain. |
| `getTaskById(long id)` | `long id` | `WorkTask` | Mengembalikan pekerjaan berdasarkan ID, atau `null`. |
| `addTask(WorkTask item)` | `WorkTask item` | `void` | Menyimpan pekerjaan baru dengan ID unik otomatis. |
| `updateTask(WorkTask updated)` | `WorkTask updated` | `boolean` | Memperbarui data pekerjaan. |
| `deleteTask(long id)` | `long id` | `boolean` | Menghapus pekerjaan berdasarkan ID. |
| `setTaskCompleted(long id, boolean isCompleted)` | `long id, boolean isCompleted` | `boolean` | Memperbarui status selesai pekerjaan. |

### 2.5 Agregasi Ringkasan (`OverviewItem`)
| Metode | Parameter | Return Type | Perilaku yang Diharapkan |
|---|---|---|---|
| `getAllOverviewItems()` | - | `List<OverviewItem>` | Menggabungkan seluruh Tugas, Ujian, dan Pekerjaan ke dalam format seragam `OverviewItem` (FR-5). Back End dapat melakukan query SQL `UNION ALL` atau menggabungkannya di lapisan repositori. |

---

## 3. Aturan Bisnis & Hal yang Harus Dipertahankan

1. **BR-1 (Offline Only):** Jangan menambahkan dependency jaringan (seperti Retrofit, Firebase) atau permission `INTERNET` pada `AndroidManifest.xml`.
2. **BR-2 (Konfirmasi Hapus):** Seluruh aksi hapus di UI sudah diproteksi oleh `ConfirmDialog`. Back End cukup mengeksekusi penghapusan pada database saat dipanggil.
3. **BR-3 (Status Selesai Permanen):** Status `isCompleted` pada setiap item harus disimpan di tabel SQLite/Room dan bertahan antar sesi aplikasi.
4. **Format Tanggal dan Jam:** Nilai tanggal dan waktu disimpan dalam format string ISO `"yyyy-MM-dd HH:mm"` (misal: `"2026-11-05 23:59"`), sedangkan jam kelas `"HH:mm"` (misal: `"08:00"`). UI telah dilengkapi `DateFormatUtils` untuk formatting tampilan lokal Indonesia.
5. **Thread Safety & Asynchronous:** Saat bermigrasi ke Room, perhatikan bahwa query Room tidak boleh berjalan di main thread (gunakan `Executors`, `Coroutine`, atau `LiveData`/`Flow` jika arsitektur di-upgrade).

---

## 4. Struktur Model Data yang Tersedia

- `com.example.myapplication.model.CourseClass`
- `com.example.myapplication.model.Assignment`
- `com.example.myapplication.model.Exam`
- `com.example.myapplication.model.WorkTask`
- `com.example.myapplication.model.OverviewItem`

Semua model telah dilengkapi getter/setter, konstruktor, serta implementasi `equals` dan `hashCode`.

