package com.example.myapplication.data;

import com.example.myapplication.model.Assignment;
import com.example.myapplication.model.CourseClass;
import com.example.myapplication.model.Exam;
import com.example.myapplication.model.WorkTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository in-memory sementara untuk menyimpan data dummy di memori selama tahap Front End.
 * Menjadi satu-satunya pintu gerbang data (Single Source of Truth) untuk Activity dan Fragment,
 * sehingga saat Back End dikerjakan, implementasi kelas ini dapat diganti dengan Room/SQLite
 * tanpa mengubah struktur layar UI.
 */
public class InMemoryRepository {

    private static InMemoryRepository instance;

    private final List<CourseClass> classList = new ArrayList<>();
    private final List<Assignment> assignmentList = new ArrayList<>();
    private final List<Exam> examList = new ArrayList<>();
    private final List<WorkTask> taskList = new ArrayList<>();

    private final AtomicLong classIdCounter = new AtomicLong(1);
    private final AtomicLong assignmentIdCounter = new AtomicLong(1);
    private final AtomicLong examIdCounter = new AtomicLong(1);
    private final AtomicLong taskIdCounter = new AtomicLong(1);

    private InMemoryRepository() {
        initSampleData();
    }

    public static synchronized InMemoryRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryRepository();
        }
        return instance;
    }

    // ==========================================
    // METODE KELAS (CourseClass)
    // ==========================================

    public synchronized List<CourseClass> getAllClasses() {
        return new ArrayList<>(classList);
    }

    public synchronized CourseClass getClassById(long id) {
        for (CourseClass item : classList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public synchronized void addClass(CourseClass courseClass) {
        if (courseClass.getId() <= 0) {
            courseClass.setId(classIdCounter.getAndIncrement());
        }
        classList.add(courseClass);
    }

    public synchronized boolean updateClass(CourseClass updated) {
        for (int i = 0; i < classList.size(); i++) {
            if (classList.get(i).getId() == updated.getId()) {
                classList.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteClass(long id) {
        return classList.removeIf(item -> item.getId() == id);
    }

    // ==========================================
    // METODE TUGAS (Assignment)
    // ==========================================

    public synchronized List<Assignment> getAllAssignments() {
        return new ArrayList<>(assignmentList);
    }

    public synchronized Assignment getAssignmentById(long id) {
        for (Assignment item : assignmentList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public synchronized void addAssignment(Assignment assignment) {
        if (assignment.getId() <= 0) {
            assignment.setId(assignmentIdCounter.getAndIncrement());
        }
        assignmentList.add(assignment);
    }

    public synchronized boolean updateAssignment(Assignment updated) {
        for (int i = 0; i < assignmentList.size(); i++) {
            if (assignmentList.get(i).getId() == updated.getId()) {
                assignmentList.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteAssignment(long id) {
        return assignmentList.removeIf(item -> item.getId() == id);
    }

    public synchronized boolean setAssignmentCompleted(long id, boolean isCompleted) {
        Assignment item = getAssignmentById(id);
        if (item != null) {
            item.setCompleted(isCompleted);
            return true;
        }
        return false;
    }

    // ==========================================
    // METODE UJIAN (Exam)
    // ==========================================

    public synchronized List<Exam> getAllExams() {
        return new ArrayList<>(examList);
    }

    public synchronized Exam getExamById(long id) {
        for (Exam item : examList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public synchronized void addExam(Exam exam) {
        if (exam.getId() <= 0) {
            exam.setId(examIdCounter.getAndIncrement());
        }
        examList.add(exam);
    }

    public synchronized boolean updateExam(Exam updated) {
        for (int i = 0; i < examList.size(); i++) {
            if (examList.get(i).getId() == updated.getId()) {
                examList.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteExam(long id) {
        return examList.removeIf(item -> item.getId() == id);
    }

    public synchronized boolean setExamCompleted(long id, boolean isCompleted) {
        Exam item = getExamById(id);
        if (item != null) {
            item.setCompleted(isCompleted);
            return true;
        }
        return false;
    }

    // ==========================================
    // METODE PEKERJAAN (WorkTask)
    // ==========================================

    public synchronized List<WorkTask> getAllTasks() {
        return new ArrayList<>(taskList);
    }

    public synchronized WorkTask getTaskById(long id) {
        for (WorkTask item : taskList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public synchronized void addTask(WorkTask task) {
        if (task.getId() <= 0) {
            task.setId(taskIdCounter.getAndIncrement());
        }
        taskList.add(task);
    }

    public synchronized boolean updateTask(WorkTask updated) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == updated.getId()) {
                taskList.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteTask(long id) {
        return taskList.removeIf(item -> item.getId() == id);
    }

    public synchronized boolean setTaskCompleted(long id, boolean isCompleted) {
        WorkTask item = getTaskById(id);
        if (item != null) {
            item.setCompleted(isCompleted);
            return true;
        }
        return false;
    }

    // ==========================================
    // DATA CONTOH & HELPER TESTING
    // ==========================================

    public synchronized void clearAll() {
        classList.clear();
        assignmentList.clear();
        examList.clear();
        taskList.clear();
    }

    public synchronized void resetToSampleData() {
        clearAll();
        classIdCounter.set(1);
        assignmentIdCounter.set(1);
        examIdCounter.set(1);
        taskIdCounter.set(1);
        initSampleData();
    }

    private void initSampleData() {
        // 4 Data contoh Kelas
        addClass(new CourseClass("Pemrograman Bergerak", "Senin, Kamis", "08:00", "09:40", "Lab Komputer 2", "RA", "Dr. Budi Santoso"));
        addClass(new CourseClass("Basis Data Lanjut", "Selasa", "10:00", "12:30", "Gedung C Ruang 204", "RB", "Prof. Siti Aminah"));
        addClass(new CourseClass("Rekayasa Perangkat Lunak", "Rabu", "13:00", "15:30", "Gedung B Ruang 101", "RC", "Ir. Ahmad Fauzi, M.T."));
        addClass(new CourseClass("Kecerdasan Buatan", "Jumat", "08:30", "11:00", "Lab AI & Data", "RA", "Dr. Hendra Wijaya"));

        // 4 Data contoh Tugas
        addAssignment(new Assignment("Implementasi Bottom Navigation", "2026-11-05 23:59", "Pemrograman Bergerak", true));
        addAssignment(new Assignment("Desain Skema ERD & Normalisasi", "2026-11-08 23:59", "Basis Data Lanjut", false));
        addAssignment(new Assignment("Penyusunan SRS & Activity Diagram", "2026-11-12 23:59", "Rekayasa Perangkat Lunak", false));
        addAssignment(new Assignment("Eksperimen Model Klasifikasi", "2026-11-15 23:59", "Kecerdasan Buatan", false));

        // 4 Data contoh Ujian (dengan dukungan status selesai sesuai BR-5)
        addExam(new Exam("UTS Pemrograman Bergerak", "2026-11-10 08:00", "Pemrograman Bergerak", "Lab Komputer 2", false));
        addExam(new Exam("UTS Basis Data Lanjut", "2026-11-11 10:00", "Basis Data Lanjut", "Gedung C Ruang 204", false));
        addExam(new Exam("Kuis 2 Rekayasa Perangkat Lunak", "2026-11-14 13:00", "Rekayasa Perangkat Lunak", "Gedung B Ruang 101", true));
        addExam(new Exam("Ujian Praktikum Kecerdasan Buatan", "2026-11-18 09:00", "Kecerdasan Buatan", "Lab AI & Data", false));

        // 4 Data contoh Pekerjaan Lain (Task)
        addTask(new WorkTask("Membeli buku referensi Android", "2026-11-03 17:00", "Pemrograman Bergerak", true));
        addTask(new WorkTask("Membentuk kelompok proyek akhir", "2026-11-06 15:00", "Rekayasa Perangkat Lunak", false));
        addTask(new WorkTask("Mempersiapkan dataset survei", "2026-11-09 20:00", "Kecerdasan Buatan", false));
        addTask(new WorkTask("Membaca modul indexing & query plan", "2026-11-11 21:00", "Basis Data Lanjut", false));
    }
}

