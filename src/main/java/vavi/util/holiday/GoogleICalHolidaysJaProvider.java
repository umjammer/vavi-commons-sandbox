/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import vavi.util.ICal;
import vavi.util.Locales;
import vavi.util.serdes.Serdes;

import static java.lang.System.getLogger;
import static java.util.function.Predicate.not;


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
@Locales(countries = "Japan", languages = {"Japanese", "English"})
public class GoogleICalHolidaysJaProvider implements HolidaysProvider {

    private static final Logger logger = getLogger(GoogleICalHolidaysJaProvider.class.getName());

    private static final String url_ja = "https://www.google.com/calendar/ical/ja.japanese%23holiday%40group.v.calendar.google.com/public/basic.ics";
    private static final String url_en = "https://www.google.com/calendar/ical/japanese%40holiday.calendar.google.com/public/basic.ics";

    @Override
    public List<Holiday> holidays(int year) {
        try {
            String url = Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage()) ? url_ja : url_en;
logger.log(Level.DEBUG, "country: " + Locale.getDefault().getCountry());
            String userDefinedUrl = System.getProperty("vavix.util.holiday.ICalProvider.url");
            if (userDefinedUrl != null) {
                url = userDefinedUrl;
logger.log(Level.DEBUG, "userDefinedUrl: " + userDefinedUrl);
            }

            InputStream is = URI.create(url).toURL().openStream();
            ICal calendar = new ICal();
            Serdes.Util.deserialize(is, calendar);

            return calendar.events.stream()
                    .filter(e -> e.getDate().getYear() == year)
                    .filter(e -> e.getDescription().equals("祝日"))
                    .filter(not(e -> e.getSummary().equals("銀行休業日")))
                    .map(e -> new Holiday(e.getDate(), e.summary))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
