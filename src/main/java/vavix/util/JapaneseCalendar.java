/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
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

import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * Japanese calendar class.
 * <p>
 * In the GregorianCalendar, the months are 0: January, 1: February, ...
 * so the month for getMonth() and set(int, int, int) is 1: January.
 * Supports the new calendar year from Meiji 6 (1873) onwards.
 * </p>
 * <ul>
 * <li> TODO Separation of analysis and formatting systems</li>
 * <li> TODO Implementation before the Meiji period</li>
 * <li> TODO Set functions are not working</li>
 * <li> TODO holiday</li>
 * </li>
 * @author Toshihiko Shirokaze
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.00 020620 Shirokaze initial version <br>
 *          2.00 021119 nsano refine <br>
 */
@Deprecated
public class JapaneseCalendar extends Calendar {

    private static final Logger logger = getLogger(JapaneseCalendar.class.getName());

    /** */
    private CalendarUtilJa current;

    /**
     * The last day of the lunar calendar in the Gregorian calendar is 1872/12/31.
     * In the GregorianCalendar specification,
     *
     * the month must be entered as the number of months in Japan minus 1.
     */
    public static final Calendar lastDayOfLunar = new GregorianCalendar(1872, 11, 31);

    /** Generates the Japanese calendar date for the current time. */
    public JapaneseCalendar() {
        setup(new GregorianCalendar());
    }

    /** Creates a Japanese calendar object from a Gregorian calendar instance. */
    public JapaneseCalendar(GregorianCalendar calendar) {
        setup(calendar);
    }

    /**
     * Creates a Japanese calendar object from the year, month, and day.
     * Since the GregorianCalendar represents January as 0, the number of months
     * in the argument is subtracted by 1 to generate the object.
     *
     * @param year Year in the Gregorian calendar
     * @param month month (1 ~ 12)
     * @param day day
     */
    public JapaneseCalendar(int year, int month, int day) {
        setup(new GregorianCalendar(year, month - 1, day));
    }

    /**
     * Creates a Japanese calendar object from the era name, year, month, and day.
     *
     * @param gengou The alphabetic character representing the era name, either "M", "T", "S", or "H"
     * @param year Imperial era year
     * @param month month (1 ~ 12)
     * @param day day
     */
    public JapaneseCalendar(String gengou, int year, int month, int day) {
        setup(gengou, year, month, day);
    }

    /**
     * Creates a Japanese calendar object from a string.
     * The currently accepted formats are:
     *
     * <pre>
     * GYYMMDD
     * GYY-MM-DD
     * YYYYMMDD
     * YYYY-MM-DD
     * </pre>
     *
     * "G" is the alphabetic character that represents the era name and can be "M", "T", "S", or "H".
     * "-" is the date separator (any character will do) "MM" is the month number, 1 to 12
     *
     * @param dateString Date String
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
     * Creates a Japanese calendar object from the era name, year, month, and day.
     * Since GregorianCalendar represents January as 0, it is generated by subtracting 1
     * from the number of months in the argument.
     *
     * @param gengou The alphabetic character representing the era name, either "M", "T", "S", or "H"
     * @param year Imperial era year
     * @param month month (1 ~ 12)
     * @param day day
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
logger.log(Level.DEBUG, "1: " + toFS(this));
                return;
            }
        }

        throw new IllegalArgumentException(gengou);
    }

    /**
     * @param calendar AD
     */
    private void setup(GregorianCalendar calendar) {
        for (int i = 0; i < utils.size(); i++) {
            CalendarUtilJa util = utils.get(i);
//logger.log(Level.DEBUG, toFS(calendar) + ".before(" + toFS(util.startYear) + "): " + calendar.before(util.startYear));
//logger.log(Level.DEBUG, toFS(calendar) + ".after(" + toFS(util.endYear) + "): " + calendar.after(util.endYear));
            if (!(calendar.before(util.startYear) || calendar.after(util.endYear))) {
                current = util;
logger.log(Level.DEBUG, "2: " + toFS(calendar));
                set(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH), calendar.get(HOUR_OF_DAY), calendar.get(MINUTE), calendar.get(SECOND));
                this.getTime();
logger.log(Level.DEBUG, "2: " + toFS(this));
                return;
            }
        }

