/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util.grep;

import java.io.File;
import java.io.IOException;


/**
 * �f�B���N�g�����ċA�I�Ɍ@�艺���Ă����C���^�[�t�F�[�X�ł��B
 * <p> 
 * �@��l�`
 * </p> 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050215 nsano initial version <br>
 */
public interface FileDigger {

    /**
     * ��̃t�@�C���ɑ΂��ĉ��炩�̃A�N�V�������s���C���^�[�t�F�[�X�ł��B
     * <p> 
     * �����l�`
     * </p> 
     */
    public interface FileDredger {
        /**
         * �����܂��B
         * @param file FileDigger �Ō@�蓖�Ă�ꂽ�t�@�C��
         */
        void dredge(File file) throws IOException;
    }

    /**
     * �@��܂��B
     * @param dir �g�b�v�f�B���N�g�� 
     */
    void dig(File dir) throws IOException;
}

/* */
