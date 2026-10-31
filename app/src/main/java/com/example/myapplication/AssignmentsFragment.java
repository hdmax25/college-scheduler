package com.example.myapplication;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Fragment untuk menampilkan daftar Tugas.
 * Terhubung ke InMemoryRepository, menangani penanda selesai melalui checkbox,
 * dan menampilkan kondisi kosong (empty state).
 */
public class AssignmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View layoutEmptyState;
    private FloatingActionButton fabAddAssignment;
    private View btnEmptyAddAssignment;
    private AssignmentAdapter adapter;

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
        layoutEmptyState = view.findViewById(R.id.layout_empty_assignments);
        fabAddAssignment = view.findViewById(R.id.fab_add_assignment);
        btnEmptyAddAssignment = view.findViewById(R.id.btn_empty_add_assignment);

        setupRecyclerView();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new AssignmentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Menangani perubahan status selesai (checkbox)
        adapter.setOnStatusChangeListener((assignment, isCompleted) -> {
            InMemoryRepository.getInstance().setAssignmentCompleted(assignment.getId(), isCompleted);
        });

        // Menangani klik baris (akan membuka form ubah di FE-07)
        adapter.setOnItemClickListener(assignment -> {
            Toast.makeText(requireContext(), assignment.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupListeners() {
        View.OnClickListener addClickListener = v -> {
            // Callback tombol tambah (akan membuka form tugas di FE-07)
            Toast.makeText(requireContext(), getString(R.string.form_item_title_add), Toast.LENGTH_SHORT).show();
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
     * Memuat daftar tugas dari repository dan mengatur kondisi tampilan (daftar vs empty state).
     */
    public void loadAssignments() {
        List<Assignment> assignments = InMemoryRepository.getInstance().getAllAssignments();
        if (assignments.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
            adapter.setAssignments(assignments);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
            adapter.setAssignments(assignments);
        }
    }
}
