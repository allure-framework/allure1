package ru.yandex.qatools.allure.data.plugins;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.data.ReportGenerationException;
import ru.yandex.qatools.allure.data.io.ReportDirectory;
import ru.yandex.qatools.allure.data.io.ResultDirectory;

import java.io.File;
import java.io.IOException;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.listAttachmentFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.02.15
 */
public class AttachmentsProvider {

    @Inject
    @ResultDirectory
    private File[] inputDirectories;

    @Inject
    @ReportDirectory
    private File outputDirectory;

    /**
     * Copies all files from specified input directories to a new location
     * preserving the file date.
     *
     * @return size in bytes of copied attachments
     */
    public long provide() {
        long size = 0;
        for (File attach : listAttachmentFiles(inputDirectories)) {
            try {
                copyAttachment(attach, new File(outputDirectory, attach.getName()));
                size += attach.length();
            } catch (IOException e) {
                throw new ReportGenerationException(e);
            }
        }
        return size;
    }

    /**
     * Copies a file to a new location preserving the file date using
     * {@link org.apache.commons.io.FileUtils#copyFile(java.io.File, java.io.File)}
     * if source and destination are not some
     *
     * @param from source file
     * @param to   destination file
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException          if source or destination is invalid or an IO error
     *                              occurs during copying
     * @see org.apache.commons.io.FileUtils#copyFile(java.io.File, java.io.File)
     */
    public static void copyAttachment(File from, File to) throws IOException {
        if (!from.getCanonicalPath().equals(to.getCanonicalPath())) {
            FileUtils.copyFile(from, to);
        }
    }
}
