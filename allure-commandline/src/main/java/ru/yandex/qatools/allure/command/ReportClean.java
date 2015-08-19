package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import org.slf4j.cal10n.LocLogger;
import ru.yandex.qatools.allure.logging.Messages;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static ru.yandex.qatools.allure.logging.LogManager.getLogger;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
@Command(name = "clean", description = "Clean report")
public class ReportClean extends ReportCommand {

    private static final LocLogger LOGGER = getLogger(ReportClean.class);

    /**
     * Remove the report directory.
     *
     * @throws IOException if any occurs.
     */
    @Override
    protected void runUnsafe() throws IOException {
        Path reportDirectory = getReportDirectoryPath();
        Files.walkFileTree(reportDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                LOGGER.debug(Messages.COMMAND_REPORT_CLEAN_ITEM_DELETED, file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                LOGGER.debug(Messages.COMMAND_REPORT_CLEAN_ITEM_DELETED, dir);
                return FileVisitResult.CONTINUE;
            }
        });
        LOGGER.info(Messages.COMMAND_REPORT_CLEAN_REPORT_CLEANED, reportDirectory);
    }

}
