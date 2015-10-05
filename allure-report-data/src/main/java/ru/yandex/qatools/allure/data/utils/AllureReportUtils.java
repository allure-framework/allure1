package ru.yandex.qatools.allure.data.utils;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import ru.yandex.qatools.allure.data.ReportGenerationException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public final class AllureReportUtils {

    /**
     * Don't use instance of this class
     */
    AllureReportUtils() {
    }

    /**
     * Create directory with given name in specified directory.
     *
     * @param parent specified parent directory
     * @param name   given name for directory to create
     * @return created directory
     * @throws ReportGenerationException if can't create specified directory
     */
    public static Path createDirectory(Path parent, String name) {
        Path created = parent.resolve(name);
        try {
            return Files.createDirectories(created);
        } catch (IOException e) {
            throw new ReportGenerationException("Can't create data directory", e);
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
        try (FileOutputStream stream = new FileOutputStream(new File(directory, name))) {
            return serialize(stream, obj);
        } catch (IOException e) {
            throw new ReportGenerationException(e);
        }
    }

    /**
     * Serialize specified object to directory with specified name. Given output stream will be closed.
     *
     * @param obj object to serialize
     * @return number of bytes written to directory
     */
    public static int serialize(OutputStream stream, Object obj) {
        ObjectMapper mapper = createMapperWithJaxbAnnotationInspector();

        try (DataOutputStream data = new DataOutputStream(stream);
             OutputStreamWriter writer = new OutputStreamWriter(data, StandardCharsets.UTF_8)) {
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
}
