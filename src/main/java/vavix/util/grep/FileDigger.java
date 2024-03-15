/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


/**
 * ディレクトリを再帰的に掘り下げていくインターフェースです。
 * <p>
 * 掘る人形
 * </p>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 050215 nsano initial version <br>
 * @deprecated use {@link Files#walkFileTree(java.nio.file.Path, java.nio.file.FileVisitor)}
 */
@Deprecated
@FunctionalInterface
public interface FileDigger {

    /**
     * 一つのファイルに対して何らかのアクションを行うインターフェースです。
     * <p>
     * 浚う人形
     * </p>
     */
    @FunctionalInterface
    public interface FileDredger {
        /**
         * 浚います。
         * @param file FileDigger で掘り当てられたファイル
         */
        void dredge(File file) throws IOException;
    }

    /**
     * 掘ります。
     * @param dir トップディレクトリ
     */
    void dig(File dir) throws IOException;
}
