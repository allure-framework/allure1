package ru.yandex.qatools.allure.data.utils;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import ru.yandex.qatools.allure.data.ReportGenerationException;

import java.io.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public final class AllureReportUtils {

    private AllureReportUtils() {
    }

    public static void serialize(final File directory, String name, Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AnnotationIntrospector ai = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
            mapper.getSerializationConfig().with(ai);

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(directory, name), obj);
        } catch (IOException e) {
            throw new ReportGenerationException(e);
        }
    }
}
