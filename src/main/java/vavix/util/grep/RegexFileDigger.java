/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util.grep;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * �f�B���N�g�����ċA�I�Ɍ@�艺���Ă����N���X�ł��B
 * <p> 
 * �@��l
 * </p> 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050215 nsano initial version <br>
 */
public class RegexFileDigger implements FileDigger {

    /**
     * �@�蓖�Ă��t�@�C���ɑ΂��čs���A�N�V����
     * <p>
     * �����l
     * </p> 
     */
    private FileDredger dredger;

    /** dredge ����p�^�[�� */
    private Pattern pattern;

    /**
     * �x�t���쐬���܂��B
     * @param dredger �@�蓖�Ă��t�@�C���ɑ΂��čs���A�N�V����
     * @param pattern dredge ����p�^�[�� (���ӁF�f�B���N�g�������͏����܂�)
     * TODO {@link FileFilter} �ł���������
     */
    public RegexFileDigger(FileDredger dredger, Pattern pattern) {
        this.dredger = dredger;
        this.pattern = pattern;
    }

    /**
     * �@��܂��B
     * @param dir �g�b�v�f�B���N�g�� 
     */
    public void dig(File dir) throws IOException {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                dig(files[i]);
            } else {
                Matcher matcher = pattern.matcher(files[i].getName());
                if (matcher.matches()) {
                    try {
                        dredger.dredge(files[i]);
                    } catch (IOException e) {
System.err.println("ERROR in file: " + files[i]);
                    }
                }
            }
        }
    }
}

/* */
