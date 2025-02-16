/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

import vavi.util.Locales;


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

    /**
     * factory
     * <p>
     * TODO use ServiceProvider, consider @Locales
     */
    static HolidaysProvider defaultProvider() {
        for (HolidaysProvider p : ServiceLoader.load(HolidaysProvider.class)) {
            Locales a = p.getClass().getAnnotation(Locales.class);
            if (Arrays.asList(a.countries()).contains(Locale.getDefault().getCountry()) &&
                    Arrays.asList(a.languages()).contains(Locale.getDefault().getCountry())) {
                return p;
            }
        }
        for (HolidaysProvider p : ServiceLoader.load(HolidaysProvider.class)) {
            Locales a = p.getClass().getAnnotation(Locales.class);
            if (Arrays.asList(a.countries()).contains(Locale.getDefault().getCountry())) {
                return p;
            }
        }
        for (HolidaysProvider p : ServiceLoader.load(HolidaysProvider.class)) {
            Locales a = p.getClass().getAnnotation(Locales.class);
            if (Arrays.asList(a.languages()).contains(Locale.getDefault().getCountry())) {
                return p;
            }
        }
        return ServiceLoader.load(HolidaysProvider.class).iterator().next();
    }
}
