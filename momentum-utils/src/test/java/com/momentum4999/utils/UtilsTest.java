package com.momentum4999.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UtilsTest {
    private static final double FLOAT_DELTA = 1e-6;
    @Test
    public void testClip() {
        assertEquals(1, Utils.clip(1.2, -1, 1), FLOAT_DELTA);
        assertEquals(-1, Utils.clip(-1.2, -1, 1), FLOAT_DELTA);
        assertEquals(0.2, Utils.clip(0.2, -1, 1), FLOAT_DELTA);
    }
}
