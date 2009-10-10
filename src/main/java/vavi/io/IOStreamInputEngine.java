/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * An input engine that copies data from an OutputStream through a
 * FilterInputStream to the target InputStream.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public class IOStreamInputEngine implements InputEngine {
    /** */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /** ���ۂɏ����o���X�g���[�� */
    private OutputStream out;

    /** */
    private InputStreamFactory factory;

    /** */
    private byte[] buffer;

    /** @see InputStreamFactory#getInputStream(InputStream) */
    private InputStream in;

    /**
     * @param out ���ۂɏ����o���X�g���[�� 
     */
    public IOStreamInputEngine(OutputStream out, InputStreamFactory factory) {
        this(out, factory, DEFAULT_BUFFER_SIZE);
    }

    /**
     * @param out ���ۂɏ����o���X�g���[�� 
     */
    public IOStreamInputEngine(OutputStream out, InputStreamFactory factory, int bufferSize) {
        this.out = out;
        this.factory = factory;
        buffer = new byte[bufferSize];
    }

    /**
     * @param in InputEngineOutputStream.InputStreamImpl
     */
    public void initialize(InputStream in) throws IOException {
        if (this.in != null) {
            throw new IOException("Already initialized");
        } else {
            this.in = factory.getInputStream(in);
        }
    }

    /** */
    public void execute() throws IOException {
        if (in == null) {
            throw new IOException("Not yet initialized");
        } else {
            int amount = in.read(buffer, 0, buffer.length);
//Debug.println("amount: " + amount + ", in: " + in + "\n" + StringUtil.getDump(buffer, 0, amount));
            if (amount < 0) {
                in.close();
            } else {
                out.write(buffer, 0, amount);
            }
        }
    }

    /** */
    public void finish() throws IOException {
        out.flush();
        out.close();
    }

    /** */
    public static interface InputStreamFactory {
        public InputStream getInputStream(InputStream in) throws IOException;
    }
}

/* */
