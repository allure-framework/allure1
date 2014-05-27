package ru.yandex.qatools.allure.config;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/24/13
 */
public final class AllureNamingUtils {

    public static final String FILE_NAME_PATTERN = "%s-%s.%s";

    /**
     * Don't create instance
     */
    AllureNamingUtils() {
        throw new IllegalStateException("Don't instance AllureNamingUtils");
    }

    /**
     * Generate suite file name with specified name
     *
     * @param name specified name
     * @return file name \"{name}-testsuite.xml\"
     */
    public static String generateTestSuiteFileName(String name) {
        AllureConfig config = AllureConfig.newInstance();
        return String.format(FILE_NAME_PATTERN,
                name,
                config.getTestSuiteFileSuffix(),
                config.getTestSuiteFileExtension());
    }

    /**
     * Generate suite file name \"{randomUid}-testsutie.xml\"
     *
     * @return test suite file name
     */
    public static String generateTestSuiteFileName() {
        return generateTestSuiteFileName(UUID.randomUUID().toString());
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
