/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;


/**
 * JapaneseCalendarTest.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050228 nsano initial version <br>
 */
public class JapaneseCalendarTest extends TestCase {

    JapaneseCalendar jc;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Class under test for void JapaneseCalendar()
     */
    public void test1() {

        jc = new JapaneseCalendar((GregorianCalendar) Calendar.getInstance());
        assertEquals(jc, JapaneseCalendar.toFS(jc));

        jc = new JapaneseCalendar("00010203");
        assertEquals(jc, JapaneseCalendar.toFS(jc));

        jc = new JapaneseCalendar("19890203");
        assertEquals("19890203", jc);

        jc = new JapaneseCalendar("19890107");
        assertEquals("19890107", jc);

        jc = new JapaneseCalendar("S", 64, 1, 7);
        assertEquals("S 64 1 7", jc);

        jc = new JapaneseCalendar("H", 1, 1, 8);
        assertEquals("H 1 1 8", jc);

        jc = new JapaneseCalendar(1989, 1, 8);
        assertEquals("1989 1 8", jc);

        jc = new JapaneseCalendar("S", 20, 8, 15);
        assertEquals("S 20 8 15", jc);
        assertEquals("S 20 8 15", JapaneseCalendar.toFS(jc));

        jc = new JapaneseCalendar("T111209");
        assertEquals("T111209", jc);
        assertEquals("T111209", JapaneseCalendar.toFS(jc));
    }

    /**
     * Class under test for void JapaneseCalendar()
     */
    public void test2() {
        jc.set(2001, 8 - 1, 11);        // TODO 機能していない
        assertEquals("2001 8 11", jc);
        assertEquals("2001 8 11", JapaneseCalendar.toFS(jc));
    }
}

/* */
