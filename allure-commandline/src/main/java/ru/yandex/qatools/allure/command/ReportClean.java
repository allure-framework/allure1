package ru.yandex.qatools.allure.command;

import io.airlift.airline.Command;
import org.slf4j.cal10n.LocLogger;
import ru.yandex.qatools.allure.utils.DeleteVisitor;

import java.nio.file.Files;
import java.nio.file.Path;

import static ru.yandex.qatools.allure.logging.LogManager.getLogger;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_REPORT_CLEAN_REPORT_CLEANED;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
@Command(name = "clean", description = "Clean report")
public class ReportClean extends ReportCommand {

    private static final LocLogger LOGGER = getLogger(ReportClean.class);

    /**
     * Remove the report directory.
     */
    @Override
    protected void runUnsafe() throws Exception {
        Path reportDirectory = getReportDirectoryPath();
        Files.walkFileTree(reportDirectory, new DeleteVisitor());
        LOGGER.info(COMMAND_REPORT_CLEAN_REPORT_CLEANED, reportDirectory);
    }
}
