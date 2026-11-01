package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.data.InMemoryRepository;
import com.example.myapplication.model.Assignment;
import com.example.myapplication.model.CourseClass;
import com.example.myapplication.model.Exam;
import com.example.myapplication.model.WorkTask;
import com.example.myapplication.util.ConfirmDialog;
import com.example.myapplication.util.ValidationUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 * Satu Activity form untuk tiga jenis item: Tugas (Assignment), Ujian (Exam), dan Pekerjaan (WorkTask).
 * Sesuai panduan FE-07 dan FE-10 (Hapus dengan dialog konfirmasi).
 */
public class ItemFormActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_TYPE = "extra_item_type";
    public static final String EXTRA_ITEM_ID = "extra_item_id";

    public static final String TYPE_ASSIGNMENT = "assignment";
    public static final String TYPE_EXAM = "exam";
    public static final String TYPE_TASK = "task";

    private String itemType = TYPE_ASSIGNMENT;
    private long itemId = -1;
    private boolean isEditMode = false;

    private MaterialToolbar toolbar;
    private TextInputLayout layoutItemName;
    private TextInputLayout layoutItemCourse;
    private TextInputLayout layoutItemDate;
    private TextInputLayout layoutItemTime;
    private TextInputLayout layoutItemLocation;

    private TextInputEditText editItemName;
    private MaterialAutoCompleteTextView autoItemCourse;
    private TextInputEditText editItemDate;
    private TextInputEditText editItemTime;
    private TextInputEditText editItemLocation;

    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    // Objek yang sedang diedit (salah satu diisi saat mode ubah)
    private Assignment existingAssignment;
    private Exam existingExam;
    private WorkTask existingTask;

    public static Intent newIntent(Context context, String itemType, long itemId) {
        Intent intent = new Intent(context, ItemFormActivity.class);
        intent.putExtra(EXTRA_ITEM_TYPE, itemType);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_form);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_item_form), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        parseIntentData();
        setupCourseDropdown();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar_item_form);
        layoutItemName = findViewById(R.id.layout_item_name);
        layoutItemCourse = findViewById(R.id.layout_item_course);
        layoutItemDate = findViewById(R.id.layout_item_date);
        layoutItemTime = findViewById(R.id.layout_item_time);
        layoutItemLocation = findViewById(R.id.layout_item_location);

        editItemName = findViewById(R.id.edit_item_name);
        autoItemCourse = findViewById(R.id.auto_item_course);
        editItemDate = findViewById(R.id.edit_item_date);
        editItemTime = findViewById(R.id.edit_item_time);
        editItemLocation = findViewById(R.id.edit_item_location);

        btnSave = findViewById(R.id.btn_save_item);
        btnCancel = findViewById(R.id.btn_cancel_item);
    }

    private void parseIntentData() {
        if (getIntent() != null) {
            String typeExtra = getIntent().getStringExtra(EXTRA_ITEM_TYPE);
            if (typeExtra != null) {
                itemType = typeExtra;
            }
            itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        }

        // Kolom lokasi hanya ditampilkan khusus jenis Ujian
        if (TYPE_EXAM.equals(itemType)) {
            layoutItemLocation.setVisibility(View.VISIBLE);
        } else {
            layoutItemLocation.setVisibility(View.GONE);
        }

        isEditMode = (itemId != -1);

        if (isEditMode) {
            toolbar.inflateMenu(R.menu.menu_form);
            loadExistingData();
        } else {
            setupAddModeTitle();
        }
    }

    private void setupAddModeTitle() {
        if (TYPE_EXAM.equals(itemType)) {
            toolbar.setTitle(R.string.title_add_exam);
        } else if (TYPE_TASK.equals(itemType)) {
            toolbar.setTitle(R.string.title_add_task);
        } else {
            toolbar.setTitle(R.string.title_add_assignment);
        }
    }

    private void loadExistingData() {
        InMemoryRepository repo = InMemoryRepository.getInstance();
        if (TYPE_EXAM.equals(itemType)) {
            existingExam = repo.getExamById(itemId);
            if (existingExam != null) {
                toolbar.setTitle(R.string.title_edit_exam);
                editItemName.setText(existingExam.getName());
                autoItemCourse.setText(existingExam.getCourse(), false);
                editItemLocation.setText(existingExam.getLocation());
                splitAndSetDateTime(existingExam.getDateTime());
            }
        } else if (TYPE_TASK.equals(itemType)) {
            existingTask = repo.getTaskById(itemId);
            if (existingTask != null) {
                toolbar.setTitle(R.string.title_edit_task);
                editItemName.setText(existingTask.getName());
                autoItemCourse.setText(existingTask.getCourse(), false);
                splitAndSetDateTime(existingTask.getDueDateTime());
            }
        } else {
            existingAssignment = repo.getAssignmentById(itemId);
            if (existingAssignment != null) {
                toolbar.setTitle(R.string.title_edit_assignment);
                editItemName.setText(existingAssignment.getName());
                autoItemCourse.setText(existingAssignment.getCourse(), false);
                splitAndSetDateTime(existingAssignment.getDueDateTime());
            }
        }
    }

    private void splitAndSetDateTime(String dateTime) {
        if (dateTime != null && dateTime.contains(" ")) {
            String[] parts = dateTime.trim().split(" ");
            editItemDate.setText(parts[0]);
            if (parts.length > 1) {
                editItemTime.setText(parts[1]);
            }
        } else if (dateTime != null) {
            editItemDate.setText(dateTime);
        }
    }

    private void setupCourseDropdown() {
        // Mengumpulkan daftar mata kuliah dari kelas yang terdaftar
        List<CourseClass> classes = InMemoryRepository.getInstance().getAllClasses();
        Set<String> courseNames = new LinkedHashSet<>();
        for (CourseClass c : classes) {
            if (c.getName() != null && !c.getName().trim().isEmpty()) {
                courseNames.add(c.getName().trim());
            }
        }

        List<String> list = new ArrayList<>(courseNames);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);
        autoItemCourse.setAdapter(adapter);
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        // Menu hapus di toolbar (hanya aktif saat mode ubah)
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete_form) {
                showDeleteConfirmation();
                return true;
            }
            return false;
        });

        // Pemilih Tanggal
        View.OnClickListener datePickerListener = v -> showDatePicker();
        editItemDate.setOnClickListener(datePickerListener);
        layoutItemDate.setEndIconOnClickListener(datePickerListener);

        // Pemilih Jam
        View.OnClickListener timePickerListener = v -> showTimePicker();
        editItemTime.setOnClickListener(timePickerListener);
        layoutItemTime.setEndIconOnClickListener(timePickerListener);

        // Hapus error saat input diubah
        editItemName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutItemName.setError(null);
            }
        });

        autoItemCourse.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutItemCourse.setError(null);
            }
        });

        btnSave.setOnClickListener(v -> validateAndSave());
    }

    private void showDeleteConfirmation() {
        String itemName = "";
        if (TYPE_EXAM.equals(itemType) && existingExam != null) {
            itemName = existingExam.getName();
        } else if (TYPE_TASK.equals(itemType) && existingTask != null) {
            itemName = existingTask.getName();
        } else if (existingAssignment != null) {
            itemName = existingAssignment.getName();
        } else if (editItemName.getText() != null) {
            itemName = editItemName.getText().toString().trim();
        }

        ConfirmDialog.showDeleteItem(this, itemName, () -> {
            InMemoryRepository repo = InMemoryRepository.getInstance();
            int toastResId;
            if (TYPE_EXAM.equals(itemType)) {
                repo.deleteExam(itemId);
                toastResId = R.string.msg_exam_deleted;
            } else if (TYPE_TASK.equals(itemType)) {
                repo.deleteTask(itemId);
                toastResId = R.string.msg_task_deleted;
            } else {
                repo.deleteAssignment(itemId);
                toastResId = R.string.msg_assignment_deleted;
            }
            Toast.makeText(this, toastResId, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.picker_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            if (selection != null) {
                // Konversi UTC selection ke string format yyyy-MM-dd
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String formattedDate = sdf.format(new Date(selection));
                editItemDate.setText(formattedDate);
                layoutItemDate.setError(null);
            }
        });

        datePicker.show(getSupportFragmentManager(), "item_date_picker");
    }

    private void showTimePicker() {
        int initialHour = 23;
        int initialMinute = 59;

        String currentVal = editItemTime.getText() != null ? editItemTime.getText().toString() : "";
        if (currentVal.contains(":")) {
            try {
                String[] parts = currentVal.split(":");
                initialHour = Integer.parseInt(parts[0].trim());
                initialMinute = Integer.parseInt(parts[1].trim());
            } catch (Exception ignored) {
            }
        }

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(initialHour)
                .setMinute(initialMinute)
                .setTitleText(R.string.picker_time)
                .build();

        picker.addOnPositiveButtonClickListener(v -> {
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", picker.getHour(), picker.getMinute());
            editItemTime.setText(formattedTime);
            layoutItemTime.setError(null);
        });

        picker.show(getSupportFragmentManager(), "item_time_picker");
    }

    private void validateAndSave() {
        String name = editItemName.getText() != null ? editItemName.getText().toString() : "";
        String course = autoItemCourse.getText() != null ? autoItemCourse.getText().toString() : "";
        String date = editItemDate.getText() != null ? editItemDate.getText().toString() : "";
        String time = editItemTime.getText() != null ? editItemTime.getText().toString() : "";
        String location = editItemLocation.getText() != null ? editItemLocation.getText().toString() : "";

        boolean isValid = true;

        // 1. Validasi Nama
        if (!ValidationUtils.isNonEmpty(name)) {
            layoutItemName.setError(getString(R.string.error_name_required));
            isValid = false;
        } else {
            layoutItemName.setError(null);
        }

        // 2. Validasi Mata Kuliah
        if (!ValidationUtils.isNonEmpty(course)) {
            layoutItemCourse.setError(getString(R.string.error_course_required));
            isValid = false;
        } else {
            layoutItemCourse.setError(null);
        }

        // 3. Validasi Tanggal
        if (!ValidationUtils.isNonEmpty(date)) {
            layoutItemDate.setError(getString(R.string.error_date_required));
            isValid = false;
        } else {
            layoutItemDate.setError(null);
        }

        // 4. Validasi Jam
        if (!ValidationUtils.isNonEmpty(time)) {
            layoutItemTime.setError(getString(R.string.error_time_required));
            isValid = false;
        } else {
            layoutItemTime.setError(null);
        }

        if (!isValid) {
            return;
        }

        String fullDateTime = date.trim() + " " + time.trim();
        InMemoryRepository repo = InMemoryRepository.getInstance();

        if (TYPE_EXAM.equals(itemType)) {
            saveExam(repo, name.trim(), course.trim(), fullDateTime, location.trim());
        } else if (TYPE_TASK.equals(itemType)) {
            saveTask(repo, name.trim(), course.trim(), fullDateTime);
        } else {
            saveAssignment(repo, name.trim(), course.trim(), fullDateTime);
        }

        finish();
    }

    private void saveAssignment(InMemoryRepository repo, String name, String course, String dueDateTime) {
        if (isEditMode && existingAssignment != null) {
            existingAssignment.setName(name);
            existingAssignment.setCourse(course);
            existingAssignment.setDueDateTime(dueDateTime);
            repo.updateAssignment(existingAssignment);
            Toast.makeText(this, R.string.msg_assignment_updated, Toast.LENGTH_SHORT).show();
        } else {
            Assignment newAssignment = new Assignment(name, dueDateTime, course, false);
            repo.addAssignment(newAssignment);
            Toast.makeText(this, R.string.msg_assignment_added, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveExam(InMemoryRepository repo, String name, String course, String dateTime, String location) {
        if (isEditMode && existingExam != null) {
            existingExam.setName(name);
            existingExam.setCourse(course);
            existingExam.setDateTime(dateTime);
            existingExam.setLocation(location);
            repo.updateExam(existingExam);
            Toast.makeText(this, R.string.msg_exam_updated, Toast.LENGTH_SHORT).show();
        } else {
            Exam newExam = new Exam(name, dateTime, course, location, false);
            repo.addExam(newExam);
            Toast.makeText(this, R.string.msg_exam_added, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTask(InMemoryRepository repo, String name, String course, String dueDateTime) {
        if (isEditMode && existingTask != null) {
            existingTask.setName(name);
            existingTask.setCourse(course);
            existingTask.setDueDateTime(dueDateTime);
            repo.updateTask(existingTask);
            Toast.makeText(this, R.string.msg_task_updated, Toast.LENGTH_SHORT).show();
        } else {
            WorkTask newTask = new WorkTask(name, dueDateTime, course, false);
            repo.addTask(newTask);
            Toast.makeText(this, R.string.msg_task_added, Toast.LENGTH_SHORT).show();
        }
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
