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
public final class AllureNamingUtils {

    public static final String FILE_NAME_PATTER = "%s-%s.%s";

    private AllureNamingUtils() {
    }

    public static String generateTestSuiteFileName(String name) {
        AllureConfig config = AllureConfig.newInstance();
        return String.format(FILE_NAME_PATTER,
                name,
                config.getTestSuiteFileSuffix(),
                config.getTestSuiteFileExtension());
    }

    public static String generateTestSuiteFileName() {
        return generateTestSuiteFileName(UUID.randomUUID().toString());
    }

    public static Collection<File> listTestSuiteFiles(File... directory) {
        AllureConfig config = AllureConfig.newInstance();
        return listFiles(directory,
                new RegexFileFilter(config.getTestSuiteFileRegex()),
                CanReadFileFilter.CAN_READ);
    }

    public static String generateAttachmentFileName(String name, AttachmentType type) {
        AllureConfig config = AllureConfig.newInstance();
        return String.format(FILE_NAME_PATTER,
                name,
                config.getAttachmentFileSuffix(),
                type.toString().toLowerCase());
    }

    public static Collection<File> listAttachmentFiles(File... directories) {
        AllureConfig config = AllureConfig.newInstance();
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
