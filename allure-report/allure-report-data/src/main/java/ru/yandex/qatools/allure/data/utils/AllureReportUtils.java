package ru.yandex.qatools.allure.data.utils;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import ru.yandex.qatools.allure.data.ReportGenerationException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public final class AllureReportUtils {

    private AllureReportUtils() {
    }

    public static File[] listFiles(File[] dirs, String regex) {
        List<File> result = new ArrayList<>();
        for (File input : dirs) {
            Collections.addAll(result, listFiles(input, regex));
        }
        return FileUtils.convertFileCollectionToFileArray(result);
    }

    public static File[] listFiles(File dir, String regex) {
        if (dir.exists() && dir.isDirectory() && dir.canRead()) {
            return FileUtils.convertFileCollectionToFileArray(FileUtils.listFiles(dir,
                    getRegexpFileFilter(regex),
                    CanReadFileFilter.CAN_READ)
            );
        } else {
            return new File[]{};
        }
    }

    public static IOFileFilter getRegexpFileFilter(String regex) {
        return new AndFileFilter(CanReadFileFilter.CAN_READ, new RegexFileFilter(regex));
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
