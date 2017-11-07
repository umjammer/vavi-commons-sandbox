/**
 * libFLAC - Free Lossless Audio Codec library
 * Copyright (C) 2001,2002,2003  Josh Coalson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package vavix.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import vavix.util.RingBuffer;


/**
 * A <code>FilteredAudioInputStream</code> is an AudioInputStream with buffers
 * to facilitate transcoding the audio. A first byte array can buffer the data
 * from the underlying inputstream until sufficient data for transcoding is
 * present. A second byte array can hold the transcoded audio data, ready to be
 * read out by the stream user.
 *
 * @author Marc Gimpel, Wimba S.A. (marc@wimba.com)
 * @version $Revision: 1.3 $
 */
public abstract class RingBufferedInputStream extends FilterInputStream {

    /** The default size of the buffer. */
    public static final int DEFAULT_BUFFER_SIZE = 2048;

    /**
     * Array of size 1, used by the read method to read just 1 byte.
     */
    private final byte[] single = new byte[1];

    /** */
    protected RingBuffer buffer = new RingBuffer();

    /**
     * Check to make sure that this stream has not been closed.
     *
     * @exception IOException
     */
    private void checkIfStillOpen() throws IOException {
        if (in == null) {
            throw new IOException("Stream closed");
        }
    }

    /**
     * Creates a <code>FilteredAudioInputStream</code> and saves its argument,
     * the input stream <code>in</code>, for later use. An internal buffer
     * array is created and stored in <code>buf</code>.
     *
     * @param in the underlying input stream.
     * @param length the length in sample frames of the data in this stream.
     */
    public RingBufferedInputStream(InputStream in, long length) {
        super(in);
        buffer.setSize(DEFAULT_BUFFER_SIZE);
    }

    /**
     * Creates a <code>FilteredAudioInputStream</code> with the specified
     * buffer size, and saves its argument, the inputstream <code>in</code>
     * for later use. An internal buffer array of length <code>size</code> is
     * created and stored in <code>buf</code>.
     *
     * @param in the underlying input stream.
     */
    public RingBufferedInputStream(InputStream in) {
        super(in);
    }

    /**
     * Fills the buffer with more data, taking into account shuffling and other
     * tricks for dealing with marks. Assumes that it is being called by a
     * synchronized method. This method also assumes that all data has already
     * been read in, hence pos > count.
     *
     * @exception IOException
     */
    protected abstract void fill() throws IOException;

    /**
     * See the general contract of the <code>read</code> method of
     * <code>InputStream</code>.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     *         stream is reached.
     * @exception IOException if an I/O error occurs.
     * @see #in
     */
    public synchronized int read() throws IOException {
        fill();
        if (buffer.read(single, 0, 1) == -1) {
            return -1;
        } else {
            return (single[0] & 0xff);
        }
    }

    /**
     * Reads bytes from this byte-input stream into the specified byte array,
     * starting at the given offset.
     *
     * <p>
     * This method implements the general contract of the corresponding
     * <code>{@link InputStream#read(byte[], int, int) read}</code> method of
     * the <code>{@link InputStream}</code> class. As an additional
     * convenience, it attempts to read as many bytes as possible by repeatedly
     * invoking the <code>read</code> method of the underlying stream. This
     * iterated <code>read</code> continues until one of the following
     * conditions becomes true:
     * <ul>
     *
     * <li>The specified number of bytes have been read,
     *
     * <li>The <code>read</code> method of the underlying stream returns
     * <code>-1</code>, indicating end-of-file, or
     *
     * <li>The <code>available</code> method of the underlying stream returns
     * zero, indicating that further input requests would block.
     *
     * </ul>
     * If the first <code>read</code> on the underlying stream returns
     * <code>-1</code> to indicate end-of-file then this method returns
     * <code>-1</code>. Otherwise this method returns the number of bytes
     * actually read.
     *
     * <p>
     * Subclasses of this class are encouraged, but not required, to attempt to
     * read as many bytes as possible in the same fashion.
     *
     * @param b destination buffer.
     * @param off offset at which to start storing bytes.
     * @param len maximum number of bytes to read.
     * @return the number of bytes read, or <code>-1</code> if the end of the
     *         stream has been reached.
     * @exception IOException if an I/O error occurs.
     */
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        checkIfStillOpen();
        fill();
        int bytesRead = buffer.read(b, off, len);
        return bytesRead;
    }

    /**
     * See the general contract of the <code>skip</code> method of
     * <code>InputStream</code>.
     *
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @exception IOException if an I/O error occurs.
     */
    public synchronized long skip(long n) throws IOException {
        checkIfStillOpen();
        throw new IOException("skip not supported");
    }

    /**
     * Returns the number of bytes that can be read from this inputstream
     * without blocking.
     * <p>
     * The <code>available</code> method of
     * <code>FilteredAudioInputStream</code> returns the sum of the the number
     * of bytes remaining to be read in the buffer (<code>count - pos</code>).
     * The result of calling the <code>available</code> method of the
     * underlying inputstream is not used, as this data will have to be
     * filtered, and thus may not be the same size after processing (although
     * subclasses that do the filtering should override this method and use the
     * amount of data available in the underlying inputstream).
     *
     * @return the number of bytes that can be read from this inputstream
     *         without blocking.
     * @exception IOException if an I/O error occurs.
     * @see #in
     */
    public synchronized int available() throws IOException {
        checkIfStillOpen();
        fill();
        return buffer.available();
    }

    /**
     * See the general contract of the <code>mark</code> method of
     * <code>InputStream</code>.
     *
     * @param readlimit the maximum limit of bytes that can be read before the
     *            mark position becomes invalid.
     * @see #reset()
     */
    public synchronized void mark(int readlimit) {
    }

    /**
     * See the general contract of the <code>reset</code> method of
     * <code>InputStream</code>.
     * <p>
     * If <code>markpos</code> is -1 (no mark has been set or the mark has
     * been invalidated), an <code>IOException</code> is thrown. Otherwise,
     * <code>pos</code> is set equal to <code>markpos</code>.
     *
     * @exception IOException if this stream has not been marked or if the mark
     *                has been invalidated.
     * @see #mark(int)
     */
    public synchronized void reset() throws IOException {
        checkIfStillOpen();
        throw new IOException("reset not supported");
    }

    /**
     * Tests if this input stream supports the <code>mark</code> and
     * <code>reset</code> methods. The <code>markSupported</code> method of
     * <code>FilteredAudioInputStream</code> returns <code>true</code>.
     *
     * @return a <code>boolean</code> indicating if this stream type supports
     *         the <code>mark</code> and <code>reset</code> methods.
     * @see #mark(int)
     * @see #reset()
     */
    public boolean markSupported() {
        return false;
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream.
     *
     * @exception IOException if an I/O error occurs.
     */
    public synchronized void close() throws IOException {
        if (in == null) {
            return;
        }
        in.close();
        in = null;
    }
}

/* */
