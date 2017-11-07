/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 1, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package vavi.util.gnu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple framework for printing change lists produced by <code>Diff</code>.
 *
 * @see vavi.util.gnu.Diff
 * @author Stuart D. Gathman
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 1.20 000302 stuart add GPL <br>
 *          1.21 020430 nsano ignore white space between "" <br>
 *          1.22 030210 nsano use List instead of Vector <br>
 */
public class DiffUtil {

    // Output the differences with lines of context before and after (-c).
    // Output the differences in a unified context diff format (-u).
    // Output the diff as a forward ed script (-f).
    // Like -f, but output a count of changed lines in each "command" (-n).
    // Output merged #ifdef'd file (-D).
    // Output sdiff style (-y).

    /**
     * A Base class for printing edit scripts produced by Diff.
     * This class divides the change list into "hunks", and calls
     * <code>print_hunk</code> for each hunk. Various utility methods
     * are provided as well.
     */
    public static abstract class BasicPrinter {

        /** */
        protected BasicPrinter(Object[] a, Object[] b) {
            out = new PrintWriter(new OutputStreamWriter(System.out));
            file0 = a;
            file1 = b;
        }

        /**
         * Set to ignore certain kinds of lines when printing
         * an edit script. For example, ignoring blank lines or comments.
         */
        protected UnaryPredicate ignore = null;

        /**
         * Set to the lines of the files being compared.
         */
        protected Object[] file0, file1;

        /**
         * Divide SCRIPT into pieces by calling HUNKFUN and
         * print each piece with PRINTFUN.
         * Both functions take one arg, an edit script.
         *
         * PRINTFUN takes a subscript which belongs together (with a null
         * link at the end) and prints it.
         */
        public void print(Diff.Change script) {
            Diff.Change next = script;

            while (next != null) {
                Diff.Change t, end;

                // Find a set of changes that belong together.
                t = next;
                end = hunkfun(next);

                // Disconnect them from the rest of the changes,
                // making them a hunk, and remember the rest
                // for next iteration.
                next = end.link;
                end.link = null;
// if (DEBUG)
// debug_script(t);

                // Print this hunk.
                printHunk(t);

                // Reconnect the script so it will all be freed properly.
                end.link = next;
            }
            out.flush();
        }

        /**
         * Called with the tail of the script and returns the last link that belongs together with the start of the tail.
         */
        protected Diff.Change hunkfun(Diff.Change hunk) {
            return hunk;
        }

        /** */
        private int first0, last0, first1, last1, deletes, inserts;

        protected boolean hasDiff() {
            return deletes != 0 || inserts != 0;
        }

        protected boolean hasBoth() {
            return inserts != 0 && deletes != 0;
        }

        protected boolean hasDeletes() {
            return deletes != 0;
        }

        protected int getStartInserts() {
            return first1;
        }

        protected int getLastInserts() {
            return last1;
        }

        protected boolean hasInserts() {
            return inserts != 0;
        }

        protected int getStartDeletes() {
            return first0;
        }

        protected int getLastDeletes() {
            return last0;
        }

        /** */
        protected PrintWriter out;

        /**
         * Look at a hunk of edit script and report the range of lines
         * in each file
         * that it applies to. HUNK is the start of the hunk, which is a chain
         * of `struct change'. The first and last line numbers of file 0 are
         * stored in *FIRST0 and *LAST0, and likewise for file 1
         * in *FIRST1 and *LAST1.
         * Note that these are internal line numbers that count from 0.
         *
         * If no lines from file 0 are deleted, then FIRST0 is LAST0+1.
         *
         * Also set *DELETES nonzero if any lines of file 0 are deleted
         * and set *INSERTS nonzero if any lines of file 1 are inserted.
         * If only ignorable lines are inserted or deleted, both are
         * set to 0.
         */
        protected void analyzeHunk(Diff.Change hunk) {

            int l0 = 0, l1 = 0;
            boolean nontrivial = (ignore == null);

            int show_from = 0;
            int show_to = 0;

            int f0 = hunk.line0;
            int f1 = hunk.line1;

            for (Diff.Change next = hunk; next != null; next = next.link) {
                l0 = next.line0 + next.deleted - 1;
                l1 = next.line1 + next.inserted - 1;
                show_from += next.deleted;
                show_to += next.inserted;
                for (int i = next.line0; i <= l0 && !nontrivial; i++) {
                    if (!ignore.execute(file0[i])) {
                        nontrivial = true;
                    }
                }
                for (int i = next.line1; i <= l1 && !nontrivial; i++) {
                    if (!ignore.execute(file1[i])) {
                        nontrivial = true;
                    }
                }
            }

            first0 = f0;
            last0 = l0;
            first1 = f1;
            last1 = l1;

            // If all inserted or deleted lines are ignorable,
            // tell the caller to ignore this hunk.

            if (!nontrivial) {
                show_from = show_to = 0;
            }

            deletes = show_from;
            inserts = show_to;
        }

        /** */
        protected abstract void printHunk(Diff.Change hunk);

        /** */
        protected void print1Line(String pre, Object linbuf) {
            out.println(pre + linbuf.toString());
        }

        /**
         * Print a pair of line numbers with SEPCHAR, translated for file FILE.
         * If the two numbers are identical, print just one number.
         *
         * Args A and B are internal line numbers.
         * We print the translated (real) line numbers.
         */
        protected void printNumberRange(char sepchar, int a, int b) {
            // Note: we can have B < A in the case of a range of no lines.
            // In this case, we should print the line number before the range,
            // which is B.
            if (++b > ++a) {
                out.print("" + a + sepchar + b);
            } else {
                out.print(b);
            }
        }

