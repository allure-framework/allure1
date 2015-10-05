package ru.yandex.qatools.allure;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.09.15
 */
public class DummyConfig implements AllureConfig {

    private final Path resultsDirectory;

    public DummyConfig(File resultsDirectory) {
        this(resultsDirectory.toPath());
    }

    public DummyConfig(Path resultsDirectory) {
        this.resultsDirectory = resultsDirectory;
    }

    @Override
    public String getRemoveAttachmentsPattern() {
        return "a^";
    }

    @Override
    public Path getResultsDirectory() {
        return resultsDirectory;
    }

    @Override
    public Charset getAttachmentsEncoding() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public int getMaxTitleLength() {
        return 120;
    }
}
