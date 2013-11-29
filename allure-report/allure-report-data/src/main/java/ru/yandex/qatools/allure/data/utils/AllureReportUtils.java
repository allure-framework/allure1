package ru.yandex.qatools.allure.data.utils;

import net.sf.saxon.TransformerFactoryImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import ru.yandex.qatools.allure.data.ReportGenerationException;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
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

    public static String applyXslTransformation(InputStream xslFile, InputStream xmlFile) {
        Source xslSource = new StreamSource(xslFile);
        Source xmlSource = new StreamSource(xmlFile);
        return applyXslTransformation(xslSource, xmlSource);
    }

    public static String applyXslTransformation(InputStream xslFile, StringReader xmlFile) {
        Source xslSource = new StreamSource(xslFile);
        Source xmlSource = new StreamSource(xmlFile);
        return applyXslTransformation(xslSource, xmlSource);
    }

    public static String applyXslTransformation(Source xslSource, Source xmlSource) {
        try {

            Transformer transformer = new TransformerFactoryImpl().newTransformer(xslSource);
            StringWriter resultWriter = new StringWriter();
            Result result = new StreamResult(resultWriter);
            transformer.transform(xmlSource, result);
            return resultWriter.toString();
        } catch (TransformerException e) {
            throw new ReportGenerationException(e);
        }
    }
}
