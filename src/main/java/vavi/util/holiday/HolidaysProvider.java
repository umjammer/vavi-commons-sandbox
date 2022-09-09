/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import java.time.LocalDate;
import java.util.List;


/**
 * HolidaysProvider.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/22 umjammer initial version <br>
 */
public interface HolidaysProvider {

    /** common */
    class Holiday implements Comparable<Holiday> {
        LocalDate date;
        String desc;
        public Holiday(LocalDate date, String desc) {
            this.date = date;
            this.desc = desc;
        }
        @Override
        public String toString() {
            return date + " " + desc;
        }
        @Override
        public int compareTo(Holiday o) {
            return this.date.compareTo(o.date);
        }
    }

    /** sorted */
    List<Holiday> holidays(int year);

    /** factory */
    static HolidaysProvider defaultProvider() {
        return new GoogleICalHolidaysJaProvider();
    }
}

/* */
