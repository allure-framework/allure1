package ru.yandex.qatools.allure.config;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.01.14
 */
public final class AllureModelUtils {
    private AllureModelUtils() {
    }

    public static Validator getAllureSchemaValidator() throws SAXException {
        String schemaFileName = AllureModelConfig.newInstance().getSchemaFileName();
        File schemaFile = new File(ClassLoader.getSystemResource(schemaFileName).getFile());
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaFile);
        return schema.newValidator();
    }
}
