/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import com.google.auth.http.HttpCredentialsAdapter;

import vavi.net.auth.oauth2.google.GoogleServiceAccountAppCredential;
import vavi.util.Debug;
import vavi.util.Locales;

import static java.util.function.Predicate.not;


/**
 * GoogleCalendarHolidaysJaProvider.
 * <p>
 * <pre>
 * system properties
 * </pre>
 * 
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/22 umjammer initial version <br>
 */
@Locales(countries = "Japan", languages = "Japanese")
public class GoogleCalendarHolidaysJaProvider implements HolidaysProvider {

    private static final String calendarId = "japanese__ja@holiday.calendar.google.com";

    private final Calendar service;

    {
        try {
            GoogleServiceAccountAppCredential appCredential = new GoogleServiceAccountAppCredential("googlecalendar");
            service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(appCredential.getRawData()))
                .setApplicationName(appCredential.getApplicationName())
                .build();
        } catch (IOException | GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Holiday> holidays(int year) {
        try {
            Events events = service.events().list(calendarId)
                .setTimeMin(new DateTime(String.format("%04d-01-01T00:00:00.000+00:00", year)))
                .setTimeMax(new DateTime(String.format("%04d-01-01T00:00:00.000+00:00", year + 1)))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
Debug.println(Level.FINE, "items: " + events.getItems().size());
events.getItems().stream()
        .filter(e -> e.getDescription().equals("祝日"))
        .filter(not(e -> e.getSummary().equals("銀行休業日")))
        .forEach(System.err::println);
            return events.getItems().stream()
                    .filter(e -> e.getDescription().equals("祝日"))
                    .filter(not(e -> e.getSummary().equals("銀行休業日")))
                    .map(e -> new Holiday(toLocalDate(e.getStart().getDate()), e.getSummary()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static LocalDate toLocalDate(DateTime dateTime) {
        return Instant.ofEpochMilli(dateTime.getValue()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
