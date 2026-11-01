package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ExamAdapter;
import com.example.myapplication.data.InMemoryRepository;
import com.example.myapplication.model.Exam;
import com.example.myapplication.util.ConfirmDialog;
import com.example.myapplication.util.ItemSorter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Fragment untuk menampilkan daftar Ujian dengan kontrol pengurutan (Sort) dan penyaringan (Filter).
 * Sesuai panduan FE-08, FE-09, dan FE-10.
 */
public class ExamsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View layoutEmptyExams;
    private View layoutEmptyFilter;
    private FloatingActionButton fabAddExam;
    private View btnEmptyAddExam;
    private View btnResetFilter;

    private Chip chipSort;
    private ChipGroup chipGroupFilter;

    private ExamAdapter adapter;

    private ItemSorter.SortOption currentSort = ItemSorter.SortOption.DATE;
    private ItemSorter.FilterOption currentFilter = ItemSorter.FilterOption.ALL;

    public ExamsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exams, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_exams);
        layoutEmptyExams = view.findViewById(R.id.layout_empty_exams);
        layoutEmptyFilter = view.findViewById(R.id.layout_empty_filter_exam);
        fabAddExam = view.findViewById(R.id.fab_add_exam);
        btnEmptyAddExam = view.findViewById(R.id.btn_empty_add_exam);
        btnResetFilter = view.findViewById(R.id.btn_reset_filter_exam);

        chipSort = view.findViewById(R.id.chip_sort_exam);
        chipGroupFilter = view.findViewById(R.id.chip_group_filter_exam);

        setupRecyclerView();
        setupSortAndFilterControls();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new ExamAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Menangani perubahan status selesai (checkbox) sesuai BR-5
        adapter.setOnStatusChangeListener((exam, isCompleted) -> {
            InMemoryRepository.getInstance().setExamCompleted(exam.getId(), isCompleted);
            // Refresh tampilan agar filter aktif langsung menyesuaikan
            loadExams();
        });

        // Menangani klik baris (membuka ItemFormActivity dalam mode ubah ujian)
        adapter.setOnItemClickListener(exam -> {
            Intent intent = ItemFormActivity.newIntent(
                    requireContext(),
                    ItemFormActivity.TYPE_EXAM,
                    exam.getId()
            );
            startActivity(intent);
        });

        // Menangani tekan lama baris untuk konfirmasi hapus sesuai BR-2
        adapter.setOnItemLongClickListener(exam -> {
            ConfirmDialog.showDeleteItem(requireContext(), exam.getName(), () -> {
                InMemoryRepository.getInstance().deleteExam(exam.getId());
                Toast.makeText(requireContext(), R.string.msg_exam_deleted, Toast.LENGTH_SHORT).show();
                loadExams();
            });
        });
    }

    private void setupSortAndFilterControls() {
        updateSortChipText();

        chipSort.setOnClickListener(v -> showSortDialog());

        chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_filter_incomplete_exam)) {
                currentFilter = ItemSorter.FilterOption.INCOMPLETE;
            } else if (checkedIds.contains(R.id.chip_filter_completed_exam)) {
                currentFilter = ItemSorter.FilterOption.COMPLETED;
            } else {
                currentFilter = ItemSorter.FilterOption.ALL;
            }
            loadExams();
        });

        btnResetFilter.setOnClickListener(v -> {
            chipGroupFilter.check(R.id.chip_filter_all_exam);
        });
    }

    private void showSortDialog() {
        String[] options = new String[]{
                getString(R.string.sort_by_name),
                getString(R.string.sort_by_exam_time),
                getString(R.string.sort_by_course)
        };

        int checkedItem = 1; // Default DATE (Waktu Ujian)
        if (currentSort == ItemSorter.SortOption.NAME) {
            checkedItem = 0;
        } else if (currentSort == ItemSorter.SortOption.COURSE) {
            checkedItem = 2;
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.sort_title)
                .setSingleChoiceItems(options, checkedItem, (dialog, which) -> {
                    if (which == 0) {
                        currentSort = ItemSorter.SortOption.NAME;
                    } else if (which == 1) {
                        currentSort = ItemSorter.SortOption.DATE;
                    } else if (which == 2) {
                        currentSort = ItemSorter.SortOption.COURSE;
                    }
                    updateSortChipText();
                    loadExams();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    private void updateSortChipText() {
        String label;
        if (currentSort == ItemSorter.SortOption.NAME) {
            label = getString(R.string.sort_by_name);
        } else if (currentSort == ItemSorter.SortOption.COURSE) {
            label = getString(R.string.sort_by_course);
        } else {
            label = getString(R.string.sort_by_exam_time);
        }
        chipSort.setText(getString(R.string.sort_chip_format, label));
    }

    private void setupListeners() {
        View.OnClickListener addClickListener = v -> {
            Intent intent = ItemFormActivity.newIntent(
                    requireContext(),
                    ItemFormActivity.TYPE_EXAM,
                    -1
            );
            startActivity(intent);
        };

        fabAddExam.setOnClickListener(addClickListener);
        btnEmptyAddExam.setOnClickListener(addClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExams();
    }

    /**
     * Memuat daftar ujian dari repository, menerapkan urut dan filter,
     * serta menangani perbedaan kondisi: daftar tampil vs belum ada data vs hasil filter kosong.
     */
    public void loadExams() {
        List<Exam> rawList = InMemoryRepository.getInstance().getAllExams();

        // 1. Jika belum ada ujian sama sekali (data kosong)
        if (rawList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmptyFilter.setVisibility(View.GONE);
            layoutEmptyExams.setVisibility(View.VISIBLE);
            adapter.setExams(rawList);
            return;
        }

        layoutEmptyExams.setVisibility(View.GONE);

        // 2. Terapkan filter dan sort
        List<Exam> displayList = ItemSorter.filterAndSortExams(rawList, currentFilter, currentSort);

        // 3. Periksa apakah hasil filter kosong padahal data ada
        if (displayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmptyFilter.setVisibility(View.VISIBLE);
            adapter.setExams(displayList);
        } else {
            layoutEmptyFilter.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setExams(displayList);
        }
    }
}
