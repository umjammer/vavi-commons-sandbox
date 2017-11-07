/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;


/**
 * 和暦クラス.
 * <p>
 * GregorianCalendarは，0: 1月, 1: 2月, ... であるので，
 * getMonth(), set(int, int, int) の月が，1: 1月 になるようにしている。
 * 新暦である明治6年(1873年)以降をサポート???。
 * </p>
 * <li> TODO 解析，フォーマット系の分離
 * <li> TODO 明治以前の実装
 * <li> TODO set 系が機能していない
 *
 * @author 城風敏彦
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 1.00 020620 城風 initial version <br>
 *          2.00 021119 nsano refine <br>
 */
public class JapaneseCalendar extends Calendar {

    /** */
    private CalendarUtilJa current;

    /**
     * 西暦での旧暦の最終日 1872/12/31 GregorianCalendar の仕様で，
     * 日本でいう月数に 1 を引いた値を月に入れる 必要がある
     */
    public static final Calendar lastDayOfLunar = new GregorianCalendar(1872, 11, 31);

    /** 現時刻の和暦を生成します． */
    public JapaneseCalendar() {
        setup(new GregorianCalendar());
    }

    /** 西暦インスタンスから和暦オブジェクトを生成します． */
    public JapaneseCalendar(GregorianCalendar calendar) {
        setup(calendar);
    }

    /**
     * 年、月、日から和暦オブジェクトを生成します． GregorianCalendar は，
     * 1 月が 0 で表されるため，引数の月数から 1 を
     * 引いて生成
     *
     * @param year 西暦の年
     * @param month 月 (1 ~ 12)
     * @param day 日
     */
    public JapaneseCalendar(int year, int month, int day) {
        setup(new GregorianCalendar(year, month - 1, day));
    }

    /**
     * 元号、年、月、日から和暦オブジェクトを生成します．
     *
     * @param gengou 元号をあらわすアルファベット文字で "M", "T", "S", "H" のいずれか
     * @param year 元号の年
     * @param month 月 (1 ~ 12)
     * @param day 日
     */
    public JapaneseCalendar(String gengou, int year, int month, int day) {
        setup(gengou, year, month, day);
    }

    /**
     * 文字列から和暦オブジェクトを生成します．
     * 受け付ける形式は現在のところ以下のとおりです．
     *
     * <pre>
     * GYYMMDD
     * GYY-MM-DD
     * YYYYMMDD
     * YYYY-MM-DD
     * </pre>
     *
     * "G" は元号をあらわすアルファベット文字で "M", "T", "S", "H" のいずれか
     * "-" は日付区切り文字(何でも良い) "MM"
     * は月を表す数字で 1 〜 12
     *
     * @param dateString 日付文字列
     */
    public JapaneseCalendar(String dateString) {
        int length = dateString.length();

        if (length == 7) { // GYYMMDD
            String gengou = dateString.substring(0, 1);
            int year = Integer.parseInt(dateString.substring(1, 3));
            int month = Integer.parseInt(dateString.substring(3, 5));
            int day = Integer.parseInt(dateString.substring(5, 7));

            setup(gengou, year, month, day);
        } else if (length == 9) { // GYY-MM-DD
            String gengou = dateString.substring(0, 1);
            int year = Integer.parseInt(dateString.substring(1, 3));
            int month = Integer.parseInt(dateString.substring(4, 6));
            int day = Integer.parseInt(dateString.substring(7, 9));

            setup(gengou, year, month, day);
        } else if (length == 8) { // YYYYMMDD
            int year = Integer.parseInt(dateString.substring(0, 4));
            int month = Integer.parseInt(dateString.substring(4, 6));
            int day = Integer.parseInt(dateString.substring(6, 8));

            setup(new GregorianCalendar(year, month - 1, day));
        } else if (length == 10) { // YYYY-MM-DD
            int year = Integer.parseInt(dateString.substring(0, 4));
            int month = Integer.parseInt(dateString.substring(5, 7));
            int day = Integer.parseInt(dateString.substring(8, 10));

            setup(new GregorianCalendar(year, month - 1, day));
        } else {
            throw new IllegalArgumentException(dateString);
        }
    }

