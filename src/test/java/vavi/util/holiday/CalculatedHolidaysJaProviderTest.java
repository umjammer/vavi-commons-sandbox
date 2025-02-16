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

    static final String[] expected2023 = {
            "2023-01-01 元日",
            "2023-01-02 振替休日",
            "2023-01-09 成人の日",
            "2023-02-11 建国記念の日",
            "2023-02-23 天皇誕生日",
            "2023-03-21 春分の日",
            "2023-04-29 昭和の日",
            "2023-05-03 憲法記念日",
            "2023-05-04 みどりの日",
            "2023-05-05 こどもの日",
            "2023-07-17 海の日",
            "2023-08-11 山の日",
            "2023-09-18 敬老の日",
            "2023-09-23 秋分の日",
            "2023-10-09 スポーツの日",
            "2023-11-03 文化の日",
            "2023-11-23 勤労感謝の日",
    };

    @Test
    void test() {
        CalculatedHolidaysJaProvider provider = new CalculatedHolidaysJaProvider();
        List<HolidaysProvider.Holiday> holidays = provider.holidays(2023);
Debug.println("holidays: " + holidays.size());
        assertNotEquals(0, holidays.size());
holidays.forEach(System.err::println);
        assertArrayEquals(expected2023, holidays.stream().map(HolidaysProvider.Holiday::toString).toArray());
    }
}
