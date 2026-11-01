package com.example.myapplication.util;

import com.example.myapplication.model.Assignment;
import com.example.myapplication.model.Exam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utilitas untuk mengurutkan (sorting) dan menyaring (filtering) daftar item (Tugas & Ujian).
 * Dilakukan di lapisan tampilan sesuai panduan FE-09.
 */
public class ItemSorter {

    public enum SortOption {
        NAME,
        DATE,
        COURSE
    }

    public enum FilterOption {
        ALL,
        INCOMPLETE,
        COMPLETED
    }

    // ==========================================
    // OPERASI UNTUK TUGAS (Assignment)
    // ==========================================

    public static List<Assignment> filterAndSortAssignments(
            List<Assignment> source,
            FilterOption filter,
            SortOption sort) {
        if (source == null) {
            return new ArrayList<>();
        }

        List<Assignment> filtered = new ArrayList<>();
        for (Assignment item : source) {
            if (matchesFilter(item.isCompleted(), filter)) {
                filtered.add(item);
            }
        }

        Collections.sort(filtered, getAssignmentComparator(sort));
        return filtered;
    }

    private static Comparator<Assignment> getAssignmentComparator(SortOption sort) {
        if (sort == SortOption.NAME) {
            return (a, b) -> safeCompare(a.getName(), b.getName());
        } else if (sort == SortOption.COURSE) {
            return (a, b) -> safeCompare(a.getCourse(), b.getCourse());
        } else {
            // Default DATE (Tenggat terdekat di atas)
            return (a, b) -> safeCompare(a.getDueDateTime(), b.getDueDateTime());
        }
    }

    // ==========================================
    // OPERASI UNTUK UJIAN (Exam)
    // ==========================================

    public static List<Exam> filterAndSortExams(
            List<Exam> source,
            FilterOption filter,
            SortOption sort) {
        if (source == null) {
            return new ArrayList<>();
        }

        List<Exam> filtered = new ArrayList<>();
        for (Exam item : source) {
            if (matchesFilter(item.isCompleted(), filter)) {
                filtered.add(item);
            }
        }

        Collections.sort(filtered, getExamComparator(sort));
        return filtered;
    }

    private static Comparator<Exam> getExamComparator(SortOption sort) {
        if (sort == SortOption.NAME) {
            return (a, b) -> safeCompare(a.getName(), b.getName());
        } else if (sort == SortOption.COURSE) {
            return (a, b) -> safeCompare(a.getCourse(), b.getCourse());
        } else {
            // Default DATE (Waktu ujian terdekat di atas)
            return (a, b) -> safeCompare(a.getDateTime(), b.getDateTime());
        }
    }

    // ==========================================
    // HELPER
    // ==========================================

    private static boolean matchesFilter(boolean isCompleted, FilterOption filter) {
        if (filter == FilterOption.COMPLETED) {
            return isCompleted;
        } else if (filter == FilterOption.INCOMPLETE) {
            return !isCompleted;
        }
        return true; // ALL
    }

    private static int safeCompare(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return 1;
        if (s2 == null) return -1;
        return s1.compareToIgnoreCase(s2);
    }
}

