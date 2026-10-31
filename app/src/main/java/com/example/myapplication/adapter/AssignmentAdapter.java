package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Assignment;
import com.example.myapplication.util.DateFormatUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter RecyclerView untuk menampilkan daftar Tugas.
 * Menangani penanda selesai (checkbox) dengan efek coret dan pemudaran kartu.
 * Sesuai panduan FE-06.
 */
public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Assignment assignment);
    }

    public interface OnStatusChangeListener {
        void onStatusChange(Assignment assignment, boolean isCompleted);
    }

    private final List<Assignment> assignments = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnStatusChangeListener statusChangeListener;

    public AssignmentAdapter() {
    }

    public AssignmentAdapter(List<Assignment> initialList) {
        if (initialList != null) {
            this.assignments.addAll(initialList);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        this.statusChangeListener = listener;
    }

    public void setAssignments(List<Assignment> newAssignments) {
        assignments.clear();
        if (newAssignments != null) {
            assignments.addAll(newAssignments);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment, parent, false);
        return new AssignmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        Assignment item = assignments.get(position);
        holder.bind(item, itemClickListener, statusChangeListener);
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    static class AssignmentViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardAssignment;
        private final MaterialCheckBox checkCompleted;
        private final TextView textAssignmentName;
        private final TextView textAssignmentCourse;
        private final TextView textAssignmentDueDate;

        AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            cardAssignment = itemView.findViewById(R.id.card_assignment);
            checkCompleted = itemView.findViewById(R.id.check_assignment_completed);
            textAssignmentName = itemView.findViewById(R.id.text_assignment_name);
            textAssignmentCourse = itemView.findViewById(R.id.text_assignment_course);
            textAssignmentDueDate = itemView.findViewById(R.id.text_assignment_due_date);
        }

        void bind(Assignment item, OnItemClickListener clickListener, OnStatusChangeListener statusListener) {
            Context context = itemView.getContext();
            textAssignmentName.setText(item.getName());
            textAssignmentCourse.setText(item.getCourse());
            textAssignmentDueDate.setText(context.getString(
                    R.string.status_assignment_due,
                    DateFormatUtils.formatDateTime(item.getDueDateTime())
            ));

            // Atur gaya visual berdasarkan status selesai
            applyCompletionStyle(item.isCompleted());

            // Hindari trigger saat recycle view
            checkCompleted.setOnCheckedChangeListener(null);
            checkCompleted.setChecked(item.isCompleted());

            checkCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setCompleted(isChecked);
                applyCompletionStyle(isChecked);
                if (statusListener != null) {
                    statusListener.onStatusChange(item, isChecked);
                }
            });

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(item);
                }
            });
        }

        private void applyCompletionStyle(boolean isCompleted) {
            if (isCompleted) {
                // Teks dicoret dan tampilan lebih pudar
                textAssignmentName.setPaintFlags(textAssignmentName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                cardAssignment.setAlpha(0.6f);
            } else {
                // Teks normal dan kontras penuh
                textAssignmentName.setPaintFlags(textAssignmentName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                cardAssignment.setAlpha(1.0f);
            }
        }
    }
}

