/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import vavi.util.Debug;


/**
 * μBlockList regex checker.
 *
 * TODO title checker
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-02-24 nsano initial version <br>
 */
public class UBlockList {

    @Test
    void test1() throws Exception {
        main(new String[] {});
    }

    static List<Pattern> patterns = new ArrayList<>();

    static {
        try {
            Path p = Path.of(System.getProperty("user.home"), "Downloads", "uBlacklist.txt");
            Files.readAllLines(p).forEach(l -> {
                if (l.startsWith("/")) {
                    String regex = l.substring(1, l.length() - 1);
Debug.println(Level.INFO, "regex: " + regex);
                    patterns.add(Pattern.compile(regex));
                } else if (l.startsWith("*")) {
                    String regex = l.replaceAll("\\*", ".*");
Debug.println(Level.INFO, "glob: " + regex);
                    patterns.add(Pattern.compile(regex));
                } else { // TODO if startsWith("title")
Debug.println(Level.WARNING, "unhandled line: " + l);
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    /**
     * @param args 0: regex
     */
    public static void main(String[] args) throws Exception {
        String target = args[0];
        UBlockList app = new UBlockList();
        app.exec(target);
    }

    /** */
    void exec(String url) throws IOException {
        System.out.println(url);
        System.out.println("--------");
        patterns.forEach(p -> {
            if (p.matcher(url).find()) {
                System.out.println(p);
            }
        });
    }
}
