/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


/**
 * PasswordFieldTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/09 umjammer initial version <br>
 */
public class PasswordFieldTest {

    @Test
    public void test() throws Exception {
        InputStream is = new ByteArrayInputStream("p@sswo@d\n".getBytes());
        char[] password = PasswordField.getPassword(is, "Enter your password:#");
        assertArrayEquals("p@sswo@d".toCharArray(), password);
    }

    /** */
    public static void main(String[] argv) throws Exception {
        char[] password = PasswordField.getPassword(System.in, "Enter your password:#");
        if (password == null) {
            System.out.println("No password entered");
        } else {
            System.out.println("The password entered is: " + String.valueOf(password));
        }
    }
}
