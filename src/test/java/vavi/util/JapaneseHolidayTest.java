/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.time.DateTimeException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * JapaneseHolidayTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/22 umjammer initial version <br>
 */
class JapaneseHolidayTest {

    @Test
    void test() {
        JapaneseHoliday jh = new JapaneseHoliday();
        assertTrue(jh.isHoliday(2001, 4, 29));

        assertThrows(DateTimeException.class, () -> {
            JapaneseHoliday jh2 = new JapaneseHoliday();
            assertFalse(jh2.isHoliday(2022, 2, 29));
        });
    }
}

/* */
