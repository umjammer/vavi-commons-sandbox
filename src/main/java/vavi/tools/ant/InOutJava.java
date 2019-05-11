/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.tools.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;


/**
 * Extension of the org.apache.tools.ant.taskdefs.Java in order to enable
 * the redirection of the Standard Input of the Java application loaded.
 * It just add another param to the Java task, "input".
 * By now, it doesn't accept the fork option, because then I don't know
 * how to make the redirection :-/, and it gets hung...
 * Any ideas will be welcome
 * @version 14 june 2002.
 * @author ebota@uoc.edu
 */
public class InOutJava extends Java {

    /** */
    InputStream origIn = System.in;
    /** */
    InputStream stdin = System.in;
    /** */
    OutputStream origOut = System.out;
    /** */
    OutputStream stdout = System.out;
    /** */
    OutputStream origErr = System.err;
    /** */
    OutputStream stderr = System.err;

    /** */
    public InOutJava () {
        super();
    }

    /** */
    private static final String message1 =
        "InOutJava: Fork not available when redirecting standard input";

    /**
     * It controls that fork it's not enabled
     */
    public void setFork(boolean b) throws BuildException {
        if (b) {
            log(message1, Project.MSG_ERR);
            throw new BuildException(message1);
        } else {
            super.setFork(b);
        }
    }

    /** */
    private static final String message2 =
        "InOutJava error opening input file";

    /**
     * The new Standard Input
     */
    public void setInput(File in) {
        try {
            stdin = new FileInputStream(in);
        } catch (IOException e) {
            log(message2, Project.MSG_ERR);
            throw new BuildException(message2, e);
        }
    }

    /** */
    private static final String message3 =
        "InOutJava error opening output file";

    /**
     * The new Standard Output
     */
    public void setOutput(File out) {
        try {
            stdout = new FileOutputStream(out);
        } catch (IOException e) {
            log(message3, Project.MSG_ERR);
            throw new BuildException(message3, e);
        }
    }

    /** */
    public void execute() {
        try {
            System.setIn(stdin);
            System.setOut(new PrintStream(stdout));
            super.execute();
            stdin.close();
            stdout.close();
            System.setIn(origIn);
            System.setOut(new PrintStream(origOut));
        } catch (Exception e) {
e.printStackTrace(System.err);
            System.out.println("InOutJava Error: " + e);
        }
    }
}

/* */
