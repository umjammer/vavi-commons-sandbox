/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.tools.ant;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * FileProperties.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040125 nsano initial version <br>
 */
public class FilePropertiesTask extends Task {

    /** */
    private String name;

    /** */
    public void setName(String name) {
        this.name = name;
    }

    /** */
    private String file;

    /** */
    public void setFile(String file) {
        this.file = file;
    }

    /** */
    public void execute() {
        try {
            File file = new File(this.file);
            if (!file.exists()) {
                file = new File(getProject().getBaseDir(), this.file);
            }
            if (!file.exists()) {
                throw new BuildException("file " + file + " does not exists");
            }
            getProject().setNewProperty(name + ".length", String.valueOf(file.length()));
//System.err.println("file.length: " + getProject().getProperty(name + ".length"));

            getProject().setNewProperty(name + ".lastModified", String.valueOf(file.lastModified()));
//System.err.println("file.lastModified: " + getProject().getProperty(name + ".lastModified"));

        } catch (Exception e) {
e.printStackTrace(System.err);
            throw new BuildException("error", e);
        }
    }
}

/* */
