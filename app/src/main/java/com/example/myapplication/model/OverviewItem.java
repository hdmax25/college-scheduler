package com.example.myapplication.model;

import java.util.Objects;

/**
 * Model seragam (wrapper) untuk menggabungkan tiga jenis item to-do:
 * Tugas (Assignment), Ujian (Exam), dan Pekerjaan (WorkTask) pada Layar Ringkasan (Overview).
 * Sesuai panduan FE-11.
 */
public class OverviewItem {

    public enum Type {
        ASSIGNMENT,
        EXAM,
        TASK
    }

    private final Type type;
    private final long id;
    private final String name;
    private final String dateTime;
    private final String course;
    private final boolean isCompleted;
    private final String location; // Khusus untuk Ujian, null untuk Tugas & Pekerjaan

    public OverviewItem(Type type, long id, String name, String dateTime, String course, boolean isCompleted, String location) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.course = course;
        this.isCompleted = isCompleted;
        this.location = location;
    }

    public static OverviewItem fromAssignment(Assignment assignment) {
        if (assignment == null) return null;
        return new OverviewItem(
                Type.ASSIGNMENT,
                assignment.getId(),
                assignment.getName(),
                assignment.getDueDateTime(),
                assignment.getCourse(),
                assignment.isCompleted(),
                null
        );
    }

    public static OverviewItem fromExam(Exam exam) {
        if (exam == null) return null;
        return new OverviewItem(
                Type.EXAM,
                exam.getId(),
                exam.getName(),
                exam.getDateTime(),
                exam.getCourse(),
                exam.isCompleted(),
                exam.getLocation()
        );
    }

    public static OverviewItem fromTask(WorkTask task) {
        if (task == null) return null;
        return new OverviewItem(
                Type.TASK,
                task.getId(),
                task.getName(),
                task.getDueDateTime(),
                task.getCourse(),
                task.isCompleted(),
                null
        );
    }

    public Type getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getCourse() {
        return course;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverviewItem that = (OverviewItem) o;
        return id == that.id &&
                isCompleted == that.isCompleted &&
                type == that.type &&
                Objects.equals(name, that.name) &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(course, that.course) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, name, dateTime, course, isCompleted, location);
    }
}

