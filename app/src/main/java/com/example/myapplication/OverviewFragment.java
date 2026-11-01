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

import com.example.myapplication.adapter.OverviewAdapter;
import com.example.myapplication.data.InMemoryRepository;
import com.example.myapplication.model.OverviewItem;
import com.example.myapplication.util.ConfirmDialog;
import com.example.myapplication.util.ItemSorter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Fragment untuk Layar Ringkasan (Overview) yang menampilkan daftar gabungan
 * Tugas, Ujian, dan Pekerjaan. Urutan awal menurut tenggat terdekat.
 * Mendukung centang selesai, klik untuk ubah, tekan lama untuk hapus,
 * dan tombol tambah dengan pilihan jenis item.
 * Sesuai panduan FE-11 (FR-5, US-4, US-8, BR-2).
 */
public class OverviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private View layoutEmptyOverview;
    private FloatingActionButton fabAddOverview;
    private View btnEmptyAddOverview;

    private OverviewAdapter adapter;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_overview);
        layoutEmptyOverview = view.findViewById(R.id.layout_empty_overview);
        fabAddOverview = view.findViewById(R.id.fab_add_overview);
        btnEmptyAddOverview = view.findViewById(R.id.btn_empty_add_overview);

        setupRecyclerView();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new OverviewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Menangani perubahan status selesai (checkbox)
        adapter.setOnStatusChangeListener((item, isCompleted) -> {
            InMemoryRepository repo = InMemoryRepository.getInstance();
            switch (item.getType()) {
                case ASSIGNMENT:
                    repo.setAssignmentCompleted(item.getId(), isCompleted);
                    break;
                case EXAM:
                    repo.setExamCompleted(item.getId(), isCompleted);
                    break;
                case TASK:
                    repo.setTaskCompleted(item.getId(), isCompleted);
                    break;
            }
            loadOverviewItems();
        });

        // Menangani klik baris (membuka ItemFormActivity sesuai jenis item)
        adapter.setOnItemClickListener(item -> {
            String formType = mapToFormType(item.getType());
            Intent intent = ItemFormActivity.newIntent(
                    requireContext(),
                    formType,
                    item.getId()
            );
            startActivity(intent);
        });

        // Menangani tekan lama baris untuk konfirmasi hapus (BR-2)
        adapter.setOnItemLongClickListener(item -> {
            ConfirmDialog.showDeleteItem(requireContext(), item.getName(), () -> {
                deleteItemFromRepo(item);
                loadOverviewItems();
            });
        });
    }

    private void setupListeners() {
        View.OnClickListener addClickListener = v -> showAddItemTypeDialog();
        fabAddOverview.setOnClickListener(addClickListener);
        btnEmptyAddOverview.setOnClickListener(addClickListener);
    }

    /**
     * Menampilkan dialog pilihan jenis item yang ingin ditambahkan.
     * Mendukung tiga jenis: Tugas, Ujian, dan Pekerjaan.
     */
    private void showAddItemTypeDialog() {
        String[] typeOptions = new String[]{
                getString(R.string.type_assignment),
                getString(R.string.type_exam),
                getString(R.string.type_task)
        };

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_choose_item_type_title)
                .setItems(typeOptions, (dialog, which) -> {
                    String formType;
                    switch (which) {
                        case 1:
                            formType = ItemFormActivity.TYPE_EXAM;
                            break;
                        case 2:
                            formType = ItemFormActivity.TYPE_TASK;
                            break;
                        default:
                            formType = ItemFormActivity.TYPE_ASSIGNMENT;
                            break;
                    }
                    Intent intent = ItemFormActivity.newIntent(
                            requireContext(),
                            formType,
                            -1
                    );
                    startActivity(intent);
                })
                .show();
    }

    /**
     * Menghapus item dari repository berdasarkan jenisnya dan menampilkan Toast.
     */
    private void deleteItemFromRepo(OverviewItem item) {
        InMemoryRepository repo = InMemoryRepository.getInstance();
        int toastResId;
        switch (item.getType()) {
            case EXAM:
                repo.deleteExam(item.getId());
                toastResId = R.string.msg_exam_deleted;
                break;
            case TASK:
                repo.deleteTask(item.getId());
                toastResId = R.string.msg_task_deleted;
                break;
            default: // ASSIGNMENT
                repo.deleteAssignment(item.getId());
                toastResId = R.string.msg_assignment_deleted;
                break;
        }
        Toast.makeText(requireContext(), toastResId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Memetakan tipe OverviewItem ke tipe form string di ItemFormActivity.
     */
    private String mapToFormType(OverviewItem.Type type) {
        switch (type) {
            case EXAM:
                return ItemFormActivity.TYPE_EXAM;
            case TASK:
                return ItemFormActivity.TYPE_TASK;
            default:
                return ItemFormActivity.TYPE_ASSIGNMENT;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOverviewItems();
    }

    /**
     * Memuat semua item gabungan dari repository, mengurutkan menurut tenggat terdekat,
     * dan mengelola tampilan daftar vs kondisi kosong.
     */
    public void loadOverviewItems() {
        List<OverviewItem> rawList = InMemoryRepository.getInstance().getAllOverviewItems();

        if (rawList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmptyOverview.setVisibility(View.VISIBLE);
            adapter.setItems(rawList);
            return;
        }

        layoutEmptyOverview.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        // Urutkan menurut tenggat terdekat (default)
        List<OverviewItem> sortedList = ItemSorter.sortOverviewItems(rawList);
        adapter.setItems(sortedList);
    }
}
