/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.util.logging.Level;

import vavi.util.Debug;


/**
 * 権限を表現するクラスです． unix のファイルシステムの権限と同じです．
 *
 * @author <a href=mailto:vavivavi@yahoo.co.jp>Naohide Sano</a> (nsano)
 *
 * @version 0.00 000224 nsano initial version <br>
 *          1.00 030924 nsano repackage <br>
 */
public class Permission {

    /** 読み込み可能のマスク */
    public static final int READ_MASK = 0x04;

    /** 書き込み可能のマスク */
    public static final int WRITE_MASK = 0x02;

    /** 実行可能のマスク */
    public static final int EXECUTE_MASK = 0x01;

    /** 所有者のマスク */
    public static final int OWNER_MASK = 0;

    /** グループのマスク */
    public static final int GROUP_MASK = 1;

    /** その他のマスク */
    public static final int OTHER_MASK = 2;

    /** 権限 */
    private int[] permission = new int[3];

    /**
     * Permission オブジェクトを構築します．
     *
     * @param permission 3 桁の数値
     */
    public Permission(String permission) {
        try {
            this.permission[OWNER_MASK] = Integer.parseInt(permission.substring(OWNER_MASK, OWNER_MASK + 1));
            this.permission[GROUP_MASK] = Integer.parseInt(permission.substring(GROUP_MASK, GROUP_MASK + 1));
            this.permission[OTHER_MASK] = Integer.parseInt(permission.substring(OTHER_MASK, OTHER_MASK + 1));
        } catch (Exception e) {
            Debug.println(Level.SEVERE, "bad permission string: [" + permission + "]");
        }
        Debug.println("permission[" + this.permission[OWNER_MASK] + ":" + this.permission[GROUP_MASK] + ":" + this.permission[OTHER_MASK] + "]");
    }

    /**
     * 読み取り権限があるかどうか．
     *
     * @param mask 対象のマスク
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     */
    public boolean isReadable(int mask) {
        return isAvailable(mask, READ_MASK);
    }

    /**
     * 書き込み権限があるかどうか．
     *
     * @param mask 対象のマスク
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     */
    public boolean isWriteable(int mask) {
        return isAvailable(mask, WRITE_MASK);
    }

    /**
     * 実行権限があるかどうか．
     *
     * @param mask 対象のマスク
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     */
    public boolean isExecutable(int mask) {
        return isAvailable(mask, EXECUTE_MASK);
    }

    /**
     * 権限があるかどうか．
     *
     * @param level 対象のマスク
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     * @param type 権限のタイプ
     * @see #READ_MASK
     * @see #WRITE_MASK
     * @see #EXECUTE_MASK
     */
    private boolean isAvailable(int level, int type) {
        return (permission[level] & type) != 0;
    }
}

/* */
