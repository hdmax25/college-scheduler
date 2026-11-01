package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ExamAdapter;
import com.example.myapplication.data.InMemoryRepository;
import com.example.myapplication.model.Exam;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Fragment untuk menampilkan daftar Ujian.
 * Terhubung ke InMemoryRepository, menangani penanda selesai melalui checkbox (BR-5),
 * menampilkan lokasi ujian dan kondisi kosong (empty state), serta membuka ItemFormActivity.
 */
public class ExamsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View layoutEmptyState;
    private FloatingActionButton fabAddExam;
    private View btnEmptyAddExam;
    private ExamAdapter adapter;

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
        layoutEmptyState = view.findViewById(R.id.layout_empty_exams);
        fabAddExam = view.findViewById(R.id.fab_add_exam);
        btnEmptyAddExam = view.findViewById(R.id.btn_empty_add_exam);

        setupRecyclerView();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new ExamAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Menangani perubahan status selesai (checkbox) sesuai BR-5
        adapter.setOnStatusChangeListener((exam, isCompleted) -> {
            InMemoryRepository.getInstance().setExamCompleted(exam.getId(), isCompleted);
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
    }

    private void setupListeners() {
        View.OnClickListener addClickListener = v -> {
            // Membuka ItemFormActivity dalam mode tambah ujian baru
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
     * Memuat daftar ujian dari repository dan mengatur kondisi tampilan (daftar vs empty state).
     */
    public void loadExams() {
        List<Exam> exams = InMemoryRepository.getInstance().getAllExams();
        if (exams.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
            adapter.setExams(exams);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
            adapter.setExams(exams);
        }
    }
}
