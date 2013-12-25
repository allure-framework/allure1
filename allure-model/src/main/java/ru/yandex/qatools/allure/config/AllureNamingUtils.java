package ru.yandex.qatools.allure.config;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import ru.yandex.qatools.allure.model.AttachmentType;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/24/13
 */
public class AllureNamingUtils {

    private AllureNamingUtils() {
    }

    public static String generateTestSuiteFileName(String name) {
        AllureResultsConfig config = AllureResultsConfig.newInstance();
        return String.format("%s-%s.%s",
                name,
                config.getTestSuiteFileSuffix(),
                config.getTestSuiteFileExtension());
    }

    public static String generateTestSuiteFileName() {
        return generateTestSuiteFileName(UUID.randomUUID().toString());
    }

    public static Collection<File> listTestSuiteFiles(File... directory) {
        AllureResultsConfig config = AllureResultsConfig.newInstance();
        return listFiles(directory,
                new RegexFileFilter(config.getTestSuiteFileRegex()),
                CanReadFileFilter.CAN_READ);
    }

    public static String generateAttachmentFileName(String name, AttachmentType type) {
        AllureResultsConfig config = AllureResultsConfig.newInstance();
        return String.format("%s-%s.%s",
                name,
                config.getAttachmentFileSuffix(),
                type.toString().toLowerCase());
    }

    public static String generateAttachmentFileName(AttachmentType type) {
        return generateAttachmentFileName(UUID.randomUUID().toString(), type);
    }

    public static Collection<File> listAttachmentFiles(File... directories) {
        AllureResultsConfig config = AllureResultsConfig.newInstance();
        return listFiles(directories,
                new RegexFileFilter(config.getAttachmentFileRegex()),
                CanReadFileFilter.CAN_READ);
    }

    public static Collection<File> listFiles(File[] directories, IOFileFilter fileFilter, IOFileFilter dirFilter) {
        Collection<File> attachmentFiles = new ArrayList<>();
        for (File directory : directories) {
            Collection<File> attachmentsInDirectory = FileUtils.listFiles(directory,
                    fileFilter,
                    dirFilter);
            attachmentFiles.addAll(attachmentsInDirectory);
        }
        return attachmentFiles;
    }
}
