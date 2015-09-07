package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import org.slf4j.cal10n.LocLogger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static ru.yandex.qatools.allure.logging.LogManager.getLogger;
import static ru.yandex.qatools.allure.logging.Message.*;

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

    /**
     * The visitor deletes files and directories.
     */
    private static class DeleteVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            LOGGER.debug(COMMAND_REPORT_CLEAN_ITEM_DELETED, file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            LOGGER.debug(COMMAND_REPORT_CLEAN_ITEM_DELETED, dir);
            return FileVisitResult.CONTINUE;
        }
    }
}
