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


/**
 * HolidaysProviderTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/22 umjammer initial version <br>
 */
class HolidaysProviderTest {

    static final String[] expected = {
        "2022-01-01 元日",
        "2022-01-10 成人の日",
        "2022-02-11 建国記念の日",
        "2022-02-23 天皇誕生日",
        "2022-03-21 春分の日",
        "2022-04-29 昭和の日",
        "2022-05-03 憲法記念日",
        "2022-05-04 みどりの日",
        "2022-05-05 こどもの日",
        "2022-07-18 海の日",
        "2022-08-11 山の日",
        "2022-09-19 敬老の日",
        "2022-09-23 秋分の日",
        "2022-10-10 スポーツの日",
        "2022-11-03 文化の日",
        "2022-11-23 勤労感謝の日",
    };

    @Test
    void test() {
Debug.println("-------- Google Calendar ICal --------");
        HolidaysProvider provider = new GoogleICalHolidaysJaProvider();
        List<HolidaysProvider.Holyday> holidays = provider.holidays(2022);
holidays.stream().sorted().forEach(System.err::println);
        assertArrayEquals(expected, holidays.stream().map(HolidaysProvider.Holyday::toString).toArray());
    }

    @Test
    void test2() {
Debug.println("-------- Google Calendar API --------");
        HolidaysProvider provider = new GoogleCalendarHolidaysJaProvider();
        List<HolidaysProvider.Holyday> holidays = provider.holidays(2022);
holidays.stream().sorted().forEach(System.err::println);
        assertArrayEquals(expected, holidays.stream().map(HolidaysProvider.Holyday::toString).toArray());
    }
}

/* */
