package ru.yandex.qatools.allure.data;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringWriter;
import java.util.Collection;

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

    private String suiteFiles;

    public TestSuiteFiles(File... dirs) {
        Collection<File> testSuitesFiles = listTestSuiteFiles(dirs);

        ListFiles listFiles = createListFiles(testSuitesFiles);
        suiteFiles = listFilesToString(listFiles);

    }

    private ListFiles createListFiles(Collection<File> files) {
        ListFiles listFiles = new ListFiles();
        for (File file : files) {
            listFiles.getFiles().add(file.toURI().toString());
        }
        return listFiles;
    }

    private String listFilesToString(ListFiles listFiles) {
        StringWriter stringWriter = new StringWriter();
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
}
