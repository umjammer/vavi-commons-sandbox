/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.tools.ant;

import java.io.IOException;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import vavi.util.Debug;
import vavi.util.diff.Diff;
import vavi.util.diff.DiffUtil;


/**
 * Diff.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040212 nsano initial version <br>
 */
public class DiffTask extends Task {

    /** */
    private String srcfile;

    /** */
    public void setSrcfile(String srcfile) {
        this.srcfile = srcfile;
    }

    /** */
    private String targetfile;

    /** */
    public void setTargetfile(String targetfile) {
        this.targetfile = targetfile;
    }

    /** */
    public void execute() {
        try {
            String[] a = DiffUtil.readLines(new File(srcfile));
            String[] b = DiffUtil.readLines(new File(targetfile));
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
Debug.printStackTrace(e);
            throw new BuildException("Error", e);
        }
    }
}

/* */
