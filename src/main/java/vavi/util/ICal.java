/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

import vavi.beans.BeanUtil;
import vavi.util.serdes.BeanBinder;
import vavi.util.serdes.Binder;
import vavi.util.serdes.Element;
import vavi.util.serdes.Serdes;
import vavi.util.serdes.SimpleBeanBinder;

import static java.lang.System.getLogger;


/**
 * ICal.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/22 umjammer initial version <br>
 */
@Serdes(beanBinder = ICal.class)
public class ICal extends SimpleBeanBinder<ICal.ICalIOSource> implements Binder {

    private static final Logger logger = getLogger(ICal.class.getName());

    @Serdes
    public static class VCalendar {
        @Element("PRODID")
        String prodId;
        @Element("VERSION")
        String vesion;
        @Element("CALSCALE")
        String calScale;
        @Element("METHOD")
        String method;
        @Element("X-WR-CALNAME")
        String xWRCalname;
        @Element("X-WR-TIMEZONE")
        String xWRTimeZone;
        @Element("X-WR-CALDESC")
        String xWRCaldesc;
        @Override
        public String toString() {
            return prodId + " " + xWRCaldesc + " " + vesion;
        }
    }

    @Serdes
    public static class VEvent implements Comparable<VEvent> {
        @Element("DTSTART")
        String dtStart;
        @Element("DTEND")
        String dtEndId;
        @Element("DTSTAMP")
        String dtStamp;
        @Element("UID")
        String uiId;
        @Element("CLASS")
        String clazz;
        @Element("CREATED")
        String created;
        @Element("DESCRIPTION")
        String description;
        @Element("LAST-MODIFIED")
        String lastModifiled;
        @Element("SEQUENCE")
        String sequence;
        @Element("STATUS")
        String status;
        @Element("SUMMARY")
        public String summary;
        @Element("TRANSP")
        String transp;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd");
        public LocalDate getDate() {
            return formatter.parse(dtStart, LocalDate::from);
        }
        public String getDescription() {
            return description;
        }
        public String getSummary() {
            return summary;
        }
        @Override
        public String toString() {
            return getDate() + " " + summary + ", " + description;
        }
        @Override
        public int compareTo(VEvent o) {
            return this.getDate().compareTo(o.getDate());
        }
        public String toStringEx() {
            return new StringJoiner(", ", VEvent.class.getSimpleName() + "[", "]")
                    .add("dtStart='" + dtStart + "'")
                    .add("dtEndId='" + dtEndId + "'")
                    .add("dtStamp='" + dtStamp + "'")
                    .add("uiId='" + uiId + "'")
                    .add("clazz='" + clazz + "'")
                    .add("created='" + created + "'")
                    .add("description='" + description + "'")
                    .add("lastModifiled='" + lastModifiled + "'")
                    .add("sequence='" + sequence + "'")
                    .add("status='" + status + "'")
                    .add("summary='" + summary + "'")
                    .add("transp='" + transp + "'")
                    .add("formatter=" + formatter)
                    .toString();
        }
    }

    @Element
    public VCalendar calendar;

    @Element
    public List<VEvent> events;

    /** */
    static class ICalIOSource implements BeanBinder.IOSource {
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
logger.log(Level.WARNING, "unexpected mode: " + mode);
                        break;
                    case 1:
                        String[] ps = l.split("\\:");
                        lines1.put(ps[0], ps[1]);
                        break;
                    case 2:
                        ps = l.split("[\\:>]");
logger.log(Level.DEBUG, "pair: " + Arrays.toString(ps) + " (" + l + "), " + ev);
                        ev.put(ps[0].split("\\;")[0], ps[1]);
                        break;
                    }
                }
logger.log(Level.DEBUG, "mode: " + mode);
            }
            s.close();
logger.log(Level.DEBUG, "lines1: " + lines1.size() + ", lines2: " + lines2.size());
        }
    }

    @Override
    public ICalIOSource getIOSource(Object... args) throws IOException {
        ICalIOSource in = new ICalIOSource();
        if (args[0] instanceof InputStream) {
            InputStream is = (InputStream) args[0];
            in.read(is);
        } else {
            throw new IllegalArgumentException("unsupported class args[0]: " + args[0].getClass().getName());
        }
        return in;
    }

    @Override
    protected Binder getDefaultBinder() {
        return this;
    }

    // String
    protected EachBinder stringEachBinder = new Binder.StringEachBinder() {
        @SuppressWarnings("unchecked")
        public void bind(EachContext context, Object destBean, Field field) {
            SimpleEachContext eachContext = (SimpleEachContext) context;
            String key = Element.Util.getValue(field);
            if (destBean instanceof VCalendar) {
                context.setValue(eachContext.context.in.lines1.get(key));
            } else if (destBean instanceof VEvent) {
                context.setValue(eachContext.context.in.lines2.get(eachContext.context.in.index2).get(key));
            } else {
                assert false : destBean.getClass().getName();
            }
logger.log(Level.DEBUG, "field: " + field.getName() + " <- " + context.getValue());
        }
    };

    // List
    protected EachBinder listEachBinder = new EachBinder() {
        @Override public boolean matches(Class<?> fieldClass) {
            return fieldClass.equals(List.class);
        }
        @SuppressWarnings("unchecked")
        public void bind(EachContext context, Object destBean, Field field) throws IOException {
            SimpleEachContext eachContext = (SimpleEachContext) context;
            try {
                Object fieldValue = BeanUtil.getFieldValue(field, destBean);
                if (fieldValue == null) {
                    fieldValue = new ArrayList<>(); // TODO fixed as ArrayList
                }
                String name = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
                Class<?> fieldElementClass = Class.forName(name);
logger.log(Level.DEBUG, "generic type: " + fieldElementClass + ", " + eachContext.context.in.lines2.size());
                for (eachContext.context.in.index2 = 0; eachContext.context.in.index2 < eachContext.context.in.lines2.size(); eachContext.context.in.index2++) {
                    Object fieldBean = fieldElementClass.getDeclaredConstructor().newInstance();
                    context.deserialize(fieldBean);
                    ((List<Object>) fieldValue).add(fieldBean);
                }
                context.setValue(fieldValue);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException |
                     InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }
    };

    /** */
    private EachBinder[] eachBinders = {
        stringEachBinder,
        listEachBinder,
    };

    @Override
    public EachBinder[] getEachBinders() {
        return eachBinders;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ICal.class.getSimpleName() + "[", "]")
                .add("calendar=" + calendar)
                .add("events=" + events)
                .add("stringEachBinder=" + stringEachBinder)
                .add("listEachBinder=" + listEachBinder)
                .add("eachBinders=" + Arrays.toString(eachBinders))
                .toString();
    }
}
