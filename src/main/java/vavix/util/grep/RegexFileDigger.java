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
 * ディレクトリを再帰的に掘り下げていくクラスです。
 * <p> 
 * 掘る人
 * </p> 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050215 nsano initial version <br>
 */
public class RegexFileDigger implements FileDigger {

    /**
     * 掘り当てたファイルに対して行うアクション
     * <p>
     * 浚う人
     * </p> 
     */
    private FileDredger dredger;

    /** dredge するパターン */
    private Pattern pattern;

    /**
     * 堀師を作成します。
     * @param dredger 掘り当てたファイルに対して行うアクション
     * @param pattern dredge するパターン (注意：ディレクトリ部分は除きます)
     * TODO {@link FileFilter} でもいいかも
     */
    public RegexFileDigger(FileDredger dredger, Pattern pattern) {
        this.dredger = dredger;
        this.pattern = pattern;
    }

    /**
     * 掘ります。
     * @param dir トップディレクトリ 
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
