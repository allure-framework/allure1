package ru.yandex.qatools.allure.data;

import javax.xml.bind.JAXB;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.yandex.qatools.allure.config.AllureModelUtils.getAllureSchemaValidator;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.listTestSuiteFiles;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtils.applyTransformations;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.12.13
 */

public class TestSuiteFiles {

    public static final String SUITES_TO_TEST_RUN_1_XSL = "xsl/suites-to-testrun-1.xsl";

    public static final String SUITES_TO_TEST_RUN_2_XSL = "xsl/suites-to-testrun-2.xsl";

    public static final String SUITES_TO_TEST_RUN_3_XSL = "xsl/suites-to-testrun-3.xsl";

    private final String suiteFiles;
    
    private final List<File> skippedSuiteFiles = new ArrayList<>();

    public TestSuiteFiles(final File... dirs) {
        final Collection<File> testSuitesFiles = listTestSuiteFiles(dirs);

        final ListFiles listFiles = createListFiles(testSuitesFiles);
        suiteFiles = listFilesToString(listFiles);

    }

    private ListFiles createListFiles(final Collection<File> files) {
        final ListFiles listFiles = new ListFiles();
        Validator validator;
        for (final File file : files) {
            try {
                validator = getAllureSchemaValidator();
                validator.validate(new StreamSource(file));
                listFiles.getFiles().add(file.toURI().toString());
            } catch (Exception e) {
                skippedSuiteFiles.add(file);
            }
        }
        return listFiles;
    }

    private String listFilesToString(final ListFiles listFiles) {
        final StringWriter stringWriter = new StringWriter();
        JAXB.marshal(new ObjectFactory().createListFiles(listFiles), stringWriter);
        return stringWriter.toString();
    }

    public String generateTestRun() {
        return applyTransformations(
                suiteFiles,
                SUITES_TO_TEST_RUN_1_XSL,
                SUITES_TO_TEST_RUN_2_XSL,
                SUITES_TO_TEST_RUN_3_XSL
        );
    }

    public List<File> getSkippedSuiteFiles() {
        return skippedSuiteFiles;
    }
}
