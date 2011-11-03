/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;


/**
 * An input engine that copies data from a Writer through a InputStreamReader
 * to the target InputStream.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public class ReaderWriterInputEngine implements InputEngine {
    /** */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /** */
    private Writer writer;

    /** */
    private String encoding;

    /** */
    private char[] buffer;

    /** */
    private Reader reader;

    /** */
    public ReaderWriterInputEngine(Writer out, String encoding) {
        this(out, encoding, DEFAULT_BUFFER_SIZE);
    }

    /** */
    public ReaderWriterInputEngine(Writer writer, String encoding, int bufferSize) {
        this.writer = writer;
        this.encoding = encoding;
        buffer = new char[bufferSize];
    }

    /* */
    public void initialize(InputStream in) throws IOException {
        if (reader != null) {
            throw new IOException("Already initialized");
        } else {
            reader = new InputStreamReader(in, encoding);
        }
    }

    /* */
    public void execute() throws IOException {
        if (reader == null) {
            throw new IOException("Not yet initialized");
        } else {
            writer.write(buffer);
            int amount = reader.read(buffer, 0, buffer.length);
            if (amount < 0) {
                reader.close();
            }
        }
    }

    /* */
    public void finish() throws IOException {
        writer.close();
    }
}

/* */
