package ru.yandex.qatools.allure.command;

import io.airlift.command.Option;
import io.airlift.command.OptionType;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.cal10n.LocLogger;
import ru.qatools.properties.PropertyLoader;
import ru.yandex.qatools.allure.CommandProperties;

import static ru.yandex.qatools.allure.command.ExitCode.GENERIC_ERROR;
import static ru.yandex.qatools.allure.command.ExitCode.NO_ERROR;
import static ru.yandex.qatools.allure.logging.LogManager.getLogger;
import static ru.yandex.qatools.allure.logging.Message.COMMANDLINE_ERROR;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_ALLURE_COMMAND_ABORTED;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
public abstract class AbstractCommand implements AllureCommand {

    private static final LocLogger LOGGER = getLogger(AbstractCommand.class);

    protected static final CommandProperties PROPERTIES =
            PropertyLoader.newInstance().populate(CommandProperties.class);

    private ExitCode exitCode = NO_ERROR;

    @Option(name = {"-v", "--verbose"}, type = OptionType.GLOBAL,
            description = "Switch on the verbose mode.")
    protected boolean verbose = false;

    @Option(name = {"-q", "--quiet"}, type = OptionType.GLOBAL,
            description = "Switch on the quiet mode.")
    protected boolean quiet = false;

    protected abstract void runUnsafe() throws Exception; //NOSONAR

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        setLogLevel();
        try {
            runUnsafe();
        } catch (AllureCommandException e) { //NOSONAR
            LOGGER.error(e.getLogMessage(), e.getLogArgs());
            setExitCode(GENERIC_ERROR);
        } catch (Exception e) {
            LOGGER.error(COMMAND_ALLURE_COMMAND_ABORTED, e);
            setExitCode(GENERIC_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExitCode getExitCode() {
        return exitCode;
    }

    protected void setExitCode(ExitCode exitCode) {
        this.exitCode = exitCode;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Configure logger with needed level {@link #getLogLevel()}.
     */
    private void setLogLevel() {
        LogManager.getRootLogger().setLevel(getLogLevel());
    }

    /**
     * Get log level depends on provided client parameters such as verbose and quiet.
     */
    private Level getLogLevel() {
        return isQuiet() ? Level.OFF : isVerbose() ? Level.DEBUG : Level.INFO;
    }

}
