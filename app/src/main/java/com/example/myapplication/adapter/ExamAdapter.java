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
import com.example.myapplication.model.Exam;
import com.example.myapplication.util.DateFormatUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter RecyclerView untuk menampilkan daftar Ujian.
 * Menangani waktu ujian, lokasi ujian, penanda selesai (checkbox) sesuai BR-5,
 * serta klik baris dan tekan lama untuk aksi hapus (BR-2).
 * Sesuai panduan FE-08 dan FE-10.
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Exam exam);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Exam exam);
    }

    public interface OnStatusChangeListener {
        void onStatusChange(Exam exam, boolean isCompleted);
    }

    private final List<Exam> exams = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener longClickListener;
    private OnStatusChangeListener statusChangeListener;

    public ExamAdapter() {
    }

    public ExamAdapter(List<Exam> initialList) {
        if (initialList != null) {
            this.exams.addAll(initialList);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        this.statusChangeListener = listener;
    }

    public void setExams(List<Exam> newExams) {
        exams.clear();
        if (newExams != null) {
            exams.addAll(newExams);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam item = exams.get(position);
        holder.bind(item, itemClickListener, longClickListener, statusChangeListener);
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardExam;
        private final MaterialCheckBox checkCompleted;
        private final TextView textExamName;
        private final TextView textExamCourse;
        private final TextView textExamDateTime;
        private final TextView textExamLocation;
        private final View layoutExamLocation;

        ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            cardExam = itemView.findViewById(R.id.card_exam);
            checkCompleted = itemView.findViewById(R.id.check_exam_completed);
            textExamName = itemView.findViewById(R.id.text_exam_name);
            textExamCourse = itemView.findViewById(R.id.text_exam_course);
            textExamDateTime = itemView.findViewById(R.id.text_exam_date_time);
            textExamLocation = itemView.findViewById(R.id.text_exam_location);
            layoutExamLocation = itemView.findViewById(R.id.layout_exam_location);
        }

        void bind(Exam item,
                  OnItemClickListener clickListener,
                  OnItemLongClickListener longListener,
                  OnStatusChangeListener statusListener) {
            Context context = itemView.getContext();
            textExamName.setText(item.getName());
            textExamCourse.setText(item.getCourse());
            textExamDateTime.setText(context.getString(
                    R.string.status_exam_time,
                    DateFormatUtils.formatDateTime(item.getDateTime())
            ));

            // Lokasi Ujian (Opsional)
            if (item.getLocation() != null && !item.getLocation().trim().isEmpty()) {
                layoutExamLocation.setVisibility(View.VISIBLE);
                textExamLocation.setText(item.getLocation().trim());
            } else {
                layoutExamLocation.setVisibility(View.GONE);
            }

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

            itemView.setOnLongClickListener(v -> {
                if (longListener != null) {
                    longListener.onItemLongClick(item);
                    return true;
                }
                return false;
            });
        }

        private void applyCompletionStyle(boolean isCompleted) {
            if (isCompleted) {
                textExamName.setPaintFlags(textExamName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                cardExam.setAlpha(0.6f);
            } else {
                textExamName.setPaintFlags(textExamName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                cardExam.setAlpha(1.0f);
            }
        }
    }
}
