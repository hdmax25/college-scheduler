package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.data.InMemoryRepository;
import com.example.myapplication.model.CourseClass;
import com.example.myapplication.util.ConfirmDialog;
import com.example.myapplication.util.ValidationUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Activity Form untuk menambah atau mengubah data Kelas.
 * Memvalidasi input wajib dan urutan waktu sebelum menyimpan ke InMemoryRepository.
 * Mendukung hapus dengan dialog konfirmasi pada mode ubah (FE-10).
 */
public class ClassFormActivity extends AppCompatActivity {

    public static final String EXTRA_CLASS_ID = "extra_class_id";

    private String[] allDays;



    private TextInputLayout layoutClassName;
    private TextInputLayout layoutClassDays;
    private TextInputLayout layoutStartTime;
    private TextInputLayout layoutEndTime;
    private TextInputLayout layoutClassRoom;
    private TextInputLayout layoutClassSection;
    private TextInputLayout layoutClassProfessor;

    private TextInputEditText editClassName;
    private TextInputEditText editClassDays;
    private TextInputEditText editStartTime;
    private TextInputEditText editEndTime;
    private TextInputEditText editClassRoom;
    private TextInputEditText editClassSection;
    private TextInputEditText editClassProfessor;

    private MaterialToolbar toolbar;
    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    private long classId = -1;
    private boolean isEditMode = false;
    private CourseClass existingClass;

