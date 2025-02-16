/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util.grep;

import java.io.File;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * GrepTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2017/11/19 umjammer initial version <br>
 */
@SuppressWarnings("deprecation")
public class GrepTest {

    @Test
    @Disabled
    public void test() {
        fail("Not yet implemented");
    }

    /**
     * @param args 0: top directory, 1: grep pattern, 2: file pattern
     */
    public static void main(final String[] args) throws Exception {
        new RegexFileDigger(file -> {
            for (Grep.ResultSet rs : new Grep(file, Pattern.compile(args[1])).exec()) {
System.out.print(rs.file + ":" + rs.lineNumber + ":" + rs.line);
            }
        }, Pattern.compile(args[2])).dig(new File(args[0]));
    }
}
