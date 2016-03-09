/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * PasswordFieldTest. 
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/09 umjammer initial version <br>
 */
public class PasswordFieldTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    /** */
    public static void main(String[] argv) {
        char[] password = null;
        try {
            password = PasswordField.getPassword(System.in, "Enter your password:#");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (password == null) {
            System.out.println("No password entered");
        } else {
            System.out.println("The password entered is: " + String.valueOf(password));
        }
    }
}

/* */
