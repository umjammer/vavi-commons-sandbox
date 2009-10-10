/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.text;


/**
 * AnsiColor.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010730 nsano initial version <br>
 *          0.01 020516 nsano repackage <br>
 */
public enum AnsiColor {
    /** */
    NONE(0),
    /** */
    BLACK(30),
    /** */
    RED(31),
    /** */
    GREEN(32),
    /** */
    YELOW(33),
    /** */
    BLUE(34),
    /** */
    PURPLE(35),
    /** */
    CYAN(36),
    /** */
    WHITE(37),
    /** */
    DARK_BLACK(40),
    /** */
    DARK_RED(41),
    /** */
    DARK_GREEN(42),
    /** */
    DARK_YELOW(43),
    /** */
    DARK_BLUE(44),
    /** */
    DARK_PURPLE(45),
    /** */
    DARK_CYAN(46),
    /** */
    DARK_WHITE(47);

    /** */
    private int value;

    /** */
    private AnsiColor(int value) {
        this.value = value;
    }

    /**
     * This function uses ANSI escape sequence of setting colors. So the
     * application use this function will be not pure Java.
     * 
     * @param fore the fore ground ansi color
     * @param back the back ground ansi color
     */
    public static String getEscapeSequenceString(AnsiColor fore, AnsiColor back) {
        return (char) 0x1b + "[" + fore.value + ";" + back.value + "m";
    }

    /**
     * Returns the ANSI graphics color code string.
     * 
     * @param color the ansi color
     */
    public static String getEscapeSequenceString(AnsiColor color) {
        return getEscapeSequenceString(AnsiColor.NONE, color);
    }
}

/* */
