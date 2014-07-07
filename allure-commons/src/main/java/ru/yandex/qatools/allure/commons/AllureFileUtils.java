package ru.yandex.qatools.allure.commons;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import ru.yandex.qatools.allure.config.AllureConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.06.14
 */
public final class AllureFileUtils {

    AllureFileUtils() {
        throw new IllegalStateException();
    }

    /**
     * Returns list of files matches {@link AllureConfig#testSuiteFileRegex} in specified directories
     *
     * @param directories to find
     * @return list of testSuite files in specified directories
     */
    public static Collection<File> listTestSuiteFiles(File... directories) {
        return listFilesByRegex(
                AllureConfig.newInstance().getTestSuiteFileRegex(),
                directories
        );
    }

    /**
     * Returns list of files matches {@link AllureConfig#attachmentFileRegex} in specified directories
     *
     * @param directories to find
     * @return list of attachment files in specified directories
     */
    public static Collection<File> listAttachmentFiles(File... directories) {
        return listFilesByRegex(
                AllureConfig.newInstance().getAttachmentFileRegex(),
                directories
        );
    }

    /**
     * Returns list of files matches specified regex in specified directories
     *
     * @param regex       to match file names
     * @param directories to find
     * @return list of files matches specified regex in specified directories
     */
    public static Collection<File> listFilesByRegex(String regex, File... directories) {
        return listFiles(directories,
                new RegexFileFilter(regex),
                CanReadFileFilter.CAN_READ);
    }

    /**
     * Returns list of files with specified name in given directories
     *
     * @param name        file name
     * @param directories to find
     * @return list of files matches specified regex in specified directories
     */
    public static Collection<File> listFilesByName(String name, File... directories) {
        return listFiles(directories,
                new NameFileFilter(name),
                CanReadFileFilter.CAN_READ);
    }

    /**
     * Returns list of files matches filters in specified directories
     *
     * @param directories which will using to find files
     * @param fileFilter  file filter
     * @param dirFilter   directory filter
     * @return list of files matches filters in specified directories
     */
    public static Collection<File> listFiles(File[] directories, IOFileFilter fileFilter, IOFileFilter dirFilter) {
        Collection<File> files = new ArrayList<>();
        for (File directory : directories) {
            Collection<File> filesInDirectory = FileUtils.listFiles(directory,
                    fileFilter,
                    dirFilter);
            files.addAll(filesInDirectory);
        }
        return files;
    }
}
