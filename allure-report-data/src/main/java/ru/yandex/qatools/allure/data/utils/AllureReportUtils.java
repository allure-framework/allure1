package ru.yandex.qatools.allure.data.utils;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.data.AllureReportInfo;
import ru.yandex.qatools.allure.data.ReportGenerationException;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.listAttachmentFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public final class AllureReportUtils {

    private static final String REPORT_FILE_NAME = "report.json";

    /**
     * Don't use instance of this class
     */
    AllureReportUtils() {
    }

    /**
     * Serialize specified object to directory with specified name.
     *
     * @param directory write to
     * @param name      serialize object with specified name
     * @param obj       object to serialize
     * @return number of bytes written to directory
     */
    public static int serialize(final File directory, String name, Object obj) {
        try {
            ObjectMapper mapper = createMapperWithJaxbAnnotationInspector();
            DataOutputStream data = createDataOutputStream(directory, name);

            OutputStreamWriter writer = new OutputStreamWriter(data, StandardCharsets.UTF_8);
            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, obj);
            return data.size();
        } catch (IOException e) {
            throw new ReportGenerationException(e);
        }
    }

    /**
     * Create Jackson mapper with {@link JaxbAnnotationIntrospector}
     *
     * @return {@link com.fasterxml.jackson.databind.ObjectMapper}
     */
    public static ObjectMapper createMapperWithJaxbAnnotationInspector() {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector annotationInspector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        mapper.getSerializationConfig().with(annotationInspector);
        return mapper;
    }

    /**
     * Create Data Output Stream for file with specified name in specified directory
     *
     * @param directory to find file
     * @param name      file to find
     * @return Created {@link java.io.DataOutputStream}
     * @throws FileNotFoundException
     */
    public static DataOutputStream createDataOutputStream(File directory, String name) throws FileNotFoundException {
        return new DataOutputStream(new FileOutputStream(new File(directory, name)));
    }

    /**
     * Copies all files from specified input directories to a new location
     * preserving the file date.
     *
     * @param inputDirectories input directories
     * @param outputDirectory  output directory
     * @return size in bytes of copied attachments
     */
    public static long copyAttachments(File[] inputDirectories, File outputDirectory) {
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

    /**
     * Serialize {@link ru.yandex.qatools.allure.data.AllureReportInfo}  to
     * specified directory
     *
     * @param info            to serialize
     * @param outputDirectory output directory
     */
    public static void writeAllureReportInfo(AllureReportInfo info, File outputDirectory) {
        serialize(outputDirectory, REPORT_FILE_NAME, info);
    }
}
