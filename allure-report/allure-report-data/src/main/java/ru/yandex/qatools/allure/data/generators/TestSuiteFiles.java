package ru.yandex.qatools.allure.data.generators;

import ru.yandex.qatools.allure.data.ListFiles;
import ru.yandex.qatools.allure.data.ObjectFactory;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringWriter;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.applyXslTransformation;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.12.13
 */

public class TestSuiteFiles {

    private static final String TEST_SUITES_MASK = ".+-testsuite\\.xml";

    private static final String CONCAT_SUITE_FILES_XSL = "xsl/concat-suite-files.xsl";

    private static final String CREATE_ALLURE_TEST_RUN_XSL = "xsl/create-allure-test-run.xsl";


    private String suiteFiles;

    public TestSuiteFiles(File... dirs) {
        File[] testSuitesFiles = AllureReportUtils.listFiles(dirs, TEST_SUITES_MASK);

        ListFiles listFiles = createListFiles(testSuitesFiles);
        this.suiteFiles = listFilesToString(listFiles);

    }

    private static ListFiles createListFiles(File... files) {
        ListFiles listFiles = new ListFiles();
        for (File file : files) {
            listFiles.getFiles().add(file.toURI().toString());
        }
        return listFiles;
    }

    private static String listFilesToString(ListFiles listFiles) {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(new ObjectFactory().createListFiles(listFiles), stringWriter);
        return stringWriter.toString();
    }

    public TestRun generateTestRun() {
        String first = applyXslTransformation(
                CONCAT_SUITE_FILES_XSL,
                suiteFiles
        );

        String second = AllureReportUtils.applyXslTransformation(
                CREATE_ALLURE_TEST_RUN_XSL,
                first
        );

        return new TestRun(second);
    }
}
