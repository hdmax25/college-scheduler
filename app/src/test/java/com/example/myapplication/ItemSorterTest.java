package com.example.myapplication;

import com.example.myapplication.model.Assignment;
import com.example.myapplication.model.Exam;
import com.example.myapplication.model.OverviewItem;
import com.example.myapplication.util.ItemSorter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ItemSorterTest {

    private List<Assignment> sampleAssignments;
    private List<Exam> sampleExams;

    @Before
    public void setUp() {
        sampleAssignments = new ArrayList<>();
        sampleAssignments.add(new Assignment(1, "Z-Tugas Akhir", "2026-11-20 23:59", "Kecerdasan Buatan", false));
        sampleAssignments.add(new Assignment(2, "A-Tugas Awal", "2026-11-05 23:59", "Pemrograman Bergerak", true));
        sampleAssignments.add(new Assignment(3, "M-Tugas Tengah", "2026-11-10 23:59", "Basis Data", false));

        sampleExams = new ArrayList<>();
        sampleExams.add(new Exam(1, "UTS Pemrograman", "2026-11-15 08:00", "Pemrograman Bergerak", "R301", false));
        sampleExams.add(new Exam(2, "Kuis Basis Data", "2026-11-02 10:00", "Basis Data", "R204", true));
        sampleExams.add(new Exam(3, "UAS AI", "2026-12-01 13:00", "Kecerdasan Buatan", "Lab AI", false));
    }

    @Test
    public void testSortAssignmentsByName() {
        List<Assignment> result = ItemSorter.filterAndSortAssignments(
                sampleAssignments,
                ItemSorter.FilterOption.ALL,
                ItemSorter.SortOption.NAME
        );
        assertEquals(3, result.size());
        assertEquals("A-Tugas Awal", result.get(0).getName());
        assertEquals("M-Tugas Tengah", result.get(1).getName());
        assertEquals("Z-Tugas Akhir", result.get(2).getName());
    }

    @Test
    public void testSortAssignmentsByDate() {
        List<Assignment> result = ItemSorter.filterAndSortAssignments(
                sampleAssignments,
                ItemSorter.FilterOption.ALL,
                ItemSorter.SortOption.DATE
        );
        assertEquals(3, result.size());
        assertEquals("2026-11-05 23:59", result.get(0).getDueDateTime());
        assertEquals("2026-11-10 23:59", result.get(1).getDueDateTime());
        assertEquals("2026-11-20 23:59", result.get(2).getDueDateTime());
    }

    @Test
    public void testSortAssignmentsByCourse() {
        List<Assignment> result = ItemSorter.filterAndSortAssignments(
                sampleAssignments,
                ItemSorter.FilterOption.ALL,
                ItemSorter.SortOption.COURSE
        );
        assertEquals(3, result.size());
        assertEquals("Basis Data", result.get(0).getCourse());
        assertEquals("Kecerdasan Buatan", result.get(1).getCourse());
        assertEquals("Pemrograman Bergerak", result.get(2).getCourse());
    }

    @Test
    public void testFilterAssignments() {
        // Filter Incomplete
        List<Assignment> incomplete = ItemSorter.filterAndSortAssignments(
                sampleAssignments,
                ItemSorter.FilterOption.INCOMPLETE,
                ItemSorter.SortOption.DATE
        );
        assertEquals(2, incomplete.size());
        for (Assignment a : incomplete) {
            assertFalse(a.isCompleted());
        }

        // Filter Completed
        List<Assignment> completed = ItemSorter.filterAndSortAssignments(
                sampleAssignments,
                ItemSorter.FilterOption.COMPLETED,
                ItemSorter.SortOption.DATE
        );
        assertEquals(1, completed.size());
        assertTrue(completed.get(0).isCompleted());
    }

    @Test
    public void testSortExamsByDateAndFilter() {
        List<Exam> incomplete = ItemSorter.filterAndSortExams(
                sampleExams,
                ItemSorter.FilterOption.INCOMPLETE,
                ItemSorter.SortOption.DATE
        );
        assertEquals(2, incomplete.size());
        assertEquals("UTS Pemrograman", incomplete.get(0).getName());
        assertEquals("UAS AI", incomplete.get(1).getName());

        List<Exam> completed = ItemSorter.filterAndSortExams(
                sampleExams,
                ItemSorter.FilterOption.COMPLETED,
                ItemSorter.SortOption.NAME
        );
        assertEquals(1, completed.size());
        assertEquals("Kuis Basis Data", completed.get(0).getName());
    }

    @Test
    public void testFilterResultEmpty() {
        // Jika semua item dibuat incomplete, filter COMPLETED harus menghasilkan daftar kosong
        for (Assignment a : sampleAssignments) {
            a.setCompleted(false);
        }
        List<Assignment> result = ItemSorter.filterAndSortAssignments(
                sampleAssignments,
                ItemSorter.FilterOption.COMPLETED,
                ItemSorter.SortOption.DATE
        );
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSortOverviewItems() {
        List<OverviewItem> list = new ArrayList<>();
        list.add(new OverviewItem(OverviewItem.Type.ASSIGNMENT, 1, "Tugas B", "2026-11-20 23:59", "Matkul", false, null));
        list.add(new OverviewItem(OverviewItem.Type.EXAM, 2, "Ujian A", "2026-11-05 08:00", "Matkul", false, "R1"));
        list.add(new OverviewItem(OverviewItem.Type.TASK, 3, "Pekerjaan C", "2026-11-10 15:00", "Matkul", false, null));

        List<OverviewItem> sorted = ItemSorter.sortOverviewItems(list);
        assertEquals(3, sorted.size());
        assertEquals("Ujian A", sorted.get(0).getName());
        assertEquals("Pekerjaan C", sorted.get(1).getName());
        assertEquals("Tugas B", sorted.get(2).getName());
    }
}

