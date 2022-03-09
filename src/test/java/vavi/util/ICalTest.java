/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.jupiter.api.Test;

import vavi.util.serdes.Serdes;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * ICalTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/22 umjammer initial version <br>
 */
class ICalTest {

    @Test
    void test() throws IOException {
        InputStream is = ICal.class.getResourceAsStream("/holidays_jp.ics");
        ICal calendar = new ICal();
        Serdes.Util.deserialize(is, calendar);
Debug.println(StringUtil.paramStringDeep(calendar, 2));
        assertEquals(50, calendar.events.size());
    }

    @Test
    void test2() throws IOException {
        InputStream is = URI.create("https://www.google.com/calendar/ical/japanese%40holiday.calendar.google.com/public/basic.ics").toURL().openStream();
        ICal calendar = new ICal();
        Serdes.Util.deserialize(is, calendar);
calendar.events.stream().sorted().forEach(System.err::println);
    }

    @Test
    void test3() throws IOException {
        InputStream is = URI.create("https://www.google.com/calendar/ical/ja.japanese%23holiday%40group.v.calendar.google.com/public/basic.ics").toURL().openStream();
        ICal calendar = new ICal();
        Serdes.Util.deserialize(is, calendar);
calendar.events.forEach(System.err::println);
    }
}

/* */
