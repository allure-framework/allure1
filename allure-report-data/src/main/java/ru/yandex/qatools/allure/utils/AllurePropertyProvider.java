package ru.yandex.qatools.allure.utils;

import ru.qatools.properties.providers.SystemPropertyProvider;

import java.nio.file.Path;
import java.util.Properties;

/**
 * This property provider resolve properties from specified file
 * in given directories.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 05.10.15
 */
public class AllurePropertyProvider extends SystemPropertyProvider {

    private final Path[] inputDirectories;

    private final String fileName;

    /**
     * Creates an instance of provider.
     */
    public AllurePropertyProvider(String fileName, Path... inputDirectories) {
        this.inputDirectories = inputDirectories;
        this.fileName = fileName;
    }

    /**
     * Load properties from all files {@link #fileName} in {@link #inputDirectories}.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public Properties provide(ClassLoader classLoader, Class<?> beanClass) {
        Properties properties = AllureReportUtils.loadProperties(fileName, inputDirectories);
        properties.putAll(super.provide(classLoader, beanClass));
        return properties;
    }
}
