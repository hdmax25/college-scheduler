package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model data untuk Kelas / Mata Kuliah terjadwal.
 * Sesuai Bagian 7.1 brief dan PRD Bagian 6.1.
 */
public class CourseClass implements Serializable {
    private long id;
    private String name;
    private String days;
    private String startTime;
    private String endTime;
    private String room;
    private String section;
    private String professor;

    public CourseClass() {
    }

    public CourseClass(long id, String name, String days, String startTime, String endTime, String room, String section, String professor) {
        this.id = id;
        this.name = name;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.section = section;
        this.professor = professor;
    }

    public CourseClass(String name, String days, String startTime, String endTime, String room, String section, String professor) {
        this(0, name, days, startTime, endTime, room, section, professor);
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

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseClass that = (CourseClass) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

