package ru.yandex.qatools.allure.data.providers;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public interface DataProvider {

    /**
     * Provide some info to specified directory
     *
     * @param testPack        marshaled {@link ru.yandex.qatools.allure.data.AllureTestRun}
     *                        with info about tests
     * @param outputDirectory specified directory
     * @return number of bytes written to outputDirectory
     */
    long provide(String testPack, File outputDirectory);

}
