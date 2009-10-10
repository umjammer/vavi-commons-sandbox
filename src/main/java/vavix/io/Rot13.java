/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.io;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;


/**
 * Rot13.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040503 nsano initial version <br>
 */
public class Rot13 {

    /** */
    public static int codec(int c) {
        if (c >= 'a' && c <= 'm') {
            c += 13;
        } else if (c >= 'n' && c <= 'z') {
            c -= 13;
        } else if (c >= 'A' && c <= 'M') {
            c += 13;
        } else if (c >= 'N' && c <= 'Z') {
            c -= 13;
        } else if (c >= '0' && c <= '4') {
            c += 5;
        } else if (c >= '5' && c <= '9') {
            c -= 5;
        }
        return c;
    }

    /** */
    public static class InputStream extends FilterInputStream {
        /** */
        public InputStream(java.io.InputStream in) {
            super(in);
        }
        /** */
        public int read() throws IOException {
            return codec(in.read());
        }
        /** */
        public int read(byte b[], int off, int len) throws IOException {
            if (b == null) {
                throw new NullPointerException("byte[]");
            } else if ((off < 0) || (off > b.length) || (len < 0) ||
                     ((off + len) > b.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException("off: " + off + ", len: " + len);
            } else if (len == 0) {
                return 0;
            }

            int c = read();
            if (c == -1) {
                return -1;
            }
            b[off] = (byte) c;

            int i = 1;
            try {
                for (; i < len ; i++) {
                    c = read();
                    if (c == -1) {
                        break;
                    }
                    if (b != null) {
                        b[off + i] = (byte) c;
                    }
                }
            } catch (IOException e) {
e.printStackTrace(System.err);
            }
            return i;
        }
    }

    /** */
    public static class OutputStream extends FilterOutputStream {
        /** */
        public OutputStream(java.io.OutputStream out) {
            super(out);
        }
        /** */
        public void write(int b) throws IOException {
            out.write(codec(b));
        }
    }
}

/* */
