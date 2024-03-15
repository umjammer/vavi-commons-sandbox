/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.tools.ant;

import java.io.IOException;
import java.io.File;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import vavi.util.diff.Diff;
import vavi.util.diff.DiffUtil;

import static java.lang.System.getLogger;


/**
 * Diff.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040212 nsano initial version <br>
 */
public class DiffTask extends Task {

    private static final Logger logger = getLogger(DiffTask.class.getName());

    /** */
    private String srcFile;

    /** */
    public void setSrcFile(String srcfile) {
        this.srcFile = srcfile;
    }

    /** */
    private String targetFile;

    /** */
    public void setTargetFile(String targetfile) {
        this.targetFile = targetfile;
    }

    /** */
    public void execute() {
        try {
            String[] a = DiffUtil.readLines(new File(srcFile));
            String[] b = DiffUtil.readLines(new File(targetFile));
            Diff d = new Diff(a, b);
            Diff.Change script = d.getChange(false);
            if (script == null) {
                System.err.println("No differences");
            } else {
                DiffUtil.BasicPrinter p;
                p = new DiffUtil.EdPrinter(a, b);
                p.print(script);
            }
        } catch (IOException e) {
logger.log(Level.ERROR, e.getMessage(), e);
            throw new BuildException("Error", e);
        }
    }
}
