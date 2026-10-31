package com.example.myapplication;

import com.example.myapplication.data.InMemoryRepository;
import com.example.myapplication.model.Assignment;
import com.example.myapplication.model.CourseClass;
import com.example.myapplication.model.Exam;
import com.example.myapplication.model.WorkTask;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class InMemoryRepositoryTest {

    private InMemoryRepository repository;

    @Before
    public void setUp() {
        repository = InMemoryRepository.getInstance();
        repository.resetToSampleData();
    }

    @Test
    public void testInitialSampleDataCount() {
        assertEquals("Jumlah kelas awal harus 4", 4, repository.getAllClasses().size());
        assertEquals("Jumlah tugas awal harus 4", 4, repository.getAllAssignments().size());
        assertEquals("Jumlah ujian awal harus 4", 4, repository.getAllExams().size());
        assertEquals("Jumlah pekerjaan awal harus 4", 4, repository.getAllTasks().size());
    }

    @Test
    public void testClassCrudOperations() {
        // 1. Tambah
        CourseClass newClass = new CourseClass("Jaringan Komputer", "Selasa", "13:00", "15:00", "Lab Jaringan", "RA", "Dr. Anton");
        repository.addClass(newClass);
        assertEquals(5, repository.getAllClasses().size());
        assertTrue(newClass.getId() > 0);

        // 2. Ambil by ID
        CourseClass fetched = repository.getClassById(newClass.getId());
        assertNotNull(fetched);
        assertEquals("Jaringan Komputer", fetched.getName());

        // 3. Ubah
        fetched.setRoom("Lab Jaringan 2");
        boolean updated = repository.updateClass(fetched);
        assertTrue(updated);
        assertEquals("Lab Jaringan 2", repository.getClassById(newClass.getId()).getRoom());

        // 4. Hapus
        boolean deleted = repository.deleteClass(newClass.getId());
        assertTrue(deleted);
        assertEquals(4, repository.getAllClasses().size());
        assertNull(repository.getClassById(newClass.getId()));
    }

    @Test
    public void testAssignmentCrudAndToggleCompletion() {
        // 1. Tambah
        Assignment newAssignment = new Assignment("Tugas 1 Socket Programming", "2026-11-20 23:59", "Jaringan Komputer", false);
        repository.addAssignment(newAssignment);
        assertEquals(5, repository.getAllAssignments().size());

        // 2. Tandai Selesai
        boolean marked = repository.setAssignmentCompleted(newAssignment.getId(), true);
        assertTrue(marked);
        assertTrue(repository.getAssignmentById(newAssignment.getId()).isCompleted());

        // 3. Tandai Belum Selesai
        repository.setAssignmentCompleted(newAssignment.getId(), false);
        assertFalse(repository.getAssignmentById(newAssignment.getId()).isCompleted());

        // 4. Ubah
        newAssignment.setName("Tugas 1 Socket Programming Revisi");
        repository.updateAssignment(newAssignment);
        assertEquals("Tugas 1 Socket Programming Revisi", repository.getAssignmentById(newAssignment.getId()).getName());

        // 5. Hapus
        boolean deleted = repository.deleteAssignment(newAssignment.getId());
        assertTrue(deleted);
        assertEquals(4, repository.getAllAssignments().size());
    }

    @Test
    public void testExamCrudAndToggleCompletion() {
        // 1. Tambah
        Exam newExam = new Exam("UTS Jaringan Komputer", "2026-11-25 08:00", "Jaringan Komputer", "Ruang 401", false);
        repository.addExam(newExam);
        assertEquals(5, repository.getAllExams().size());

        // 2. Tandai Selesai (BR-5)
        repository.setExamCompleted(newExam.getId(), true);
        assertTrue(repository.getExamById(newExam.getId()).isCompleted());

        // 3. Ubah
        newExam.setLocation("Ruang 402");
        repository.updateExam(newExam);
        assertEquals("Ruang 402", repository.getExamById(newExam.getId()).getLocation());

        // 4. Hapus
        boolean deleted = repository.deleteExam(newExam.getId());
        assertTrue(deleted);
        assertEquals(4, repository.getAllExams().size());
    }

    @Test
    public void testWorkTaskCrudAndToggleCompletion() {
        // 1. Tambah
        WorkTask newTask = new WorkTask("Install Cisco Packet Tracer", "2026-11-04 12:00", "Jaringan Komputer", false);
        repository.addTask(newTask);
        assertEquals(5, repository.getAllTasks().size());

        // 2. Tandai Selesai
        repository.setTaskCompleted(newTask.getId(), true);
        assertTrue(repository.getTaskById(newTask.getId()).isCompleted());

        // 3. Ubah
        newTask.setName("Install Wireshark & Cisco Packet Tracer");
        repository.updateTask(newTask);
        assertEquals("Install Wireshark & Cisco Packet Tracer", repository.getTaskById(newTask.getId()).getName());

        // 4. Hapus
        boolean deleted = repository.deleteTask(newTask.getId());
        assertTrue(deleted);
        assertEquals(4, repository.getAllTasks().size());
    }
}