    public static Intent newIntent(Context context, long classId) {
        Intent intent = new Intent(context, ClassFormActivity.class);
        intent.putExtra(EXTRA_CLASS_ID, classId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class_form);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_class_form), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        parseIntentData();
        setupListeners();
    }

    private void initViews() {
        allDays = getResources().getStringArray(R.array.days_of_week);
        toolbar = findViewById(R.id.toolbar_class_form);
        layoutClassName = findViewById(R.id.layout_class_name);
        layoutClassDays = findViewById(R.id.layout_class_days);
        layoutStartTime = findViewById(R.id.layout_start_time);
        layoutEndTime = findViewById(R.id.layout_end_time);
        layoutClassRoom = findViewById(R.id.layout_class_room);
        layoutClassSection = findViewById(R.id.layout_class_section);
        layoutClassProfessor = findViewById(R.id.layout_class_professor);

        editClassName = findViewById(R.id.edit_class_name);
        editClassDays = findViewById(R.id.edit_class_days);
        editStartTime = findViewById(R.id.edit_start_time);
        editEndTime = findViewById(R.id.edit_end_time);
        editClassRoom = findViewById(R.id.edit_class_room);
        editClassSection = findViewById(R.id.edit_class_section);
        editClassProfessor = findViewById(R.id.edit_class_professor);

        btnSave = findViewById(R.id.btn_save_class);
        btnCancel = findViewById(R.id.btn_cancel_class);
    }

    private void parseIntentData() {
        if (getIntent() != null && getIntent().hasExtra(EXTRA_CLASS_ID)) {
            classId = getIntent().getLongExtra(EXTRA_CLASS_ID, -1);
        }

        if (classId != -1) {
            existingClass = InMemoryRepository.getInstance().getClassById(classId);
            if (existingClass != null) {
                isEditMode = true;
                toolbar.setTitle(R.string.form_class_title_edit);
                toolbar.inflateMenu(R.menu.menu_form);
                populateExistingData();
                return;
            }
        }

        // Default Mode Tambah
        isEditMode = false;
        toolbar.setTitle(R.string.form_class_title_add);
    }

    private void populateExistingData() {
        editClassName.setText(existingClass.getName());
        editClassDays.setText(existingClass.getDays());
        editStartTime.setText(existingClass.getStartTime());
        editEndTime.setText(existingClass.getEndTime());
        editClassRoom.setText(existingClass.getRoom());
        editClassSection.setText(existingClass.getSection());
        editClassProfessor.setText(existingClass.getProfessor());
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        // Menu hapus di toolbar (hanya aktif saat mode ubah)
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete_form) {
                if (existingClass != null) {
                    ConfirmDialog.showDeleteClass(this, existingClass.getName(), () -> {
                        InMemoryRepository.getInstance().deleteClass(classId);
                        Toast.makeText(this, R.string.msg_class_deleted, Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
                return true;
            }
            return false;
        });

        // Pemilih Hari Kuliah
        View.OnClickListener dayPickerListener = v -> showDaysPickerDialog();
        editClassDays.setOnClickListener(dayPickerListener);
        layoutClassDays.setEndIconOnClickListener(dayPickerListener);

        // Pemilih Jam Mulai & Jam Selesai
        editStartTime.setOnClickListener(v -> showTimePicker(true));
        layoutStartTime.setEndIconOnClickListener(v -> showTimePicker(true));

        editEndTime.setOnClickListener(v -> showTimePicker(false));
        layoutEndTime.setEndIconOnClickListener(v -> showTimePicker(false));

        // Hapus error saat pengguna mulai mengetik
        editClassName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutClassName.setError(null);
            }
        });

        btnSave.setOnClickListener(v -> validateAndSave());
    }

    private void showDaysPickerDialog() {
        String currentText = editClassDays.getText() != null ? editClassDays.getText().toString() : "";
        List<String> selectedList = new ArrayList<>();
        if (!currentText.trim().isEmpty()) {
            String[] parts = currentText.split(",");
            for (String part : parts) {
                selectedList.add(part.trim());
            }
        }

        boolean[] checkedItems = new boolean[allDays.length];
        for (int i = 0; i < allDays.length; i++) {
            checkedItems[i] = selectedList.contains(allDays[i]);
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_select_days)
                .setMultiChoiceItems(allDays, checkedItems, (dialog, which, isChecked) -> {
                    checkedItems[which] = isChecked;
                })
                .setPositiveButton(R.string.action_save, (dialog, which) -> {
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < allDays.length; i++) {
                        if (checkedItems[i]) {
                            if (result.length() > 0) {
                                result.append(", ");
                            }
                            result.append(allDays[i]);
                        }
                    }
                    editClassDays.setText(result.toString());
                    layoutClassDays.setError(null);
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void showTimePicker(boolean isStartTime) {
        int initialHour = 8;
        int initialMinute = 0;

        String currentVal = isStartTime ?
                (editStartTime.getText() != null ? editStartTime.getText().toString() : "") :
                (editEndTime.getText() != null ? editEndTime.getText().toString() : "");

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
                .setTitleText(isStartTime ? R.string.picker_start_time : R.string.picker_end_time)
                .build();

        picker.addOnPositiveButtonClickListener(v -> {
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", picker.getHour(), picker.getMinute());
            if (isStartTime) {
                editStartTime.setText(formattedTime);
                layoutStartTime.setError(null);
            } else {
                editEndTime.setText(formattedTime);
                layoutEndTime.setError(null);
            }
        });

        picker.show(getSupportFragmentManager(), isStartTime ? "start_time_picker" : "end_time_picker");
    }

    private void validateAndSave() {
        String name = editClassName.getText() != null ? editClassName.getText().toString() : "";
        String days = editClassDays.getText() != null ? editClassDays.getText().toString() : "";
        String startTime = editStartTime.getText() != null ? editStartTime.getText().toString() : "";
        String endTime = editEndTime.getText() != null ? editEndTime.getText().toString() : "";
        String room = editClassRoom.getText() != null ? editClassRoom.getText().toString() : "";
        String section = editClassSection.getText() != null ? editClassSection.getText().toString() : "";
        String professor = editClassProfessor.getText() != null ? editClassProfessor.getText().toString() : "";

        boolean isValid = true;

        // 1. Validasi Nama Kelas (Wajib, tidak boleh kosong setelah trim)
        if (!ValidationUtils.isNonEmpty(name)) {
            layoutClassName.setError(getString(R.string.error_name_required));
            isValid = false;
        } else {
            layoutClassName.setError(null);
        }

        // 2. Validasi Hari Kuliah (Wajib minimal satu hari)
        if (!ValidationUtils.isNonEmpty(days)) {
            layoutClassDays.setError(getString(R.string.error_days_required));
            isValid = false;
        } else {
            layoutClassDays.setError(null);
        }

        // 3. Validasi Jam Mulai
        if (!ValidationUtils.isNonEmpty(startTime)) {
            layoutStartTime.setError(getString(R.string.error_date_time_required));
            isValid = false;
        } else {
            layoutStartTime.setError(null);
        }

        // 4. Validasi Jam Selesai
        if (!ValidationUtils.isNonEmpty(endTime)) {
            layoutEndTime.setError(getString(R.string.error_date_time_required));
            isValid = false;
        } else {
            layoutEndTime.setError(null);
        }

        // 5. Validasi Urutan Waktu (Jam selesai harus setelah jam mulai)
        if (ValidationUtils.isNonEmpty(startTime) && ValidationUtils.isNonEmpty(endTime)) {
            if (!ValidationUtils.isTimeAfter(startTime, endTime)) {
                layoutEndTime.setError(getString(R.string.error_end_time_before_start));
                isValid = false;
            }
        }

        if (!isValid) {
            return;
        }

        // Simpan ke repository
        if (isEditMode && existingClass != null) {
            existingClass.setName(name.trim());
            existingClass.setDays(days.trim());
            existingClass.setStartTime(startTime.trim());
            existingClass.setEndTime(endTime.trim());
            existingClass.setRoom(room.trim());
            existingClass.setSection(section.trim());
            existingClass.setProfessor(professor.trim());

            InMemoryRepository.getInstance().updateClass(existingClass);
            Toast.makeText(this, R.string.msg_class_updated, Toast.LENGTH_SHORT).show();
        } else {
            CourseClass newClass = new CourseClass(
                    name.trim(),
                    days.trim(),
                    startTime.trim(),
                    endTime.trim(),
                    room.trim(),
                    section.trim(),
                    professor.trim()
            );

            InMemoryRepository.getInstance().addClass(newClass);
            Toast.makeText(this, R.string.msg_class_added, Toast.LENGTH_SHORT).show();
        }

        finish();
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
