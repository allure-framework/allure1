package ru.yandex.qatools.allure.data.providers;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 *         <p/>
 *         Used to provide data to output directory. Find provides using Java SPI
 * @see ru.yandex.qatools.allure.data.utils.ServiceLoaderUtils
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
    long provide(File testPack, File[] inputDirectories, File outputDirectory);

    /**
     * Provide phase in which data provider will be executed.
     * Please see {@link DataProviderPhase} for possible values.
     *
     * @return ordering number
     */
    DataProviderPhase getPhase();

}