    /**
     * 元号、年、月、日から和暦オブジェクトを生成します． GregorianCalendar は，1 月が 0 で表されるため，引数の月数から 1 を
     * 引いて生成
     *
     * @param gengou 元号をあらわすアルファベット文字で "M", "T", "S", "H" のいずれか
     * @param year 元号の年
     * @param month 月 (1 ~ 12)
     * @param day 日
     */
    private void setup(String gengou, int year, int month, int day) {

        if (year < 0) {
            throw new IllegalArgumentException("year: " + year);
        }

        for (int i = 0; i < utils.size(); i++) {
            CalendarUtilJa util = utils.get(i);
            if (gengou.equals(util.shortName)) {
                util.check(year, month, day);
                current = util;
                set(year + util.diff, month - 1, day);
Debug.println("1: " + toFS(this));
                return;
            }
        }

        throw new IllegalArgumentException(gengou);
    }

    /**
     * @param calendar 西暦
     */
    private void setup(GregorianCalendar calendar) {
        for (int i = 0; i < utils.size(); i++) {
            CalendarUtilJa util = utils.get(i);
//Debug.println(toFS(calendar) + ".before(" + toFS(util.startYear) + "): " + calendar.before(util.startYear));
//Debug.println(toFS(calendar) + ".after(" + toFS(util.endYear) + "): " + calendar.after(util.endYear));
            if (!(calendar.before(util.startYear) || calendar.after(util.endYear))) {
                current = util;
Debug.println("2: " + toFS(calendar));
                set(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH), calendar.get(HOUR_OF_DAY), calendar.get(MINUTE), calendar.get(SECOND));
                this.getTime();
Debug.println("2: " + toFS(this));
                return;
            }
        }

