package ru.yandex.qatools.allure.data.utils;

import net.sf.saxon.Controller;
import net.sf.saxon.TransformerFactoryImpl;
import ru.yandex.qatools.allure.data.ReportGenerationException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.deleteFile;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.12.13
 */
public final class XslTransformationUtils {

    private XslTransformationUtils() {
    }

    public static File applyTransformations(File xml, String... xslTransformations) {
        File result = xml;
        for (String xslResourceName : xslTransformations) {
            File tmp = null;
            try (InputStream inputStream = new FileInputStream(result)) {
                tmp = applyTransformation(inputStream, xslResourceName);
            } catch (IOException e) {
                throw new ReportGenerationException(e);
            } finally {
                if (xml != result) {
                    deleteFile(result);
                }
                result = tmp;
            }
        }
        return result;
    }

    public static File applyTransformation(InputStream xml, String xslResourceName) {
        try {
            File result = Files.createTempFile("xsl-transform", ".xml").toFile();
            try (Writer resultWriter = new OutputStreamWriter(new FileOutputStream(result), StandardCharsets.UTF_8)) {
                applyTransformation(xml, xslResourceName, resultWriter);
                return result;
            }
        } catch (IOException e) {
            throw new ReportGenerationException(e);
        }
    }

    public static void applyTransformation(InputStream xml, String xslResourceName, Writer resultWriter) {
        URL url = XslTransformationUtils.class.getClassLoader().getResource(xslResourceName);
        if (url == null) {
            throw new ReportGenerationException("Can't find resource " + xslResourceName);
        }

        try (InputStream inputStream = url.openStream()) {
            applyTransformation(new StreamSource(xml), new StreamSource(inputStream, url.toString()), resultWriter);
        } catch (IOException e) {
            throw new ReportGenerationException(e);
        }
    }

    public static void applyTransformation(Source xml, Source xsl, Writer resultWriter) {
        Controller transformer = null;
        try {
            transformer = (Controller) new TransformerFactoryImpl().newTransformer(xsl);
            Result result = new StreamResult(resultWriter);
            transformer.transform(xml, result);
        } catch (TransformerException e) {
            throw new ReportGenerationException(e);
        } finally {
            if (transformer != null) {
                transformer.reset();
                transformer.clearParameters();
                transformer.clearDocumentPool();
                System.gc();
            }
        }
    }
}
