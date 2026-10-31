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

import com.example.myapplication.adapter.ClassAdapter;
import com.example.myapplication.data.InMemoryRepository;
import com.example.myapplication.model.CourseClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Fragment untuk menampilkan daftar Kelas.
 * Terhubung ke InMemoryRepository untuk membaca data Kelas dan menampilkan kondisi kosong (empty state).
 */
public class ClassesFragment extends Fragment {

    private RecyclerView recyclerView;
    private View layoutEmptyState;
    private FloatingActionButton fabAddClass;
    private View btnEmptyAddClass;
    private ClassAdapter adapter;

    public ClassesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_classes);
        layoutEmptyState = view.findViewById(R.id.layout_empty_classes);
        fabAddClass = view.findViewById(R.id.fab_add_class);
        btnEmptyAddClass = view.findViewById(R.id.btn_empty_add_class);

        setupRecyclerView();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new ClassAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(courseClass -> {
            // Callback klik item kelas (akan dihubungkan ke form ubah di FE-05)
            Toast.makeText(requireContext(), courseClass.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupListeners() {
        View.OnClickListener addClickListener = v -> {
            // Callback tombol tambah (akan membuka form tambah di FE-05)
            Toast.makeText(requireContext(), getString(R.string.form_class_title_add), Toast.LENGTH_SHORT).show();
        };

        fabAddClass.setOnClickListener(addClickListener);
        btnEmptyAddClass.setOnClickListener(addClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadClasses();
    }

    /**
     * Memuat daftar kelas dari repository dan mengatur kondisi tampilan (daftar vs empty state).
     */
    public void loadClasses() {
        List<CourseClass> classes = InMemoryRepository.getInstance().getAllClasses();
        if (classes.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
            adapter.setClasses(classes);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
            adapter.setClasses(classes);
        }
    }
}
