/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;

import vavi.net.auth.oauth2.google.GoogleServiceAccountAppCredential;
import vavi.util.Debug;


/**
 * GoogleCalendarTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/23 umjammer initial version <br>
 */
class GoogleCalendarTest {

    // "primary"
    static final String calendarId = System.getenv("GOOGLE_ACCOUNT");

    @Test
    @DisabledIfEnvironmentVariable(named = "GITHUB_WORKFLOW", matches = ".*")
    void test1() throws Exception {
        GoogleServiceAccountAppCredential appCredential = new GoogleServiceAccountAppCredential("googlecalendar");

        Calendar service = new Calendar.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            new GsonFactory(),
            new HttpCredentialsAdapter(appCredential.getRawData()))
            .setApplicationName(appCredential.getApplicationName())
            .build();

        CalendarList calendarList = service.calendarList().list().execute();
calendarList.forEach((k, v) -> {
 System.err.println(k + ": " + v);
});

        Event event = new Event()
                .setSummary("summary")
                .setDescription("description")
                .setColorId("2") // green
                .setStart(new EventDateTime().setDateTime(new DateTime("2022-02-23T08:00:00.000+09:00")))
                .setEnd(new EventDateTime().setDateTime(new DateTime("2022-02-23T08:00:00.000+09:00")));
        event = service.events().insert(calendarId, event).execute();
Debug.println("event: " + event.getId());
    }
}
