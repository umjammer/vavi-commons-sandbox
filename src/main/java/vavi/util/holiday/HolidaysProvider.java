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

    class Holyday implements Comparable<Holyday> {
        LocalDate date;
        String desc;
        public Holyday(LocalDate date, String desc) {
            this.date = date;
            this.desc = desc;
        }
        @Override
        public String toString() {
            return date + " " + desc;
        }
        @Override
        public int compareTo(Holyday o) {
            return this.date.compareTo(o.date);
        }
    }

    List<Holyday> holydays();

    /** factory */
    static HolidaysProvider defaultProvider() {
        return new GoogleICalHolidaysJaProvider();
    }
}

/* */
