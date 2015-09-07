package ru.yandex.qatools.allure.command;

import ru.yandex.qatools.allure.logging.Message;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The exception indicates about some problems during Allure commandline execution.
 *
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
public class AllureCommandException extends Exception {

    private final Message logMessage;

    private final String[] logArgs;

    public AllureCommandException(Message logMessage, Path... logArgs) {
        this(logMessage, convert(logArgs));
    }

    public AllureCommandException(Message logMessage, String... logArgs) {
        this.logMessage = logMessage;
        this.logArgs = logArgs;
    }

    public Message getLogMessage() {
        return logMessage;
    }

    public Object[] getLogArgs() {
        return logArgs;
    }

    private static String[] convert(Path[] paths) {
        List<String> result = new ArrayList<>();
        for (Path path : paths) {
            result.add(path.toString());
        }
        return result.toArray(new String[result.size()]);
    }
}
