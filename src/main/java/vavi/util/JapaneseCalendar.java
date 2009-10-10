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
 * �a��N���X.
 * <p>
 * GregorianCalendar�́C0: 1��, 1: 2��, ... �ł���̂ŁC
 * getMonth(), set(int, int, int) �̌����C1: 1�� �ɂȂ�悤�ɂ��Ă���B
 * �V��ł��閾��6�N(1873�N)�ڍs���T�|�[�g???�B
 * </p>
 * <li> TODO ��́C�t�H�[�}�b�g�n�̕���
 * <li> TODO �����ȑO�̎���
 * <li> TODO set �n���@�\���Ă��Ȃ�
 * 
 * @author �镗�q�F
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 1.00 020620 �镗 initial version <br>
 *          2.00 021119 nsano refine <br>
 */
public class JapaneseCalendar extends Calendar {

    /** */
    private CalendarUtilJa current;

    /**
     * ����ł̋���̍ŏI�� 1872/12/31 GregorianCalendar �̎d�l�ŁC
     * ���{�ł��������� 1 ���������l�����ɓ���� �K�v������
     */
    public static final Calendar lastDayOfLunar = new GregorianCalendar(1872, 11, 31);

    /** �������̘a��𐶐����܂��D */
    public JapaneseCalendar() {
        setup(new GregorianCalendar());
    }

    /** ����C���X�^���X����a��I�u�W�F�N�g�𐶐����܂��D */
    public JapaneseCalendar(GregorianCalendar calendar) {
        setup(calendar);
    }

    /**
     * �N�A���A������a��I�u�W�F�N�g�𐶐����܂��D GregorianCalendar �́C
     * 1 ���� 0 �ŕ\����邽�߁C�����̌������� 1 ��
     * �����Đ���
     * 
     * @param year ����̔N
     * @param month �� (1 ~ 12)
     * @param day ��
     */
    public JapaneseCalendar(int year, int month, int day) {
        setup(new GregorianCalendar(year, month - 1, day));
    }

    /**
     * �����A�N�A���A������a��I�u�W�F�N�g�𐶐����܂��D
     * 
     * @param gengou ����������킷�A���t�@�x�b�g������ "M", "T", "S", "H" �̂����ꂩ
     * @param year �����̔N
     * @param month �� (1 ~ 12)
     * @param day ��
     */
    public JapaneseCalendar(String gengou, int year, int month, int day) {
        setup(gengou, year, month, day);
    }

    /**
     * �����񂩂�a��I�u�W�F�N�g�𐶐����܂��D
     * �󂯕t����`���͌��݂̂Ƃ���ȉ��̂Ƃ���ł��D
     * 
     * <pre>
     * GYYMMDD
     * GYY-MM-DD
     * YYYYMMDD
     * YYYY-MM-DD
     * </pre>
     * 
     * "G" �͌���������킷�A���t�@�x�b�g������ "M", "T", "S", "H" �̂����ꂩ
     * "-" �͓��t��؂蕶��(���ł��ǂ�) "MM"
     * �͌���\�������� 1 �` 12
     * 
     * @param dateString ���t������
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
     * �����A�N�A���A������a��I�u�W�F�N�g�𐶐����܂��D GregorianCalendar �́C1 ���� 0 �ŕ\����邽�߁C�����̌������� 1 ��
     * �����Đ���
     * 
     * @param gengou ����������킷�A���t�@�x�b�g������ "M", "T", "S", "H" �̂����ꂩ
     * @param year �����̔N
     * @param month �� (1 ~ 12)
     * @param day ��
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
     * @param calendar ����
     */
    private void setup(GregorianCalendar calendar) {
        for (int i = 0; i < utils.size(); i++) {
            CalendarUtilJa util = utils.get(i);
            // Debug.println(toFS(calendar) + ".before(" + toFS(util.startYear)
            // + "): " + calendar.before(util.startYear));
            // Debug.println(toFS(calendar) + ".after(" + toFS(util.endYear) +
            // "): " + calendar.after(util.endYear));
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
     * �������擾���܂��D
     * 
     * @return �����̕�����
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
     * �J�����_�̋K���Ɋ�Â��āA�w�肳�ꂽ (�����t����) ���ԗʂ��A�w�肳�ꂽ ���ԃt�B�[���h�ɉ����܂��B
     */
    public void add(int field, int amount) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.add(field, amount);
        tmp.copyInto(this);
    }

    /** Calendar ���I�[�o�[���C�h���܂��B */
    protected void computeFields() {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.computeFields();
        tmp.copyInto(this);
    }

    /** Calendar ���I�[�o�[���C�h���܂��B */
    protected void computeTime() {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        tmp.computeTime();
        tmp.copyInto(this);
    }

    /** �w�肳�ꂽ�t�B�[���h���ω�����ꍇ�A���̍ő�l��Ԃ��܂��B */
    public int getGreatestMinimum(int field) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int min = tmp.getGreatestMinimum(field);
        tmp.copyInto(this);
        return min;
    }

    /** �w�肳�ꂽ�t�B�[���h���ω�����ꍇ�A���̍ŏ��̍ő�l��Ԃ��܂��B */
    public int getLeastMaximum(int field) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int max = tmp.getLeastMaximum(field);
        tmp.copyInto(this);
        return max;
    }

    /**
     * �w�肳�ꂽ�t�B�[���h�̍ő�l (���Ƃ��΁A�O���S���I��� DAY_OF_MONTH �ł́A31) ��Ԃ��܂��B
     */
    public int getMaximum(int field) {
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int max = tmp.getMaximum(field);
        tmp.copyInto(this);
        return max;
    }

    /**
     * �w�肳�ꂽ�t�B�[���h�̍ŏ��l (���Ƃ��΁A�O���S���I��� DAY_OF_MONTH �ł́A1) ��Ԃ��܂��B
     */
    public int getMinimum(int field) {
        Debug.println("here");
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        int min = tmp.getMinimum(field);
        tmp.copyInto(this);
        return min;
    }

    /** �w�肳�ꂽ�N���A���邤�N���ǂ����𔻒肵�܂��B */
    public boolean isLeapYear(int year) {
        Debug.println("here");
        HackedGregorianCalendar tmp = new HackedGregorianCalendar();
        copyInto(tmp);
        return tmp.isLeapYear(year);
    }

    /**
     * �傫���t�B�[���h��ύX�����Ɏw�肳�ꂽ���ԃt�B�[���h�� 1 �̒P�ʂ̎��Ԃ���܂��͉��ɉ��Z�܂��͌��Z���܂��B
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
     * �����C�N������ݒ肵�܂��D
     * 
     * @param gengou ����
     * @param year �����̔N
     * @param month �� (1 ~ 12)
     * @param day ��
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

    /** ���� YY �N MM �N DD ���� String �ɕϊ� */
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
        /** �����̏ȗ��\�L */
        String shortName;

        /** ���� */
        String name;

        /** ����ł̊J�n�� */
        GregorianCalendar startYear;

        /** ����ł̍ŏI�� */
        GregorianCalendar endYear;

        /** ����Ƃ̍��� */
        int diff;

        /**
         * ���܂��D
         * 
         * @param year �����̔N
         * @param month �� (1 ~ 12)
         * @param day ��
         */
        public void check(int year, int month, int day) {
            GregorianCalendar cal = new GregorianCalendar(year + diff, month - 1, day);

            if (cal.before(startYear) || cal.after(endYear)) {
                throw new IllegalArgumentException(shortName + year + "/" + month + "/" + day);
            }
        }

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
    private static final List<CalendarUtilJa> utils = new ArrayList<CalendarUtilJa>();

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

    /** */
    public static void main(String[] args) {

        JapaneseCalendar jc;

        if (args.length == 2 && "-d".equals(args[0])) {

            jc = new JapaneseCalendar(args[1]);
            System.err.println(jc);
            System.exit(0);
        }

        jc = new JapaneseCalendar();
        System.err.println("Today " + toFS(jc));
        System.err.println("Today " + jc);

        jc = new JapaneseCalendar("00010203");
        System.err.println("00010203 " + jc);
        System.err.println("00010203 " + toFS(jc));

        jc = new JapaneseCalendar("19890203");
        System.err.println("19890203 " + jc);

        jc = new JapaneseCalendar("19890107");
        System.err.println("19890107 " + jc);

        jc = new JapaneseCalendar("S", 64, 1, 7);
        System.err.println("S 64 1 7 " + jc);

        jc = new JapaneseCalendar("H", 1, 1, 8);
        System.err.println("H 1 1 8 " + jc);

        jc = new JapaneseCalendar(1989, 1, 8);
        System.err.println("1989 1 8 " + jc);

        jc = new JapaneseCalendar("S", 20, 8, 15);
        System.err.println("S 20 8 15 " + jc);
        System.err.println("S 20 8 15 " + toFS(jc));

        jc = new JapaneseCalendar("T111209");
        System.err.println("T111209 " + jc);
        System.err.println("T111209 " + toFS(jc));

        jc.set(2001, 8 - 1, 11); // TODO �@�\���Ă��Ȃ�
        System.err.println("2001 8 11 " + jc);
        System.err.println("2001 8 11 " + toFS(jc));
    }
}

/* */
