/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.util.logging.Level;

import vavi.util.Debug;


/**
 * ������\������N���X�ł��D unix �̃t�@�C���V�X�e���̌����Ɠ����ł��D
 * 
 * @author <a href=mailto:vavivavi@yahoo.co.jp>Naohide Sano</a> (nsano)
 * 
 * @version 0.00 000224 nsano initial version <br>
 *          1.00 030924 nsano repackage <br>
 */
public class Permission {

    /** �ǂݍ��݉\�̃}�X�N */
    public static final int READ_MASK = 0x04;

    /** �������݉\�̃}�X�N */
    public static final int WRITE_MASK = 0x02;

    /** ���s�\�̃}�X�N */
    public static final int EXECUTE_MASK = 0x01;

    /** ���L�҂̃}�X�N */
    public static final int OWNER_MASK = 0;

    /** �O���[�v�̃}�X�N */
    public static final int GROUP_MASK = 1;

    /** ���̑��̃}�X�N */
    public static final int OTHER_MASK = 2;

    /** ���� */
    private int[] permission = new int[3];

    /**
     * Permission �I�u�W�F�N�g���\�z���܂��D
     * 
     * @param permission 3 ���̐��l
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
     * �ǂݎ�茠�������邩�ǂ����D
     * 
     * @param mask �Ώۂ̃}�X�N
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     */
    public boolean isReadable(int mask) {
        return isAvailable(mask, READ_MASK);
    }

    /**
     * �������݌��������邩�ǂ����D
     * 
     * @param mask �Ώۂ̃}�X�N
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     */
    public boolean isWriteable(int mask) {
        return isAvailable(mask, WRITE_MASK);
    }

    /**
     * ���s���������邩�ǂ����D
     * 
     * @param mask �Ώۂ̃}�X�N
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     */
    public boolean isExecutable(int mask) {
        return isAvailable(mask, EXECUTE_MASK);
    }

    /**
     * ���������邩�ǂ����D
     * 
     * @param level �Ώۂ̃}�X�N
     * @see #OWNER_MASK
     * @see #GROUP_MASK
     * @see #OTHER_MASK
     * @param type �����̃^�C�v
     * @see #READ_MASK
     * @see #WRITE_MASK
     * @see #EXECUTE_MASK
     */
    private boolean isAvailable(int level, int type) {
        return (permission[level] & type) != 0;
    }
}

/* */
