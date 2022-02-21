/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import org.junit.jupiter.api.Test;

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
    }
}

/* */