        /** */
        public static char changeLetter(boolean inserts, boolean deletes) {
            if (inserts) {
                return 'd';
            } else if (deletes) {
                return 'a';
            } else {
                return 'c';
            }
        }
    }

    /**
     * Print a change list in the standard diff format.
     */
    public static class NormalPrinter extends BasicPrinter {

        public NormalPrinter(Object[] a, Object[] b) {
            super(a, b);
        }

        /**
         * Print a hunk of a normal diff.
         * This is a contiguous portion of a complete edit script,
         * describing changes in consecutive lines.
         */
        protected void printHunk(Diff.Change hunk) {

            /* Determine range of line numbers involved in each file. */
            analyzeHunk(hunk);
            if (!hasDiff()) {
                return;
            }

            /* Print out the line number header for this hunk */
            printNumberRange(',', getStartDeletes(), getLastDeletes());
            out.print(changeLetter(hasInserts(), hasDeletes()));
            printNumberRange(',', getStartInserts(), getLastInserts());
            out.println();

            /* Print the lines that the first file has. */
            if (hasDeletes()) {
                for (int i = getStartDeletes(); i <= getLastDeletes(); i++) {
                    print1Line("< ", file0[i]);
                }
            }

            if (hasBoth()) {
                out.println("---");
            }

            /* Print the lines that the second file has. */
            if (hasInserts()) {
                for (int i = getStartInserts(); i <= getLastInserts(); i++) {
                    print1Line("> ", file1[i]);
                }
            }
        }
    }

    /**
     * Prints an edit script in a format suitable for input to <code>ed</code>.
     * The edit script must be generated with the reverse option to
     * be useful as actual <code>ed</code> input.
     */
    public static class EdPrinter extends BasicPrinter {

        public EdPrinter(Object[] a, Object[] b) {
            super(a, b);
        }

        /** Print a hunk of an ed diff */
        protected void printHunk(Diff.Change hunk) {

            // Determine range of line numbers involved in each file.
            analyzeHunk(hunk);
            if (!hasDiff()) {
                return;
            }

            // Print out the line number header for this hunk
            printNumberRange(',', getStartDeletes(), getLastDeletes());
            out.println(changeLetter(hasInserts(), hasDeletes()));

            // Print new/changed lines from second file, if needed
            if (hasInserts()) {
                boolean inserting = true;
                for (int i = getStartInserts(); i <= getLastInserts(); i++) {
                    // Resume the insert, if we stopped.
                    if (!inserting) {
                        out.println(i - getStartInserts() + getStartDeletes() + "a");
                    }
                    inserting = true;

                    // If the file's line is just a dot, it would confuse `ed'.
                    // So output it with a double dot, and set the flag
                    // LEADING_DOT
                    // so that we will output another ed-command later
                    // to change the double dot into a single dot.

                    if (".".equals(file1[i])) {
                        out.println("..");
                        out.println(".");
                        // Now change that double dot to the desired
                        // single dot.
                        out.println(i - getStartInserts() + getStartDeletes() + 1 + "s/^\\.\\././");
                        inserting = false;
                    } else {
                        // Line is not `.', so output it unmodified.
                        print1Line("", file1[i]);
                    }
                }

                // End insert mode, if we are still in it.
                if (inserting) {
                    out.println(".");
                }
            }
        }
    }

    /**
     * Read a text file into an array of String.  This provides basic diff
     * functionality.  A more advanced diff utility will use specialized
     * objects to represent the text lines, with options to, for example,
     * convert sequences of whitespace to a single space for comparison
     * purposes.
     */
    public static String[] readLines(File file) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(file));
        List<String> s = new ArrayList<>();
        while (true) {
            String line = r.readLine();
            if (line == null) {
                break;
            }
            s.add(line);
        }
        r.close();
        String[] a = new String[s.size()];
        s.toArray(a);
        return a;
    }

    /**
     * Ignore white space between "".
     */
    public static String[] readLinesIgnoreWhiteSpace(File file) throws IOException {

        BufferedReader r = new BufferedReader(new FileReader(file));
        List<String> s = new ArrayList<>();

        //
        boolean isStringLiteral = false;

        // int ll = 1;
        while (true) {
            String line = r.readLine();
// System.err.println(ll++ + ": " + line);
            if (line == null) {
                break;
            }

            // ----

            line = line.trim();

            int p = 0;
            boolean[] isStringLiterals = new boolean[line.length()];
            while (true) {
                int c = line.indexOf('\"', p);
                if (c == -1) {
                    break;
                }
                if (c > 0 && line.charAt(c - 1) != '\\') {
                    for (int i = p; i < c; i++) {
                        isStringLiterals[i] = isStringLiteral;
                    }
                    isStringLiterals[c] = false;
                    isStringLiteral = !isStringLiteral;
                } else if (c == 0) {
                    isStringLiterals[0] = false;
                    isStringLiteral = !isStringLiteral;
                }
                if (c + 1 <= line.length()) {
                    p = c + 1;
                } else {
                    break;
                }
            }
            for (int i = p == -1 ? 0 : p; i < line.length(); i++) {
                isStringLiterals[i] = isStringLiteral;
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (isStringLiterals[i] || !Character.isWhitespace(c)) {
                    sb.append(c);
                }
            }

// System.err.println(sb);

            // ----

            s.add(sb.toString());
        }
        r.close();

        String[] a = new String[s.size()];
        s.toArray(a);
        return a;
    }
}

/* */
