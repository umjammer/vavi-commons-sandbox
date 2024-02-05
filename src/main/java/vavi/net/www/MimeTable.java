/*
 * Copyright 1994-1999 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package vavi.net.www;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * MimeTable.
 */
public class MimeTable implements FileNameMap {

    /** Keyed by content type, returns MimeEntries */
    private Map<String, MimeEntry> entries = new HashMap<>();

    /** Keyed by file extension (with the .), returns MimeEntries */
    private Map<String, MimeEntry> extensionMap = new HashMap<>();

    /** */
//  private static final String filePreamble = "vavi.net.www MIME content-types table";

    /** */
//  private static final String fileMagic = "#" + filePreamble;

    /** */
    public MimeTable() {
        load();
    }

    /** */
    public synchronized int size() {
        return entries.size();
    }

    /** */
    public synchronized Map<String, MimeEntry> entries() {
        return entries;
    }

    /** @see FileNameMap */
    public synchronized String getContentTypeFor(String fileName) {
        MimeEntry entry = findByFileName(fileName);
// System.err.println(entry);
        if (entry != null) {
            return entry.getType();
        } else {
            return null;
        }
    }

    /** */
    public synchronized void add(MimeEntry m) {
        entries.put(m.getType(), m);

        String[] exts = m.getExtensions();
        if (exts == null) {
            return;
        }

        for (String ext : exts) {
            extensionMap.put(ext, m);
        }
    }

    /** */
    public synchronized MimeEntry remove(String type) {
        MimeEntry entry = entries.get(type);
        return remove(entry);
    }

    /** */
    public synchronized MimeEntry remove(MimeEntry entry) {
        String[] extensionKeys = entry.getExtensions();
        if (extensionKeys != null) {
            for (String extensionKey : extensionKeys) {
                extensionMap.remove(extensionKey);
            }
        }

        return entries.remove(entry.getType());
    }

    /** */
    public synchronized MimeEntry find(String type) {
        MimeEntry entry = entries.get(type);
        if (entry == null) {
            // try a wildcard lookup
            for (MimeEntry wild : entries.values()) {
                if (wild.matches(type)) {
                    return wild;
                }
            }
        }

        return entry;
    }

    /**
     * Locate a MimeEntry by the file extension that has been associated with it.
     * Parses general file names, and URLs.
     */
    public MimeEntry findByFileName(String fname) {
        String ext = "";

        int i = fname.lastIndexOf('#');

        if (i > 0) {
            fname = fname.substring(0, i - 1);
        }

        i = fname.lastIndexOf('.');
        // TODO OS specific delimiters appear here
        i = Math.max(i, fname.lastIndexOf('/'));
        i = Math.max(i, fname.lastIndexOf('?'));

        if (i != -1 && fname.charAt(i) == '.') {
            ext = fname.substring(i).toLowerCase();
        }

        return findByExt(ext);
    }

    /**
     * Locate a MimeEntry by the file extension that has been associated with it.
     */
    public synchronized MimeEntry findByExt(String fileExtension) {
        return extensionMap.get(fileExtension);
    }

    /** */
    public synchronized MimeEntry findByDescription(String description) {
        Iterator<MimeEntry> i = elements();
        while (i.hasNext()) {
            MimeEntry entry = i.next();
            if (description.equals(entry.getDescription())) {
                return entry;
            }
        }

        // We failed, now try treating description as type
        return find(description);
    }

    /** */
    public synchronized Iterator<MimeEntry> elements() {
        return entries.values().iterator();
    }

    /** */
    private static final String userTableFile = "/META-INF/content-types.properties";

