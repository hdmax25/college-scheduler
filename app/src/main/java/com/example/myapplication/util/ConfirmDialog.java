package com.example.myapplication.util;

import android.content.Context;

import com.example.myapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Helper dialog konfirmasi yang dapat dipakai ulang di seluruh layar.
 * Memastikan setiap aksi hapus selalu didahului dialog konfirmasi sesuai aturan bisnis BR-2.
 */
public class ConfirmDialog {

    public interface OnConfirmListener {
        void onConfirm();
    }

    /**
     * Menampilkan dialog konfirmasi generik.
     */
    public static void show(Context context, String title, String message, OnConfirmListener onConfirm) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_confirm_delete, (dialog, which) -> {
                    if (onConfirm != null) {
                        onConfirm.onConfirm();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    /**
     * Menampilkan dialog konfirmasi penghapusan kelas.
     */
    public static void showDeleteClass(Context context, String className, OnConfirmListener onConfirm) {
        String message;
        if (className != null && !className.trim().isEmpty()) {
            message = context.getString(R.string.dialog_delete_class_message_formatted, className.trim());
        } else {
            message = context.getString(R.string.dialog_delete_class_message);
        }

        show(context, context.getString(R.string.dialog_delete_class_title), message, onConfirm);
    }

    /**
     * Menampilkan dialog konfirmasi penghapusan item (Tugas, Ujian, Pekerjaan).
     */
    public static void showDeleteItem(Context context, String itemName, OnConfirmListener onConfirm) {
        String message;
        if (itemName != null && !itemName.trim().isEmpty()) {
            message = context.getString(R.string.dialog_delete_item_message_formatted, itemName.trim());
        } else {
            message = context.getString(R.string.dialog_delete_item_message);
        }

        show(context, context.getString(R.string.dialog_delete_item_title), message, onConfirm);
    }
}

