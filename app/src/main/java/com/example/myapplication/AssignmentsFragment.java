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

import com.example.myapplication.adapter.AssignmentAdapter;
import com.example.myapplication.data.InMemoryRepository;
import com.example.myapplication.model.Assignment;
import com.example.myapplication.util.ConfirmDialog;
import com.example.myapplication.util.ItemSorter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Fragment untuk menampilkan daftar Tugas dengan kontrol pengurutan (Sort), penyaringan (Filter),
 * dan penghapusan dengan dialog konfirmasi (BR-2).
 * Sesuai panduan FE-06, FE-09, dan FE-10.
 */
public class AssignmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View layoutEmptyAssignments;
    private View layoutEmptyFilter;
    private FloatingActionButton fabAddAssignment;
    private View btnEmptyAddAssignment;
    private View btnResetFilter;

    private Chip chipSort;
    private ChipGroup chipGroupFilter;

    private AssignmentAdapter adapter;

    private ItemSorter.SortOption currentSort = ItemSorter.SortOption.DATE;
    private ItemSorter.FilterOption currentFilter = ItemSorter.FilterOption.ALL;

    public AssignmentsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assignments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_assignments);
        layoutEmptyAssignments = view.findViewById(R.id.layout_empty_assignments);
        layoutEmptyFilter = view.findViewById(R.id.layout_empty_filter_assignment);
        fabAddAssignment = view.findViewById(R.id.fab_add_assignment);
        btnEmptyAddAssignment = view.findViewById(R.id.btn_empty_add_assignment);
        btnResetFilter = view.findViewById(R.id.btn_reset_filter_assignment);

        chipSort = view.findViewById(R.id.chip_sort_assignment);
        chipGroupFilter = view.findViewById(R.id.chip_group_filter_assignment);

        setupRecyclerView();
        setupSortAndFilterControls();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new AssignmentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Menangani perubahan status selesai (checkbox)
        adapter.setOnStatusChangeListener((assignment, isCompleted) -> {
            InMemoryRepository.getInstance().setAssignmentCompleted(assignment.getId(), isCompleted);
            loadAssignments();
        });

        // Menangani klik baris (membuka ItemFormActivity dalam mode ubah)
        adapter.setOnItemClickListener(assignment -> {
            Intent intent = ItemFormActivity.newIntent(
                    requireContext(),
                    ItemFormActivity.TYPE_ASSIGNMENT,
                    assignment.getId()
            );
            startActivity(intent);
        });

        // Mode Hapus: tekan lama memunculkan dialog konfirmasi (BR-2)
        adapter.setOnItemLongClickListener(assignment -> {
            ConfirmDialog.showDeleteItem(requireContext(), assignment.getName(), () -> {
                InMemoryRepository.getInstance().deleteAssignment(assignment.getId());
                Toast.makeText(requireContext(), R.string.msg_assignment_deleted, Toast.LENGTH_SHORT).show();
                loadAssignments();
            });
        });
    }

    private void setupSortAndFilterControls() {
        updateSortChipText();

        chipSort.setOnClickListener(v -> showSortDialog());

        chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_filter_incomplete_assignment)) {
                currentFilter = ItemSorter.FilterOption.INCOMPLETE;
            } else if (checkedIds.contains(R.id.chip_filter_completed_assignment)) {
                currentFilter = ItemSorter.FilterOption.COMPLETED;
            } else {
                currentFilter = ItemSorter.FilterOption.ALL;
            }
            loadAssignments();
        });

        btnResetFilter.setOnClickListener(v -> {
            chipGroupFilter.check(R.id.chip_filter_all_assignment);
        });
    }

    private void showSortDialog() {
        String[] options = new String[]{
                getString(R.string.sort_by_name),
                getString(R.string.sort_by_due_date),
                getString(R.string.sort_by_course)
        };

        int checkedItem = 1; // Default DATE
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
                    loadAssignments();
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
            label = getString(R.string.sort_by_due_date);
        }
        chipSort.setText(getString(R.string.sort_chip_format, label));
    }

    private void setupListeners() {
        View.OnClickListener addClickListener = v -> {
            Intent intent = ItemFormActivity.newIntent(
                    requireContext(),
                    ItemFormActivity.TYPE_ASSIGNMENT,
                    -1
            );
            startActivity(intent);
        };

        fabAddAssignment.setOnClickListener(addClickListener);
        btnEmptyAddAssignment.setOnClickListener(addClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAssignments();
    }

    /**
     * Memuat daftar tugas dari repository, menerapkan urut dan filter,
     * serta menangani perbedaan kondisi: daftar tampil vs belum ada data vs hasil filter kosong.
     */
    public void loadAssignments() {
        List<Assignment> rawList = InMemoryRepository.getInstance().getAllAssignments();

        if (rawList.isEmpty()) {
            // Kondisi 1: Belum ada data sama sekali di repository
            recyclerView.setVisibility(View.GONE);
            layoutEmptyAssignments.setVisibility(View.VISIBLE);
            layoutEmptyFilter.setVisibility(View.GONE);
            adapter.setAssignments(rawList);
            return;
        }

        layoutEmptyAssignments.setVisibility(View.GONE);

        // Terapkan filter dan sort di lapisan UI
        List<Assignment> displayList = ItemSorter.filterAndSortAssignments(rawList, currentFilter, currentSort);

        if (displayList.isEmpty()) {
            // Kondisi 2: Ada data di repository, tetapi tidak ada yang cocok dengan filter aktif
            recyclerView.setVisibility(View.GONE);
            layoutEmptyFilter.setVisibility(View.VISIBLE);
            adapter.setAssignments(displayList);
        } else {
            // Kondisi 3: Ada item yang cocok ditampilkan
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmptyFilter.setVisibility(View.GONE);
            adapter.setAssignments(displayList);
        }
    }
}
