/*
 * Copyright (c) 2009 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


/**
 * FileUtil.
 * 
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2009/02/17 nsano initial version <br>
 */
public class FileUtil {

    /** */
    @SuppressWarnings("resource")
    public static void copy(File src, File dest) throws IOException {
        FileChannel srcChannel = new FileInputStream(src).getChannel();
        FileChannel destChannel = new FileOutputStream(dest).getChannel();
        destChannel.transferFrom(srcChannel, 0, srcChannel.size());
    }
}

/* */
