/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.time.DayOfWeek;
import java.time.LocalDate;


/**
 * 日本における国民の祝日を取得するクラスです。
 *
 * <pre>
 * 1月1日          元旦
 * 1月第2月曜日    成人の日
 * 2月11日         建国記念の日
 * 3月*日          春分の日
 * 4月29日         みどりの日
 * 5月3日          憲法記念日
 * 5月4日          国民の休日
 * 5月5日          こどもの日
 * 7月20日         海の日 (2003年から第三月曜日)
 * 9月15日         敬老の日 (2003年から第三月曜日)
 * 9月*日          秋分の日
 * 10月第2月曜日   体育の日
 * 11月3日         文化の日
 * 11月23日        勤労感謝の日
 * 12月23日        天皇誕生日
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 021119 nsano port <br>
 */
public class JapaneseHoliday {

    /** このオブジェクトが指す日付 */
    private LocalDate calendar;

    /** 土曜日も休日とするかどうか */
    private boolean saturdayHoliday;

    /**
     * デフォルトのタイムゾーンおよびロケールを持つカレンダーを使用します。
     */
    public JapaneseHoliday() {
        this.calendar = LocalDate.now();
    }

    /**
     * 土曜日も休日とするかどうかを設定します。
     * @param sturdayHoliday 土曜日も休日とするなら true、そうでないなら false
     */
    public void setSaturdayHoliday(boolean sturdayHoliday) {
        this.saturdayHoliday = sturdayHoliday;
    }

    /**
     * このインスタンスを作成した時刻の日付が休日かどうかを判定します。
     * @return 休日ならば true、そうでないなら false
     */
    public boolean isHoliday() {
        return isHoliday(calendar.getYear(),
                         calendar.getMonthValue(),
                         calendar.getDayOfMonth());
    }

    /**
     * 指定された年月日が休日かどうか判定します。
     * オブジェクトのカレンダーは指定された日に変更されます。
     * @param yyyy 年、西暦 4 桁で指定
     * @param mm 月 1 ~ 12
     * @param dd 日 1 ~ 31
     * @return 休日ならば true、そうでないなら false
     */
    public boolean isHoliday(int yyyy, int mm, int dd) {
        int[] equinox = getEquinoxDays(yyyy);

        int[] monday = getMondayHoliday(yyyy);
        calendar.withYear(yyyy);
        calendar.withMonth(mm);
        calendar.withDayOfMonth(dd);
        if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return true;
        } else if (saturdayHoliday && calendar.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return true;
        }

