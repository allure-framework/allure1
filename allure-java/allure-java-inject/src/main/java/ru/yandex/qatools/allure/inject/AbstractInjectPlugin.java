package ru.yandex.qatools.allure.inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import static com.google.common.collect.Iterators.concat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.11.13
 */
@SuppressWarnings("unused")
public abstract class AbstractInjectPlugin extends AbstractAspectJPluginExecutor {

    protected static final String CLASS_FILTER = ".*\\.class";

    @Parameter(defaultValue = "${project.build.testOutputDirectory}")
    protected File testOutputDirectory;

    @Parameter(defaultValue = "${project.build.outputDirectory}")
    protected File outputDirectory;

    protected void inject() throws MojoExecutionException, MojoFailureException {
        getLog().info(String.format("Analyse test directory %s", testOutputDirectory));

        Iterator<File> testClasses = getClassFiles(testOutputDirectory);
        Iterator<File> classes = getClassFiles(outputDirectory);

        if (!testClasses.hasNext()) {
            getLog().info("There is no test classes for injecting.");
        }

        if (!classes.hasNext()) {
            getLog().info("There is no classes for injecting.");
        }

        Iterator<File> files = concat(classes, testClasses);

        while (files.hasNext()) {
            File testClassFile = files.next();
            getLog().info("Processing file: " + testClassFile);
            try {
                byte[] bytes = getInjector().inject(new FileInputStream(testClassFile));
                FileUtils.writeByteArrayToFile(testClassFile, bytes);
            } catch (IOException e) {
                throw new MojoExecutionException("IO trouble while reading file " + testClassFile, e);
            }
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
        inject();
    }

    protected static Iterator<File> getClassFiles(File directory) {
        if (directory.exists() && directory.isDirectory() && directory.canRead()) {
            return FileUtils.iterateFiles(directory,
                    new RegexFileFilter(CLASS_FILTER), CanWriteFileFilter.CAN_WRITE);
        } else {
            return Collections.<File>emptyList().iterator();
        }
    }

    protected abstract Injector getInjector();
}
