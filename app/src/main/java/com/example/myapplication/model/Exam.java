package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model data untuk Ujian (Exam).
 * Sesuai Bagian 7.1 brief dan PRD Bagian 6.1 (BR-5).
 */
public class Exam implements Serializable {
    private long id;
    private String name;
    private String dateTime;
    private String course;
    private String location;
    private boolean isCompleted;

    public Exam() {
    }

    public Exam(long id, String name, String dateTime, String course, String location, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.course = course;
        this.location = location;
        this.isCompleted = isCompleted;
    }

    public Exam(String name, String dateTime, String course, String location, boolean isCompleted) {
        this(0, name, dateTime, course, location, isCompleted);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return id == exam.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

