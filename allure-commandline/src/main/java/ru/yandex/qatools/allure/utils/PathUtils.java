package ru.yandex.qatools.allure.utils;

import ru.qatools.properties.PropertyLoader;
import ru.yandex.qatools.allure.CommandProperties;

import java.nio.file.Path;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
public class PathUtils {

    private static final CommandProperties PROPERTIES = PropertyLoader.newInstance().populate(CommandProperties.class);

    private PathUtils() {
    }

    /**
     * Returns the path to java executable.
     *
     * @return the path to java executable.
     * @throws NullPointerException if JAVA_HOME environment variable is not set.
     */
    public static Path getJavaExecutablePath() {
        Path javaHome = PROPERTIES.getJavaHome();
        if (javaHome == null) {
            throw new NullPointerException("'java.home' is not set");
        }
        return PROPERTIES.getJavaHome().resolve("bin/java");
    }
}