    /** */
    public synchronized void load() {
        Properties entries = new Properties();
        Class<?> c = getClass();
        try {
            InputStream is;
            // First try to load the user-specific table, if it exists
            String userTablePath = System.getProperty("content.types.user.table");
            if (userTablePath != null) {
                is = c.getResourceAsStream(userTablePath);
                if (is == null) {
                    // No user-table, try to load the default built-in table.
                    is = c.getResourceAsStream(userTableFile);
                }
            } else {
                // No user table, try to load the default built-in table.
                is = c.getResourceAsStream(userTableFile);
            }
// System.err.println(is);
            InputStream bis = new BufferedInputStream(is);
            entries.load(bis);
            bis.close();
        } catch (IOException e) {
            System.err.println("Warning: default mime table not found: " + e);
            return;
        }
        parse(entries);
    }

    /** */
    void parse(Properties entries) {
        // now, parse the mime-type spec's
        Enumeration<?> types = entries.propertyNames();
        while (types.hasMoreElements()) {
            String type = (String) types.nextElement();
            String attrs = entries.getProperty(type);
            parse(type, attrs);
        }
    }

    //
    // Table format:
    //
    // <entry> ::= <table_tag> | <type_entry>
    //
    // <table_tag> ::= <table_format_version> | <temp_file_template>
    //
    // <type_entry> ::= <type_subtype_pair> '=' <type_attrs_list>
    //
    // <type_subtype_pair> ::= <type> '/' <subtype>
    //
    // <type_attrs_list> ::= <attr_value_pair> [ ';' <attr_value_pair> ]*
    // | [ <attr_value_pair> ]+
    //
    // <attr_value_pair> ::= <attr_name> '=' <attr_value>
    //
    // <attr_name> ::= 'description' | 'action' | 'application'
    // | 'file_extensions' | 'icon'
    //
    // <attr_value> ::= <legal_char>*
    //
    // Embedded ';' in an <attr_value> are quoted with leading '\' .
    //
    // Interpretation of <attr_value> depends on the <attr_name> it is
    // associated with.
    //

    /** */
    void parse(String type, String attrs) {
        MimeEntry newEntry = new MimeEntry(type);

        // REMIND handle embedded ';' and '|' and literal '"'
        StringTokenizer tokenizer = new StringTokenizer(attrs, ";");
        while (tokenizer.hasMoreTokens()) {
            String pair = tokenizer.nextToken();
            parse(pair, newEntry);
        }

        add(newEntry);
    }

    /** */
    void parse(String pair, MimeEntry entry) {
        // REMIND add exception handling...
        String name = null;
        String value = null;

        boolean gotName = false;
        StringTokenizer tokenizer = new StringTokenizer(pair, "=");
        while (tokenizer.hasMoreTokens()) {
            if (gotName) {
                value = tokenizer.nextToken().trim();
            } else {
                name = tokenizer.nextToken().trim();
                gotName = true;
            }
        }

        fill(entry, name, value);
    }

    /** */
    void fill(MimeEntry entry, String name, String value) {
        if ("description".equalsIgnoreCase(name)) {
            entry.setDescription(value);
        } else if ("action".equalsIgnoreCase(name)) {
            entry.setAction(getActionCode(value));
        } else if ("application".equalsIgnoreCase(name)) {
            entry.setCommand(value);
        } else if ("icon".equalsIgnoreCase(name)) {
            entry.setImageFileName(value);
        } else if ("file_extensions".equalsIgnoreCase(name)) {
            entry.setExtensions(value);
        }

        // else illegal name exception
    }

    /** */
    String[] getExtensions(String list) {
        StringTokenizer tokenizer = new StringTokenizer(list, ",");
        int n = tokenizer.countTokens();
        String[] extensions = new String[n];
        for (int i = 0; i < n; i++) {
            extensions[i] = tokenizer.nextToken();
        }

        return extensions;
    }

    /** */
    int getActionCode(String action) {
        for (int i = 0; i < MimeEntry.actionKeywords.length; i++) {
            if (action.equalsIgnoreCase(MimeEntry.actionKeywords[i])) {
                return i;
            }
        }

        return MimeEntry.UNKNOWN;
    }
}

/* */
