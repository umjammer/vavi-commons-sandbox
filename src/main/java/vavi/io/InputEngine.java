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
    void initialize(InputStream in) throws IOException;

    /** 
     * in ‚ª -1 ‚ğ•Ô‚·‚Ü‚Å“Ç‚İ‚Ş•K—v‚ª‚ ‚é 
     */
    void execute() throws IOException;

    /** */
    void finish() throws IOException;
}

/* */
