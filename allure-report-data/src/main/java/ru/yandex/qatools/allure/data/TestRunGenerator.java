package ru.yandex.qatools.allure.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.StringWriter;
import java.util.Collection;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;
import static ru.yandex.qatools.allure.config.AllureModelUtils.getAllureSchemaValidator;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtils.applyTransformations;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.12.13
 */

public class TestRunGenerator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String SUITES_TO_TEST_RUN_1_XSL = "xsl/suites-to-testrun-1.xsl";

    public static final String SUITES_TO_TEST_RUN_2_XSL = "xsl/suites-to-testrun-2.xsl";

    public static final String SUITES_TO_TEST_RUN_3_XSL = "xsl/suites-to-testrun-3.xsl";

    private final ListFiles listFiles;

    private final boolean validateXML;

    public TestRunGenerator(boolean validateXML, File... dirs) {
        Collection<File> testSuitesFiles = listTestSuiteFiles(dirs);

        listFiles = createListFiles(testSuitesFiles);
        this.validateXML = validateXML;

    }

    private ListFiles createListFiles(Collection<File> files) {
        ListFiles lf = new ListFiles();
        for (File file : files) {
            try {
                if (validateXML) {
                    Validator validator = getAllureSchemaValidator();
                    validator.validate(new StreamSource(file));
                }
                lf.getFiles().add(file.toURI().toString());
            } catch (Exception e) {
                logger.error("File " + file + " skipped.", e);
            }
        }
        return lf;
    }

    private String listFilesToString(final ListFiles listFiles) {
        final StringWriter stringWriter = new StringWriter();
        JAXB.marshal(new ObjectFactory().createListFiles(listFiles), stringWriter);
        return stringWriter.toString();
    }

    public String generate() {
        String suiteFiles = listFilesToString(listFiles);
        return applyTransformations(
                suiteFiles,
                SUITES_TO_TEST_RUN_1_XSL,
                SUITES_TO_TEST_RUN_2_XSL,
                SUITES_TO_TEST_RUN_3_XSL
        );
    }

    @SuppressWarnings("unused")
    public ListFiles getListFiles() {
        return listFiles;
    }
}
