package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import org.slf4j.cal10n.LocLogger;
import ru.yandex.qatools.allure.logging.Message;

import static ru.yandex.qatools.allure.logging.LogManager.getLogger;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
@Command(name = "version", description = "Display version")
public class AllureVersion extends AbstractCommand {

    private static final LocLogger LOGGER = getLogger(ReportOpen.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runUnsafe() {
        String toolVersion = getClass().getPackage().getImplementationVersion();
        LOGGER.info(Message.COMMAND_VERSION_INFO, toolVersion);
    }

}
