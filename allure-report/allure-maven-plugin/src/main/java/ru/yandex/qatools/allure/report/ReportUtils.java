package ru.yandex.qatools.allure.report;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.11.13
 */
public final class ReportUtils {

    private ReportUtils() {
    }

    public static URL convertStringToURL(final String from) throws MalformedURLException {
        return new File(from).toURI().toURL();
    }

    public static URL[] getRuntimeClasspathElements(final MavenProject project)
            throws DependencyResolutionRequiredException, MalformedURLException {
        List<URL> result = new ArrayList<>();

        for (String element : project.getRuntimeClasspathElements()) {
            result.add(convertStringToURL(element));
        }
        return result.toArray(new URL[result.size()]);
    }

    public static URLClassLoader createURLClassLoader(final MavenProject project)
            throws MalformedURLException, DependencyResolutionRequiredException {
        return new URLClassLoader(
                getRuntimeClasspathElements(project),
                Thread.currentThread().getContextClassLoader()
        );
    }

    public static Class<?> getReportGeneratorClass(final MavenProject project, final String reportGeneratorClassName)
            throws ClassNotFoundException, MalformedURLException, DependencyResolutionRequiredException {
        URLClassLoader classLoader = createURLClassLoader(project);
        return classLoader.loadClass(reportGeneratorClassName);
    }
}
