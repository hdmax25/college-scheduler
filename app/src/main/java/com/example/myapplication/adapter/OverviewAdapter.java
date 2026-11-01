package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.OverviewItem;
import com.example.myapplication.util.DateFormatUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter RecyclerView untuk menampilkan daftar gabungan Tugas, Ujian, dan Pekerjaan
 * pada Layar Ringkasan (Overview). Tiap baris menampilkan penanda jenis (badge),
 * nama, mata kuliah, tenggat/waktu, lokasi (khusus Ujian), dan checkbox selesai.
 * Mendukung klik baris, tekan lama untuk hapus, serta perubahan status selesai.
 * Sesuai panduan FE-11.
 */
public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.OverviewViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(OverviewItem item);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(OverviewItem item);
    }

    public interface OnStatusChangeListener {
        void onStatusChange(OverviewItem item, boolean isCompleted);
    }

    private final List<OverviewItem> items = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener longClickListener;
    private OnStatusChangeListener statusChangeListener;

    public OverviewAdapter() {
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

    public void setItems(List<OverviewItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_overview, parent, false);
        return new OverviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewViewHolder holder, int position) {
        OverviewItem item = items.get(position);
        holder.bind(item, itemClickListener, longClickListener, statusChangeListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OverviewViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardOverview;
        private final MaterialCheckBox checkCompleted;
        private final TextView textTypeBadge;
        private final TextView textName;
        private final TextView textCourse;
        private final TextView textDateTime;
        private final LinearLayout layoutLocation;
        private final TextView textLocation;

        OverviewViewHolder(@NonNull View itemView) {
            super(itemView);
            cardOverview = itemView.findViewById(R.id.card_overview);
            checkCompleted = itemView.findViewById(R.id.check_overview_completed);
            textTypeBadge = itemView.findViewById(R.id.text_overview_type_badge);
            textName = itemView.findViewById(R.id.text_overview_name);
            textCourse = itemView.findViewById(R.id.text_overview_course);
            textDateTime = itemView.findViewById(R.id.text_overview_datetime);
            layoutLocation = itemView.findViewById(R.id.layout_overview_location);
            textLocation = itemView.findViewById(R.id.text_overview_location);
        }

        void bind(OverviewItem item,
                  OnItemClickListener clickListener,
                  OnItemLongClickListener longListener,
                  OnStatusChangeListener statusListener) {
            Context context = itemView.getContext();

            // 1. Pasang badge jenis item
            bindTypeBadge(context, item.getType());

            // 2. Nama dan mata kuliah
            textName.setText(item.getName());
            textCourse.setText(item.getCourse());

            // 3. Tanggal/Waktu dengan label kontekstual
            String formattedDate = DateFormatUtils.formatDateTime(item.getDateTime());
            if (item.getType() == OverviewItem.Type.EXAM) {
                textDateTime.setText(context.getString(R.string.status_exam_time, formattedDate));
            } else {
                textDateTime.setText(context.getString(R.string.status_assignment_due, formattedDate));
            }

            // 4. Lokasi hanya untuk Ujian
            if (item.getType() == OverviewItem.Type.EXAM
                    && item.getLocation() != null
                    && !item.getLocation().trim().isEmpty()) {
                layoutLocation.setVisibility(View.VISIBLE);
                textLocation.setText(item.getLocation());
            } else {
                layoutLocation.setVisibility(View.GONE);
            }

            // 5. Status selesai (checkbox)
            applyCompletionStyle(item.isCompleted());
            checkCompleted.setOnCheckedChangeListener(null);
            checkCompleted.setChecked(item.isCompleted());

            checkCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                applyCompletionStyle(isChecked);
                if (statusListener != null) {
                    statusListener.onStatusChange(item, isChecked);
                }
            });

            // 6. Klik baris → membuka form ubah
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(item);
                }
            });

            // 7. Tekan lama → konfirmasi hapus (BR-2)
            itemView.setOnLongClickListener(v -> {
                if (longListener != null) {
                    longListener.onItemLongClick(item);
                    return true;
                }
                return false;
            });
        }

        private void bindTypeBadge(Context context, OverviewItem.Type type) {
            int textResId;
            int bgColorResId;
            int textColorResId;
            switch (type) {
                case EXAM:
                    textResId = R.string.type_exam;
                    bgColorResId = R.color.md_theme_light_tertiaryContainer;
                    textColorResId = R.color.md_theme_light_onTertiaryContainer;
                    break;
                case TASK:
                    textResId = R.string.type_task;
                    bgColorResId = R.color.md_theme_light_secondaryContainer;
                    textColorResId = R.color.md_theme_light_onSecondaryContainer;
                    break;
                default: // ASSIGNMENT
                    textResId = R.string.type_assignment;
                    bgColorResId = R.color.md_theme_light_primaryContainer;
                    textColorResId = R.color.md_theme_light_onPrimaryContainer;
                    break;
            }
            textTypeBadge.setText(textResId);
            textTypeBadge.getBackground().setTint(ContextCompat.getColor(context, bgColorResId));
            textTypeBadge.setTextColor(ContextCompat.getColor(context, textColorResId));
        }

        private void applyCompletionStyle(boolean isCompleted) {
            if (isCompleted) {
                textName.setPaintFlags(textName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                cardOverview.setAlpha(0.6f);
            } else {
                textName.setPaintFlags(textName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                cardOverview.setAlpha(1.0f);
            }
        }
    }
}

