/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import vavi.util.Debug;
import vavi.util.Locales;


/**
 * CalclatedHolidaysJaProvider.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 021119 nsano port <br>
 */
@Locales(countries = "Japan", languages = "Japanese")
public class CalculatedHolidaysJaProvider implements HolidaysProvider {

    /** this cause Y10K problem lol  */
    private static final int MoY = 9999;

    enum Holidays {
        _1_1(1, 1, "元日", 1948, MoY) { int day(int year) { return day; }},
        _1_15(1, 15, "成人の日", 1948, 2000) { int day(int year) { return day; }},
        _1_W2M(1, 0, "成人の日", 2000, MoY) { int day(int year) { return monday(year, 1, 2); }},
        _2_11(2, 11, "建国記念の日", 1966, MoY) { int day(int year) { return day; }},
        _2_23(2, 23, "天皇誕生日", 2020, MoY) { int day(int year) { return day; }},
        _3_E1(3, 0, "春分の日", 1948, MoY) { int day(int year) { return equinoxes(year)[0]; }},
        _4_29_1(4, 29, "天皇誕生日", 1949, 1989) { int day(int year) { return day; }},
        _4_29_2(4, 29, "みどりの日", 1989, 2007) { int day(int year) { return day; }},
        _4_29_3(4, 29, "昭和の日", 2007, MoY) { int day(int year) { return day; }},
        _5_3(5, 3, "憲法記念日", 1948, MoY) { int day(int year) { return day; }},
        _5_4(5, 4, "みどりの日", 2007, MoY) { int day(int year) { return day; }},
        _5_5(5, 5, "こどもの日", 1948, MoY) { int day(int year) { return day; }},
        _7_20(7, 20, "海の日", 1996, 2003) { int day(int year) { return day; }},
        _7_W3M(7, 0, "海の日", 2003, MoY) { int day(int year) { return monday(year, 7, 3); }},
        _8_11(8, 11, "山の日", 2016, MoY) { int day(int year) { return day; }},
        _9_15(9, 15, "敬老の日", 1966, 2003) { int day(int year) { return day; }},
        _9_W3M(9, 0, "敬老の日", 2003, MoY) { int day(int year) { return monday(year, 9, 3); }},
        _9_E2(9, 0, "秋分の日", 1948, MoY) { int day(int year) { return equinoxes(year)[1]; }},
        _10_10(10, 10, "体育の日", 1966, 2000) { int day(int year) { return day; }},
        _10_W2M(10, 0, "スポーツの日", 2000, MoY) { int day(int year) { return monday(year, 10, 2); }},
        _11_3(11, 3, "文化の日", 1946, MoY) { int day(int year) { return day; }},
        _11_23(11, 23, "勤労感謝の日", 1947, MoY) { int day(int year) { return day; }},
        _12_23(2, 23, "天皇誕生日", 1989, 2020) { int day(int year) { return day; }};
        int month;
        int day;
        String summary;
        /** include self, use &gt;= */
        int from;
        /** not include self, use &lt; */
        int to;
        Holidays(int month, int day, String summary, int from, int to) {
            this.month = month;
            this.day = day;
            this.summary = summary;
            this.from = from;
            this.to = to;
        }
        abstract int day(int year);
        /** @return null when year is out of range */
        LocalDate getDate(int year) {
            if (from <= year && year < to) {
                return LocalDate.of(year, month, day(year));
            } else {
                return null;
            }
        }
        private static final int[][] WnM = {
            { 7, 6, 5, 4, 3, 2, 1 },
            { 14, 13, 12, 11, 10, 9, 8 },
            { 21, 20, 19, 18, 17, 16, 15 },
            { 28, 27, 26, 25, 24, 23, 22 },
        };
        private static int monday(int year, int mm, int week) {
            LocalDate calendar = LocalDate.of(year, mm, 1);
            DayOfWeek dow = calendar.getDayOfWeek();
            return WnM[week - 1][dow.ordinal() - 1];
        }
        // @see "https://kinsentansa.blogspot.com/2011/02/2012-11622.html"
        private static int[] equinoxes(int year) {
            if (year > 2099) {
                Debug.println(Level.WARNING, "return value is not correct when year > 2099");
            }
            int unit = 1000000;
            int val = year - 1980;
            int springEquinox = (20843100 + (242194 * val)) / unit - (val / 4);
            int autumnEquinox = (23248800 + (242194 * val)) / unit - (val / 4);
            return new int[] { springEquinox, autumnEquinox };
        }
    }

    @Override
    public List<Holyday> holidays(int year) {
        List<Holyday> holydays = new ArrayList<>();
        Arrays.stream(Holidays.values()).forEach(e -> {
            LocalDate date = e.getDate(year);
            if (date != null) {
                holydays.add(new Holyday(date, e.summary));
                if (year >= 1973 && date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    holydays.add(new Holyday(date.withDayOfMonth(date.getDayOfMonth() + 1), "振替休日"));
                }
            }
        });

        return holydays;
    }
}

/* */
