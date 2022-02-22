/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

import vavi.beans.BeanUtil;
import vavi.util.Debug;
import vavi.util.serdes.Binder;
import vavi.util.serdes.DefaultBinder;
import vavi.util.serdes.Element;
import vavi.util.serdes.Serdes;
import vavi.util.serdes.Serdes.Util.Context;


/**
 * ICal.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/22 umjammer initial version <br>
 */
@Serdes(binder = ICal.class)
public class ICal extends DefaultBinder {

    @Serdes(binder = VCalendar.class)
    public static class VCalendar implements Binder {
        @Element(sequence = 1, value = "PRODID")
        String prodId;
        @Element(sequence = 2, value = "VERSION")
        String vesion;
        @Element(sequence = 3, value = "CALSCALE")
        String calScale;
        @Element(sequence = 4, value = "METHOD")
        String method;
        @Element(sequence = 5, value = "X-WR-CALNAME")
        String xWRCalname;
        @Element(sequence = 6, value = "X-WR-TIMEZONE")
        String xWRTimeZone;
        @Element(sequence = 7, value = "X-WR-CALDESC")
        String xWRCaldesc;
        @Override
        public String toString() {
            return prodId + " " + xWRCaldesc + " " + vesion;
        }
        @Override
        public void bind(Context context, Object destBean, Field field) throws IOException {
            if (field.getType().equals(String.class)) {
                String key = Element.Util.getValue(field);
                context.value = preReader.get().lines1.get(key);
Debug.println(Level.FINE, "field: " + field.getName() + " <- " + context.value);
            } else {
                assert false : field.getType().toString();
            }
            BeanUtil.setFieldValue(field, destBean, context.value);
        }
    }

    @Serdes(binder = VEvent.class)
    public static class VEvent implements Binder, Comparable<VEvent> {
        @Element(sequence = 1, value = "DTSTART")
        String dtStart;
        @Element(sequence = 2, value = "DTEND")
        String dtEndId;
        @Element(sequence = 3, value = "DTSTAMP")
        String dtStamp;
        @Element(sequence = 4, value = "UID")
        String uiId;
        @Element(sequence = 5, value = "CLASS")
        String clazz;
        @Element(sequence = 6, value = "CREATED")
        String created;
        @Element(sequence = 7, value = "DESCRIPTION")
        String description;
        @Element(sequence = 9, value = "LAST-MODIFIED")
        String lastModifiled;
        @Element(sequence = 9, value = "SEQUENCE")
        String sequence;
        @Element(sequence = 10, value = "STATUS")
        String status;
        @Element(sequence = 11, value = "SUMMARY")
        public String summary;
        @Element(sequence = 12, value = "TRANSP")
        String transp;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd");
        public LocalDate getDate() {
            return formatter.parse(dtStart, LocalDate::from);
        }
        @Override
        public String toString() {
            return getDate() + " " + summary;
        }
        @Override
        public int compareTo(VEvent o) {
            return this.getDate().compareTo(o.getDate());
        }
        public void bind(Context context, Object destBean, Field field) throws IOException {
            if (field.getType().equals(String.class)) {
                String key = Element.Util.getValue(field);
                context.value = preReader.get().lines2.get(preReader.get().index2).get(key);
Debug.println(Level.FINE, "field: " + field.getName() + " <- " + context.value);
            } else {
                assert false : field.getType().toString();
            }
            BeanUtil.setFieldValue(field, destBean, context.value);
        }
    }

    private static class PreReader {
        private Map<String, String> lines1 = new HashMap<>();
        private List<Map<String, String>> lines2 = new ArrayList<>();
        int index2;
        private void read(InputStream is) {
            Scanner s = new Scanner(is);
            int mode = 0;
            Map<String, String> ev = null;
            while (s.hasNextLine()) {
                String l = s.nextLine();
//System.err.println(l);
                if (l.startsWith("BEGIN:VCALENDAR")) {
                    mode = 1;
                } else if (l.startsWith("END:VCALENDAR")) {
                    mode = 0;
                    break;
                } else if (l.startsWith("BEGIN:VEVENT")) {
                    mode = 2;
                    ev = new HashMap<>();
                } else if (l.startsWith("END:VEVENT")) {
                    mode = 1;
                    lines2.add(ev);
                } else {
                    switch (mode) {
                    default:
                    case 0:
Debug.println(Level.WARNING, "unexpected mode: " + mode);
                        break;
                    case 1:
                        String[] ps = l.split("\\:");
                        lines1.put(ps[0], ps[1]);
                        break;
                    case 2:
                        ps = l.split("\\:");
Debug.println(Level.FINE, "pair: " + Arrays.toString(ps) + ", " + ev);
                        ev.put(ps[0].split("\\;")[0], ps[1]);
                        break;
                    }
                }
Debug.println(Level.FINE, "mode: " + mode);
            }
            s.close();
Debug.println(Level.FINE, "lines1: " + lines1.size() + ", lines2: " + lines2.size());
        }
    }

    /** TODO bad use */
    private static ThreadLocal<PreReader> preReader = new ThreadLocal<>();

    @Element(sequence = 1)
    public VCalendar calendar;

    @Element(sequence = 2)
    public List<VEvent> events;

    @SuppressWarnings("unchecked")
    @Override
    public void bind(Context context, Object destBean, Field field) throws IOException {
        if (preReader.get() == null || ICal.class.cast(destBean).calendar == null) {
            preReader.remove();
            preReader.set(new PreReader());
            preReader.get().read((InputStream) context.dis);
        }

        if (field.getType().equals(List.class)) {
            try {
                Object fieldValue = BeanUtil.getFieldValue(field, destBean);
                if (fieldValue == null) {
                    fieldValue = new ArrayList<>(); // TODO fixed as ArrayList
                }
                String name = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
                Class<?> fieldElementClass = Class.forName(name);
Debug.println(Level.FINE, "generic type: " + fieldElementClass + ", " + preReader.get().lines2.size());
                for (preReader.get().index2 = 0; preReader.get().index2 < preReader.get().lines2.size(); preReader.get().index2++) {
                    Object fieldBean = fieldElementClass.newInstance();
                    context.deserialize(fieldBean);
                    List.class.cast(fieldValue).add(fieldBean);
                }
                context.value = fieldValue;
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        } else if (defaultEachBinder.matches(field.getType())) {
            defaultEachBinder.bind(context, destBean, field);
        } else {
            assert false : field.getType().toString();
        }

        BeanUtil.setFieldValue(field, destBean, context.value);
    }
}

/* */
