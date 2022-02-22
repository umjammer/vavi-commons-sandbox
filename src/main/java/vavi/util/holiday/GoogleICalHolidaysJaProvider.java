/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import vavi.util.Debug;
import vavi.util.serdes.Serdes;


/**
 * GoogleICalHolidaysJaProvider.
 * <p>
 * <pre>
 * system properties
 * "vavix.util.holiday.ICalProvider.url" ... url for iCal (.icl file)
 * </pre>
 * 
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/22 umjammer initial version <br>
 */
public class GoogleICalHolidaysJaProvider implements HolidaysProvider {

    private static final String url_jp = "https://www.google.com/calendar/ical/ja.japanese%23holiday%40group.v.calendar.google.com/public/basic.ics";
    private static final String url_en = "https://www.google.com/calendar/ical/japanese%40holiday.calendar.google.com/public/basic.ics";

    @Override
    public List<Holyday> holydays() {
        try {
            String url = Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage()) ? url_jp : url_en;
Debug.println("country: " + Locale.getDefault().getCountry());
            String userDefinedUrl = System.getProperty("vavix.util.holiday.ICalProvider.url");
            if (userDefinedUrl != null) {
                url = userDefinedUrl;
Debug.println("userDefinedUrl: " + userDefinedUrl);
            }

            InputStream is = URI.create(url).toURL().openStream();
            ICal calendar = new ICal();
            Serdes.Util.deserialize(is, calendar);

            return calendar.events.stream().map(e -> new Holyday(e.getDate(), e.summary)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

/* */