        if ((mm == 1 && (dd == 1 || dd == monday[0])) ||
            (mm == 2 && (dd == 11)) ||
            (mm == 3 && (dd == equinox[0])) ||
            (mm == 4 && (dd == 29)) ||
            (mm == 5 && (dd == 3 || dd == 4 || dd == 5)) ||
            (mm == 7 && (dd == monday[1])) ||
            (mm == 9 && (dd == monday[2] || dd == equinox[1])) ||
            (mm == 10 && (dd == monday[3])) ||
            (mm == 11 && (dd == 3 || dd == 23)) ||
            (mm == 12 && (dd == 23))) {
            return true;
        } else {
            if (mm == 2 && dd == 12) {
                calendar.withMonth(1);
                calendar.withDayOfMonth(11);

                if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    return true;
                }
            } else if
                (mm == 3 && dd == (equinox[0] + 1)) {
                calendar.withMonth(2);

                calendar.withDayOfMonth(equinox[0]);
                if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    return true;
                }
            } else if (mm == 4 && dd == 30) {
                calendar.withMonth(3);
                calendar.withDayOfMonth(29);
                if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    return true;
                }
            } else if (mm == 5 && dd == 6) {
                calendar.withMonth(4);
                calendar.withDayOfMonth(5);
                if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    return true;
                }
            } else if (mm == 9 && dd == (equinox[1] + 1)) {
                calendar.withMonth(8);
                calendar.withDayOfMonth(equinox[1]);
                if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    return true;
                }
            } else if (mm == 11 && dd == 4) {
                calendar.withMonth(10);
                calendar.withDayOfMonth(3);
                if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    return true;
                }
            } else if (mm == 11 && dd == 24) {
                calendar.withMonth(10);
                calendar.withDayOfMonth(23);
                if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    return true;
                }
            } else if (mm == 12 && dd == 24) {
                calendar.withMonth(11);
                calendar.withDayOfMonth(23);
                if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    return true;
                }
            } else {
                if (yyyy <= 2002) {
                    if (mm == 7 && dd == (monday[1] + 1)) {
                        calendar.withMonth(6);
                        calendar.withDayOfMonth(monday[1]);
                        if (calendar.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            return true;
                        }
                    } else if (mm == 9 && (dd == monday[2] + 1)) {
                        calendar.withMonth(8);
                        calendar.withDayOfMonth(monday[2]);
                        if (calendar.getDayOfWeek() ==
                            DayOfWeek.SUNDAY) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    /**
     * @param yyyy
     * @return 春分、秋分の日
     */
    private int[] getEquinoxDays(int yyyy) {
        int temp = 0;
        // 一時的な変数 temp = (242194 * (yyyy - 1980));

        temp -= ((yyyy - 1980) >> 2) * 1000000;
        int s3 = (20843100 + temp) / 1000000;
        int s9 = (23248800 + temp) / 1000000;
        return new int[] { s3, s9 };
    }

    /**
     * @param yyyy
     * @return 指定した年の月曜日の休日
     */
    private int[] getMondayHoliday(int yyyy) {
        int h1 = 0;
        int h7 = 0;
        int h9 = 0;
        int h10 = 0;
        // 成人の日 1月第二月曜日 int h7 = 20;
        // 海の日 7月第三月曜日(2003年から) int h9 = 15;
        // 敬老の日 9月第三月曜日(2003年から) int h10 = 0;
        // 体育の日 10月第二月曜日 cal.set(DayOfWeek.YEAR, yyyy);

        calendar.withMonth(1);
        calendar.withDayOfMonth(1);
        DayOfWeek week = calendar.getDayOfWeek();
        switch (week) {
        case SUNDAY:
            h1 = 9;
            break;
        case MONDAY:
            h1 = 8;
            break;
        case TUESDAY:
            h1 = 14;
            break;
        case WEDNESDAY:
            h1 = 13;
            break;
        case THURSDAY:
            h1 = 12;
            break;
        case FRIDAY:
            h1 = 11;
            break;
        case SATURDAY:
            h1 = 10;
            break;
        }

        calendar.withMonth(10);
        week = calendar.getDayOfWeek();
        switch (week) {
        case SUNDAY:
            h10 = 9;
            break;
        case MONDAY:
            h10 = 8;
            break;
        case TUESDAY:
            h10 = 14;
            break;
        case WEDNESDAY:
            h10 = 13;
            break;
        case THURSDAY:
            h10 = 12;
            break;
        case FRIDAY:
            h10 = 11;
            break;
        case SATURDAY:
            h10 = 10;
            break;
        }

        if (yyyy >= 2003) {
            calendar.withMonth(7);
            week = calendar.getDayOfWeek();
            switch (week) {
            case SUNDAY:
                h7 = 16;
                break;
            case MONDAY:
                h7 = 15;
                break;
            case TUESDAY:
                h7 = 21;
                break;
            case WEDNESDAY:
                h7 = 20;
                break;
            case THURSDAY:
                h7 = 19;
                break;
            case FRIDAY:
                h7 = 18;
                break;
            case SATURDAY:
                h7 = 17;
                break;
            }

            calendar.withMonth(9);
            week = calendar.getDayOfWeek();
            switch (week) {
            case SUNDAY:
                h9 = 16;
                break;
            case MONDAY:
                h9 = 15;
                break;
            case TUESDAY:
                h9 = 21;
                break;
            case WEDNESDAY:
                h9 = 20;
                break;
            case THURSDAY:
                h9 = 19;
                break;
            case FRIDAY:
                h9 = 18;
                break;
            case SATURDAY:
                h9 = 17;
                break;
            }
        }

        return new int[] { h1, h7, h9, h10 };
    }
}

/* */
