/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.IOException;
import java.io.InputStream;


/**
 * An incremental data source that writes data to an OutputStream.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public interface InputEngine {
    /** */
    public void initialize(InputStream out) throws IOException;

    /** */
    public void execute() throws IOException;

    /** */
    public void finish() throws IOException;
}

/* */