        throw new IllegalArgumentException(toFS(calendar));
    }

    /**
     * Gets the era name.
     *
     * @return Era string
     */
    public String getGengou() {
        return current.shortName;
    }

    // ----

    /** */
    private void copyInto(HackedGregorianCalendar calendar) {
        calendar.set(super.internalGet(YEAR), super.internalGet(MONTH), super.internalGet(DATE), super.internalGet(HOUR_OF_DAY), super.internalGet(MINUTE), super.internalGet(SECOND));
logger.log(Level.DEBUG, toFS(calendar) + ": " + Debug.getCallerMethod(5));
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
logger.log(Level.DEBUG, toFS(this));
        }
    }

    /**
     * Adds the specified (signed) amount of time to the given time field, based on the calendar's rules.
     */
    public void add(int field, int amount) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.add(field, amount);
        tmp.copyInto(this);
    }

    /** Overrides Calendar. */
    protected void computeFields() {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.computeFields();
        tmp.copyInto(this);
    }

    /** Overrides Calendar. */
    protected void computeTime() {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.computeTime();
        tmp.copyInto(this);
    }

    /** If the specified field varies, returns the maximum value. */
    public int getGreatestMinimum(int field) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int min = tmp.getGreatestMinimum(field);
        tmp.copyInto(this);
        return min;
    }

    /** Returns the smallest maximum value for the specified field if it varies. */
    public int getLeastMaximum(int field) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int max = tmp.getLeastMaximum(field);
        tmp.copyInto(this);
        return max;
    }

    /**
     * Returns the maximum value for the specified field
     * (for example, 31 for DAY_OF_MONTH in the Gregorian calendar).
     */
    public int getMaximum(int field) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int max = tmp.getMaximum(field);
        tmp.copyInto(this);
        return max;
    }

    /**
     * Returns the minimum value of the specified field
     * (for example, 1 for DAY_OF_MONTH in the Gregorian calendar).
     */
    public int getMinimum(int field) {
logger.log(Level.DEBUG, "here");
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int min = tmp.getMinimum(field);
        tmp.copyInto(this);
        return min;
    }

    /** Determines whether the specified year is a leap year. */
    public boolean isLeapYear(int year) {
logger.log(Level.DEBUG, "here");
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        return tmp.isLeapYear(year);
    }

    /**
     * Adds or subtracts one unit of time up or down in the specified time field without changing the larger field.
     */
    public void roll(int field, boolean up) {
logger.log(Level.DEBUG, "here");
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
     * Set the era name and date.
     *
     * @param gengou Imperial era name
     * @param year Imperial era year
     * @param month month (1 ~ 12)
     * @param day day
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
        /** Era abbreviation */
        String shortName;

        /** Imperial era name */
        String name;

        /** Start date in the Gregorian calendar */
        GregorianCalendar startYear;

        /** Last day in the Gregorian calendar */
        GregorianCalendar endYear;

        /** Difference from the Gregorian calendar */
        int diff;

        /**
         * Checks．
         *
         * @param year Imperial era year
         * @param month month (1 ~ 12)
         * @param day day
         */
        public void check(int year, int month, int day) {
            GregorianCalendar cal = new GregorianCalendar(year + diff, month - 1, day);

            if (cal.before(startYear) || cal.after(endYear)) {
                throw new IllegalArgumentException(shortName + year + "/" + month + "/" + day);
            }
        }

        @SuppressWarnings("unused")
        void debug() {
            logger.log(Level.DEBUG, shortName);
            logger.log(Level.DEBUG, name);
            logger.log(Level.DEBUG, toFS(startYear));
            logger.log(Level.DEBUG, toFS(endYear));
            logger.log(Level.DEBUG, diff);
        }
    }

    /** */
    private static final ResourceBundle rb = ResourceBundle.getBundle("vavi.util.JapaneseCalendar", Locale.JAPAN);

    /** */
    private static final List<CalendarUtilJa> utils = new ArrayList<>();

    /* */
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
logger.log(Level.DEBUG, "calender util: " + i + " not found, break");
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

//logger.log(Level.DEBUG, "---- " + i + " ----");
//util.debug();
                utils.add(util);

                i++;
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage(), e);
        }
    }

    /** */
    public int compareTo(Calendar calendar) {
        return super.compareTo(calendar);
    }
}
