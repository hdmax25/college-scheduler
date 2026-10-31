package com.example.myapplication.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utilitas untuk memformat tanggal dan waktu ke dalam format Bahasa Indonesia.
 * Sesuai panduan FE-06.
 */
public class DateFormatUtils {

    private static final Locale LOCALE_ID = Locale.forLanguageTag("id-ID");
    private static final SimpleDateFormat ISO_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private static final SimpleDateFormat DISPLAY_DATETIME_FORMAT = new SimpleDateFormat("d MMM yyyy, HH:mm", LOCALE_ID);
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("d MMM yyyy", LOCALE_ID);

    /**
     * Mengubah string format "yyyy-MM-dd HH:mm" menjadi format tampilan Indonesia "d MMM yyyy, HH:mm".
     * Contoh: "2026-11-05 23:59" -> "5 Nov 2026, 23:59".
     *
     * @param rawDateTime string input tanggal/jam
     * @return string tanggal terformat atau nilai asli bila gagal di-parse
     */
    public static String formatDateTime(String rawDateTime) {
        if (rawDateTime == null || rawDateTime.trim().isEmpty()) {
            return "-";
        }
        String trimmed = rawDateTime.trim();
        try {
            Date date = ISO_DATETIME_FORMAT.parse(trimmed);
            if (date != null) {
                return DISPLAY_DATETIME_FORMAT.format(date);
            }
        } catch (ParseException ignored) {
        }

        try {
            Date date = ISO_DATE_FORMAT.parse(trimmed);
            if (date != null) {
                return DISPLAY_DATE_FORMAT.format(date);
            }
        } catch (ParseException ignored) {
        }

        return trimmed;
    }
}
