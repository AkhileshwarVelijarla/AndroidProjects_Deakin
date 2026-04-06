package com.example.a41_personaleventplannerapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.a41_personaleventplannerapp.util.EventValidator;

import org.junit.Test;

public class EventValidatorTest {

    @Test
    public void titleValidationRejectsBlankTitles() {
        assertFalse(EventValidator.isTitleValid("   "));
        assertTrue(EventValidator.isTitleValid("Doctor Appointment"));
    }

    @Test
    public void dateValidationRejectsPastDates() {
        assertTrue(EventValidator.isDateInPast(System.currentTimeMillis() - 1000));
        assertFalse(EventValidator.isDateInPast(System.currentTimeMillis() + 1000));
    }
}
