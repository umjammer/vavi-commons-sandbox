/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Events;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;

import vavi.net.auth.AppCredential;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


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
public class GoogleCalendarHolidaysJaProvider implements HolidaysProvider {

    @PropsEntity(url = "file://${user.home}/.vavifuse/google.properties")
    static class MyGoogleAppCredential implements AppCredential {

        @Property(name = "google.{0}.applicationName")
        private String applicationName;

        @Property(name = "google.{0}.scopes")
        private String scope;

        private String id;

        /** */
        protected ServiceAccountCredentials credentials;

        /**
         * @param id ~/vavifuse/{id}.json
         */
        public MyGoogleAppCredential(String id) {
            try {
                PropsEntity.Util.bind(this, id);

                this.id = id;
                Path dir = Paths.get(System.getProperty("user.home"), ".vavifuse");

                InputStream in = Files.newInputStream(dir.resolve(id + ".json"));
                this.credentials = (ServiceAccountCredentials) ServiceAccountCredentials
                        .fromStream(in)
                        .createScoped(Collections.singleton(scope));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            try {
                PropsEntity.Util.bind(this, id);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public String getScheme() {
            return id;
        }

        @Override
        public String getApplicationName() {
            return applicationName;
        }

        @Override
        public String getClientId() {
            return credentials.getClientId();
        }
    }

    private Calendar service;

    String serviceAccoundt = "primary";

    {
        try {
            MyGoogleAppCredential appCredential = new MyGoogleAppCredential("googlecalendar");
            service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                new HttpCredentialsAdapter(appCredential.credentials))
                .setApplicationName(appCredential.getApplicationName())
                .build();

            CalendarList calendarList = service.calendarList().list().execute();
calendarList.forEach((k, v) -> {
 System.err.println(k + ": " + v);
});
        } catch (IOException | GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Holyday> holydays() {
        try {
            Events events = service.events().list(serviceAccoundt)
                .setMaxResults(100)
                .setTimeMax(new DateTime("2012-04-23T00:00:00.000+09:00"))
                .setTimeMin(new DateTime("2012-03-24T00:00:00.000+09:00"))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
Debug.println("items: " + events.getItems().size());
            return events.getItems().stream().map(e -> new Holyday(toLocalDate(e.getStart().getDateTime()), e.getDescription())).collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static LocalDate toLocalDate(DateTime dateTime) {
        return LocalDate.ofEpochDay(dateTime.getValue());
    }
}

/* */
