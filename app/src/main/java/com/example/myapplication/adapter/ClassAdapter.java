package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.CourseClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter RecyclerView untuk menampilkan daftar Kelas.
 * Sesuai panduan FE-04 dan FE-10.
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CourseClass courseClass);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(CourseClass courseClass);
    }

    private final List<CourseClass> classes = new ArrayList<>();
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public ClassAdapter() {
    }

    public ClassAdapter(List<CourseClass> initialList) {
        if (initialList != null) {
            this.classes.addAll(initialList);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setClasses(List<CourseClass> newClasses) {
        classes.clear();
        if (newClasses != null) {
            classes.addAll(newClasses);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        CourseClass item = classes.get(position);
        holder.bind(item, clickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder {
        private final TextView textClassName;
        private final TextView textClassSection;
        private final TextView textClassSchedule;
        private final TextView textClassRoom;
        private final TextView textClassProfessor;
        private final View layoutClassRoom;
        private final View layoutClassProfessor;

        ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            textClassName = itemView.findViewById(R.id.text_class_name);
            textClassSection = itemView.findViewById(R.id.text_class_section);
            textClassSchedule = itemView.findViewById(R.id.text_class_schedule);
            textClassRoom = itemView.findViewById(R.id.text_class_room);
            textClassProfessor = itemView.findViewById(R.id.text_class_professor);
            layoutClassRoom = itemView.findViewById(R.id.layout_class_room);
            layoutClassProfessor = itemView.findViewById(R.id.layout_class_professor);
        }

        void bind(CourseClass item, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            Context context = itemView.getContext();
            textClassName.setText(item.getName());

            // Seksi (Section)
            if (item.getSection() != null && !item.getSection().trim().isEmpty()) {
                textClassSection.setVisibility(View.VISIBLE);
                textClassSection.setText(context.getString(R.string.format_section, item.getSection().trim()));
            } else {
                textClassSection.setVisibility(View.GONE);
            }

            // Jadwal (Hari & Jam)
            String days = item.getDays() != null ? item.getDays().trim() : "";
            String start = item.getStartTime() != null ? item.getStartTime().trim() : "";
            String end = item.getEndTime() != null ? item.getEndTime().trim() : "";

            if (!days.isEmpty() && !start.isEmpty() && !end.isEmpty()) {
                textClassSchedule.setText(context.getString(R.string.format_schedule, days, start, end));
            } else if (!days.isEmpty() && !start.isEmpty()) {
                textClassSchedule.setText(days + " • " + start);
            } else if (!days.isEmpty()) {
                textClassSchedule.setText(days);
            } else {
                textClassSchedule.setText("-");
            }

            // Ruang / Lokasi
            if (item.getRoom() != null && !item.getRoom().trim().isEmpty()) {
                layoutClassRoom.setVisibility(View.VISIBLE);
                textClassRoom.setText(item.getRoom().trim());
            } else {
                layoutClassRoom.setVisibility(View.GONE);
            }

            // Dosen
            if (item.getProfessor() != null && !item.getProfessor().trim().isEmpty()) {
                layoutClassProfessor.setVisibility(View.VISIBLE);
                textClassProfessor.setText(item.getProfessor().trim());
            } else {
                layoutClassProfessor.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(item);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    longClickListener.onItemLongClick(item);
                    return true;
                }
                return false;
            });
        }
    }
}
