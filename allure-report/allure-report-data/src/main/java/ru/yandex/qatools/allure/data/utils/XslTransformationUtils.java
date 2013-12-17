package ru.yandex.qatools.allure.data.utils;

import net.sf.saxon.TransformerFactoryImpl;
import org.apache.commons.io.IOUtils;
import ru.yandex.qatools.allure.data.ReportGenerationException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.12.13
 */
public final class XslTransformationUtils {

    private XslTransformationUtils() {
    }

    public static String applyTransformations(String xml, String... xslPaths) {
        String result = xml;
        for (String xsl : xslPaths) {
            result = applyTransformation(result, xsl);
        }
        return result;
    }

    public static String applyTransformation(String xml, String xslPath) {
        return applyTransformation(
                XslTransformationUtils.class.getClassLoader().getResourceAsStream(xslPath),
                IOUtils.toInputStream(xml)
        );
    }

    public static String applyTransformation(InputStream xml, InputStream xsl) {
        Source xslSource = new StreamSource(xsl);
        Source xmlSource = new StreamSource(xml);
        return applyTransformation(xslSource, xmlSource);
    }

    public static String applyTransformation(Source xml, Source xsl) {
        try {

            Transformer transformer = new TransformerFactoryImpl().newTransformer(xsl);
            StringWriter resultWriter = new StringWriter();
            Result result = new StreamResult(resultWriter);
            transformer.transform(xml, result);
            return resultWriter.toString();
        } catch (TransformerException e) {
            throw new ReportGenerationException(e);
        }
    }

}
