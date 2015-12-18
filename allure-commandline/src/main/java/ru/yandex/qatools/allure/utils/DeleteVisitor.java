package ru.yandex.qatools.allure.utils;

import org.slf4j.cal10n.LocLogger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static ru.yandex.qatools.allure.logging.LogManager.getLogger;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_REPORT_CLEAN_ITEM_DELETED;

/**
 * The visitor deletes files and directories.
 */
public class DeleteVisitor extends SimpleFileVisitor<Path> {

    private static final LocLogger LOGGER = getLogger(DeleteVisitor.class);

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