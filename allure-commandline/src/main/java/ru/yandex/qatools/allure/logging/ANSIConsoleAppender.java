package ru.yandex.qatools.allure.logging;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
public class ANSIConsoleAppender extends ConsoleAppender {

    private static final int NORMAL = 0;
    private static final int BRIGHT = 1;
    private static final int FOREGROUND_RED = 31;
    private static final int FOREGROUND_GREEN = 32;
    private static final int FOREGROUND_YELLOW = 33;
    private static final int FOREGROUND_CYAN = 36;

    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private static final char SEPARATOR = ';';
    private static final String END_COLOUR = PREFIX + SUFFIX;

    private static final String FATAL_COLOUR = color(FOREGROUND_RED, true);
    private static final String ERROR_COLOUR = color(FOREGROUND_RED);
    private static final String WARN_COLOUR = color(FOREGROUND_YELLOW);
    private static final String INFO_COLOUR = color(FOREGROUND_GREEN);
    private static final String DEBUG_COLOUR = color(FOREGROUND_CYAN);

    /**
     * Wraps the ANSI control characters around the
     * output from the super-class Appender.
     */

    @Override
    protected void subAppend(LoggingEvent event) {
        if (!Level.INFO.equals(event.getLevel())) {
            this.qw.write(getColour(event.getLevel()));
            this.qw.write(event.getLevel().toString() + ": ");
            this.qw.write(END_COLOUR);
        }
        super.subAppend(event);
    }

    /**
     * Get the appropriate control characters to change
     * the colour for the specified logging level.
     */
    private String getColour(Level level) {
        switch (level.toInt()) {
            case Priority.FATAL_INT:
                return FATAL_COLOUR;
            case Priority.ERROR_INT:
                return ERROR_COLOUR;
            case Priority.WARN_INT:
                return WARN_COLOUR;
            case Priority.DEBUG_INT:
                return DEBUG_COLOUR;
            default:
                return INFO_COLOUR;
        }
    }

    /**
     * Shortcut for {@link #color(int, boolean)}.
     */
    private static String color(int color) {
        return color(color, false);
    }

    /**
     * Create control characters for given colors.
     */
    private static String color(int color, boolean isBright) {
        return PREFIX + (isBright ? BRIGHT : NORMAL) + SEPARATOR + color + SUFFIX;
    }
}
