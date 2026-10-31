package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model data untuk Pekerjaan (Task).
 * Diberi nama WorkTask agar tidak berbenturan dengan android.os.AsyncTask
 * atau com.google.android.gms.tasks.Task.
 * Sesuai Bagian 7.1 brief dan PRD Bagian 6.1.
 */
public class WorkTask implements Serializable {
    private long id;
    private String name;
    private String dueDateTime;
    private String course;
    private boolean isCompleted;

    public WorkTask() {
    }

    public WorkTask(long id, String name, String dueDateTime, String course, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.dueDateTime = dueDateTime;
        this.course = course;
        this.isCompleted = isCompleted;
    }

    public WorkTask(String name, String dueDateTime, String course, boolean isCompleted) {
        this(0, name, dueDateTime, course, isCompleted);
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

    public String getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(String dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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
        WorkTask workTask = (WorkTask) o;
        return id == workTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

