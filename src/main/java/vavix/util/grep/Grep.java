/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util.grep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 濾し器
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050215 nsano initial version <br>
 */
public class Grep {
    /** */
    private static Charset charset = Charset.forName(System.getProperty("file.encoding"));
    /** */
    private static CharsetDecoder decoder = charset.newDecoder();
    
    /** Pattern used to parse lines */
    private static final Pattern linePattern = Pattern.compile(".*\r?\n");
    
    /** 濾すパターン */
    private Pattern pattern;
    
    /** 濾す対象 */
    private File file;
    
    /** 濾し器作成 */
    public Grep(File file, Pattern pattern) {
        this.file = file;
        this.pattern = pattern;
    }
    
    /**
     * grep で引っかかった行を表すクラスです。
     * <p>
     * 濾した結果
     * </p>
     */
    public static class ResultSet {
        ResultSet(File file, int lineNumber, String line) {
            this.file = file;
            this.lineNumber = lineNumber;
            this.line = line;
        }
        /** grep で引っかかったファイル */
        File file;
        /** grep で引っかかった行番号 */
        int lineNumber;
        /** grep で引っかかった行 */
        String line;
    }
    
    /** 濾したのを溜める */
    private List<ResultSet> results = new ArrayList<>();
    
    /** ひとつ濾します */
    private void grep(CharBuffer cb) {
        Matcher lm = linePattern.matcher(cb); // Line matcher
        Matcher pm = null; // Pattern matcher
        int lines = 0;
        while (lm.find()) {
            lines++;
            CharSequence cs = lm.group(); // The current line
            if (pm == null) {
                pm = pattern.matcher(cs);
            } else {
                pm.reset(cs);
            }
            if (pm.find()) {
                results.add(new ResultSet(file, lines, cs.toString()));
            }
            if (lm.end() == cb.limit()) {
                break;
            }
        }
    }
    
    /** 濾します */
    public List<ResultSet> exec() throws IOException {
        // Open the file and then get a channel from the stream
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        
        // Get the file's size and then map it into memory
        int size = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);
        
        // Decode the file into a char buffer
        CharBuffer cb = decoder.decode(bb);

        // Perform the search
        grep(cb);
        
        fis.close();

        return results;
    }

    /**
     * @param args 0: top directory, 1: grep pattern, 2: file pattern
     */
    public static void main(final String[] args) throws Exception {
        new RegexFileDigger(new FileDigger.FileDredger() {
            public void dredge(File file) throws IOException {
                for (Grep.ResultSet rs : new Grep(file, Pattern.compile(args[1])).exec()) {
System.out.print(rs.file + ":" + rs.lineNumber + ":" + rs.line);
                }
            }
        }, Pattern.compile(args[2])).dig(new File(args[0]));
    }
}

/* */
