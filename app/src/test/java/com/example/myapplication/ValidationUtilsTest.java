package com.example.myapplication;

import com.example.myapplication.util.ValidationUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationUtilsTest {

    @Test
    public void testIsNonEmpty() {
        assertTrue(ValidationUtils.isNonEmpty("Pemrograman Bergerak"));
        assertFalse(ValidationUtils.isNonEmpty(""));
        assertFalse(ValidationUtils.isNonEmpty("   "));
        assertFalse(ValidationUtils.isNonEmpty(null));
    }

    @Test
    public void testIsTimeAfter() {
        // Valid: jam selesai lebih akhir dari jam mulai
        assertTrue(ValidationUtils.isTimeAfter("08:00", "09:40"));
        assertTrue(ValidationUtils.isTimeAfter("10:00", "12:30"));
        assertTrue(ValidationUtils.isTimeAfter("00:00", "01:00"));

        // Invalid: jam selesai lebih awal atau sama
        assertFalse(ValidationUtils.isTimeAfter("09:40", "08:00"));
        assertFalse(ValidationUtils.isTimeAfter("08:00", "08:00"));

        // Invalid: null atau format salah
        assertFalse(ValidationUtils.isTimeAfter(null, "09:40"));
        assertFalse(ValidationUtils.isTimeAfter("08:00", null));
        assertFalse(ValidationUtils.isTimeAfter("", ""));
        assertFalse(ValidationUtils.isTimeAfter("invalid", "time"));
    }
}

