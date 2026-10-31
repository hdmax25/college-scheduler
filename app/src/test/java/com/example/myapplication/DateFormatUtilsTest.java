package com.example.myapplication;

import com.example.myapplication.util.DateFormatUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateFormatUtilsTest {

    @Test
    public void testFormatDateTimeValid() {
        String formatted = DateFormatUtils.formatDateTime("2026-11-05 23:59");
        assertTrue(formatted.contains("2026"));
        assertTrue(formatted.contains("23:59"));
        assertTrue(formatted.contains("Nov") || formatted.contains("11"));
    }

    @Test
    public void testFormatDateOnlyValid() {
        String formatted = DateFormatUtils.formatDateTime("2026-11-05");
        assertTrue(formatted.contains("2026"));
        assertTrue(formatted.contains("Nov") || formatted.contains("11"));
    }

    @Test
    public void testFormatDateTimeNullOrEmpty() {
        assertEquals("-", DateFormatUtils.formatDateTime(null));
        assertEquals("-", DateFormatUtils.formatDateTime(""));
        assertEquals("-", DateFormatUtils.formatDateTime("   "));
    }

    @Test
    public void testFormatDateTimeFallback() {
        // Fallback kembalikan input jika tidak cocok dengan pola
        assertEquals("Besok Sore", DateFormatUtils.formatDateTime("Besok Sore"));
    }
}

