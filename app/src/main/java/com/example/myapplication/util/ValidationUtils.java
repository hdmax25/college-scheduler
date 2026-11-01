package com.example.myapplication.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utilitas validasi input form.
 * Sesuai aturan validasi PRD Bagian 9.1.
 */
public class ValidationUtils {

    /**
     * Memeriksa apakah teks tidak bernilai null dan tidak kosong setelah di-trim.
     */
    public static boolean isNonEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * Memeriksa apakah tanggal dan jam terisi lengkap.
     */
    public static boolean isDateTimeComplete(String date, String time) {
        return isNonEmpty(date) && isNonEmpty(time);
    }

    /**
     * Memeriksa apakah nama mata kuliah valid (wajib dan tidak kosong).
     */
    public static boolean isCourseValid(String course) {
        return isNonEmpty(course);
    }

    /**
     * Memeriksa apakah endTime berada setelah startTime (format HH:mm).
     *
     * @param startTime string jam mulai, contoh: "08:00"
     * @param endTime string jam selesai, contoh: "09:40"
     * @return true jika jam selesai lebih akhir daripada jam mulai
     */
    public static boolean isTimeAfter(String startTime, String endTime) {
        if (!isNonEmpty(startTime) || !isNonEmpty(endTime)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date start = sdf.parse(startTime.trim());
            Date end = sdf.parse(endTime.trim());
            return start != null && end != null && end.after(start);
        } catch (ParseException e) {
            return false;
        }
    }
}