        throw new IllegalArgumentException(toFS(calendar));
    }

    /**
     * 元号を取得します．
     *
     * @return 元号の文字列
     */
    public String getGengou() {
        return current.shortName;
    }

    // ----

    /** */
    private void copyInto(HackedGregorianCalendar calendar) {
        calendar.set(super.internalGet(YEAR), super.internalGet(MONTH), super.internalGet(DATE), super.internalGet(HOUR_OF_DAY), super.internalGet(MINUTE), super.internalGet(SECOND));
Debug.println(toFS(calendar) + ": " + Debug.getCallerMethod(5));
    }

    /** */
    private class HackedGregorianCalendar extends GregorianCalendar {
        public void computeFields() {
            super.computeFields();
        }

        public void computeTime() {
            super.computeTime();
        }

        public void copyInto(Calendar calendar) {
            calendar.set(internalGet(YEAR), internalGet(MONTH), internalGet(DATE), internalGet(HOUR_OF_DAY), internalGet(MINUTE), internalGet(SECOND));
Debug.println(toFS(this));
        }
    }

    /**
     * カレンダの規則に基づいて、指定された (符号付きの) 時間量を、指定された 時間フィールドに加えます。
     */
    public void add(int field, int amount) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.add(field, amount);
        tmp.copyInto(this);
    }

    /** Calendar をオーバーライドします。 */
    protected void computeFields() {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.computeFields();
        tmp.copyInto(this);
    }

    /** Calendar をオーバーライドします。 */
    protected void computeTime() {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.computeTime();
        tmp.copyInto(this);
    }

    /** 指定されたフィールドが変化する場合、その最大値を返します。 */
    public int getGreatestMinimum(int field) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int min = tmp.getGreatestMinimum(field);
        tmp.copyInto(this);
        return min;
    }

    /** 指定されたフィールドが変化する場合、その最小の最大値を返します。 */
    public int getLeastMaximum(int field) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int max = tmp.getLeastMaximum(field);
        tmp.copyInto(this);
        return max;
    }

    /**
     * 指定されたフィールドの最大値 (たとえば、グレゴリオ暦の DAY_OF_MONTH では、31) を返します。
     */
    public int getMaximum(int field) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int max = tmp.getMaximum(field);
        tmp.copyInto(this);
        return max;
    }

    /**
     * 指定されたフィールドの最小値 (たとえば、グレゴリオ暦の DAY_OF_MONTH では、1) を返します。
     */
    public int getMinimum(int field) {
Debug.println("here");
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int min = tmp.getMinimum(field);
        tmp.copyInto(this);
        return min;
    }

    /** 指定された年が、うるう年かどうかを判定します。 */
    public boolean isLeapYear(int year) {
Debug.println("here");
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        return tmp.isLeapYear(year);
    }

    /**
     * 大きいフィールドを変更せずに指定された時間フィールドの 1 つの単位の時間を上または下に加算または減算します。
     */
    public void roll(int field, boolean up) {
Debug.println("here");
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.roll(field, up);
        tmp.copyInto(this);
    }

    /** */
    public Date getLunarChange() {
        return lastDayOfLunar.getTime();
    }

    // ----

    /**
     * 元号，年月日を設定します．
     *
     * @param gengou 元号
     * @param year 元号の年
     * @param month 月 (1 ~ 12)
     * @param day 日
     */
    public void set(String gengou, int year, int month, int day) {
        for (int i = 0; i < utils.size(); i++) {
            CalendarUtilJa util = utils.get(i);
            if (gengou.equals(util.shortName)) {
                current = util;
                set(year + util.diff, month - 1, day);
            }
        }

        throw new IllegalArgumentException(gengou);
    }

    /** 元号 YY 年 MM 年 DD 日の String に変換 */
    public String toString() {
        int year = internalGet(YEAR) - current.diff;
        String result = new String();
        if (year == 1) {
            result = rb.getString("calendar.ja.label.year.first");
        } else {
            result = Integer.toString(year);
        }

        return current.name + result + rb.getString("calendar.ja.label.year") + (internalGet(MONTH) + 1) + rb.getString("calendar.ja.label.month") + internalGet(DAY_OF_MONTH) + rb.getString("calendar.ja.label.day");
    }

    /** for debug */
    static final String toFS(Calendar calendar) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(calendar.getTime());
    }

    /** */
    private static class CalendarUtilJa {
        /** 元号の省略表記 */
        String shortName;

        /** 元号 */
        String name;

        /** 西暦での開始日 */
        GregorianCalendar startYear;

        /** 西暦での最終日 */
        GregorianCalendar endYear;

        /** 西暦との差分 */
        int diff;

        /**
         * します．
         *
         * @param year 元号の年
         * @param month 月 (1 ~ 12)
         * @param day 日
         */
        public void check(int year, int month, int day) {
            GregorianCalendar cal = new GregorianCalendar(year + diff, month - 1, day);

            if (cal.before(startYear) || cal.after(endYear)) {
                throw new IllegalArgumentException(shortName + year + "/" + month + "/" + day);
            }
        }

        @SuppressWarnings("unused")
        void debug() {
            Debug.println(shortName);
            Debug.println(name);
            Debug.println(toFS(startYear));
            Debug.println(toFS(endYear));
            Debug.println(diff);
        }
    }

    /** */
    private static final ResourceBundle rb = ResourceBundle.getBundle("vavi.util.JapaneseCalendar", Locale.JAPAN);

    /** */
    private static final List<CalendarUtilJa> utils = new ArrayList<>();

    /** */
    static {

        try {
            int i = 0;
            while (true) {
                CalendarUtilJa util = new CalendarUtilJa();

                // shortName
                String value;
                try {
                    value = rb.getString("calendar.ja." + i + ".gengou.short");
                } catch (MissingResourceException e) {
Debug.println("calender util: " + i + " not found, break");
                    break;
                }
                util.shortName = value;

                // name
                value = rb.getString("calendar.ja." + i + ".gengou.name");
                util.name = value;

                // startYear
                value = rb.getString("calendar.ja." + i + ".year.start");
                StringTokenizer st = new StringTokenizer(value, " ,");
                int year = Integer.parseInt(st.nextToken());
                int month = Integer.parseInt(st.nextToken());
                int day = Integer.parseInt(st.nextToken());
                util.startYear = new GregorianCalendar(year, month, day);

                // endYear
                value = rb.getString("calendar.ja." + i + ".year.end");
                st = new StringTokenizer(value, " ,");
                year = Integer.parseInt(st.nextToken());
                month = Integer.parseInt(st.nextToken());
                day = Integer.parseInt(st.nextToken());
                util.endYear = new GregorianCalendar(year, month, day);

                value = rb.getString("calendar.ja." + i + ".year.diff");
                util.diff = Integer.parseInt(value);

// Debug.println("---- " + i + " ----");
// util.debug();
                utils.add(util);

                i++;
            }
        } catch (Exception e) {
Debug.printStackTrace(e);
        }
    }

    /** */
    public int compareTo(Calendar calendar) {
        return super.compareTo(calendar);
    }
}

/* */
