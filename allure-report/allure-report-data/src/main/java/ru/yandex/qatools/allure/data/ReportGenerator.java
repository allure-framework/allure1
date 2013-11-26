package ru.yandex.qatools.allure.data;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.11.13
 */
public abstract class ReportGenerator {

    protected File[] inputDirectories;

    public ReportGenerator(File... inputDirectories) {
        this.inputDirectories = inputDirectories;
    }

    public abstract void generate(File outputDirectory);

}
