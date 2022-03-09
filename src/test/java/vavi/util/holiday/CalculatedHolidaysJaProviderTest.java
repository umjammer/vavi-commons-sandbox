/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import java.util.List;

import org.junit.jupiter.api.Test;

import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


/**
 * CalculatedHolidaysJaProviderTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/23 umjammer initial version <br>
 */
class CalculatedHolidaysJaProviderTest {

    @Test
    void test() {
        CalculatedHolidaysJaProvider provider = new CalculatedHolidaysJaProvider();
        List<HolidaysProvider.Holyday> holidays = provider.holidays(2022);
Debug.println("holidays: " + holidays.size());
        assertNotEquals(0, holidays.size());
holidays.forEach(System.err::println);
        assertArrayEquals(HolidaysProviderTest.expected, holidays.stream().map(HolidaysProvider.Holyday::toString).toArray());
    }
}

/* */
