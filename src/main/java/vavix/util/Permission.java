/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * This class represents permissions, similar to permissions in a unix file system.
 *
 * @author <a href=mailto:umjammer@gmail.com>Naohide Sano</a> (nsano)
 *
 * @version 0.00 000224 nsano initial version <br>
 *          1.00 030924 nsano repackage <br>
 * @deprecated use {@link java.nio.file.attribute.PosixFilePermission}
 */
@Deprecated
public class Permission {

    private static final Logger logger = getLogger(Permission.class.getName());

    /** Readable mask */
    public static final int READ_MASK = 0x04;

    /** Writable mask */
    public static final int WRITE_MASK = 0x02;

    /** Executable Mask */
    public static final int EXECUTE_MASK = 0x01;

    /** Owner's Mask */
    public static final int OWNER_MASK = 0;

    /** Group Mask */
    public static final int GROUP_MASK = 1;

    /** Other Masks */
    public static final int OTHER_MASK = 2;

    /** permission */
    private int[] permission = new int[3];

    /**
     * Constructs a Permission object.
     *
     * @param permission Three digit number
     */
    public Permission(String permission) {
        try {
            this.permission[OWNER_MASK] = Integer.parseInt(permission.substring(OWNER_MASK, OWNER_MASK + 1));
            this.permission[GROUP_MASK] = Integer.parseInt(permission.substring(GROUP_MASK, GROUP_MASK + 1));
            this.permission[OTHER_MASK] = Integer.parseInt(permission.substring(OTHER_MASK, OTHER_MASK + 1));
        } catch (Exception e) {
            logger.log(Level.ERROR, "bad permission string: [" + permission + "]");
        }
logger.log(Level.DEBUG, "permission[" + this.permission[OWNER_MASK] + ":" + this.permission[GROUP_MASK] + ":" + this.permission[OTHER_MASK] + "]");
    }

    /**
     * Do you have read permission?
     *
     * @param mask Target Mask
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     */
    public boolean isReadable(int mask) {
        return isAvailable(mask, READ_MASK);
    }

    /**
     * Do you have write permission?
     *
     * @param mask Target Mask
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     */
    public boolean isWriteable(int mask) {
        return isAvailable(mask, WRITE_MASK);
    }

    /**
     * Do you have permission to execute?
     *
     * @param mask Target Mask
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     */
    public boolean isExecutable(int mask) {
        return isAvailable(mask, EXECUTE_MASK);
    }

    /**
     * Do you have the permission?
     *
     * @param level Target Mask
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     * @param type Privilege Types
     * @see #READ_MASK
     * @see #WRITE_MASK
     * @see #EXECUTE_MASK
     */
    private boolean isAvailable(int level, int type) {
        return (permission[level] & type) != 0;
    }
}
