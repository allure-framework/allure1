package ru.yandex.qatools.allure.data.utils;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import ru.yandex.qatools.allure.data.AllureReportInfo;
import ru.yandex.qatools.allure.data.ReportGenerationException;

import javax.xml.bind.JAXB;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

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
     * Try to delete given file. Deletes the file or directory denoted by this abstract pathname.
     * If this pathname denotes a directory, then the directory must be empty in
     * order to be deleted.
     * @param file to delete
     * @return true if file deleted successfully
     */
    public static boolean deleteFile(File file) {
        return file != null && file.exists() && file.delete();
    }

    /**
     * Create directory with given name in specified directory. Check created directory using
     * {@link #checkDirectory(java.io.File)}
     *
     * @param parent specified parent directory
     * @param name   given name for directory to create
     * @return created directory
     * @throws ReportGenerationException if can't create specified directory
     */
    public static File createDirectory(File parent, String name) {
        File created = new File(parent, name);
        checkDirectory(created);
        return created;
    }

    /**
     * If directory doesn't exists try to create it.
     *
     * @param directory given directory to check
     * @throws ReportGenerationException if can't create specified directory
     */
    public static void checkDirectory(File directory) {
        if (!(directory.exists() || directory.mkdirs())) {
            throw new ReportGenerationException(
                    String.format("Can't create data directory <%s>", directory.getAbsolutePath())
            );
        }
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
