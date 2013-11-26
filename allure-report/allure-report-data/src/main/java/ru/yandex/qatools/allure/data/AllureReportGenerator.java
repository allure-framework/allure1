package ru.yandex.qatools.allure.data;

import ch.lambdaj.Lambda;
import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import javax.xml.bind.JAXB;
import java.io.*;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static ru.yandex.qatools.allure.data.utils.AllureReportSerializeUtils.*;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public class AllureReportGenerator extends ReportGenerator {

    private static final String CREATE_TEST_PACK_XSL = "xsl/create-test-pack.xsl";

    private static final String TEST_SUITES_MASK = ".+-testsuite\\.xml";

    private static final String ATTACHMENTS_MASK = ".+-attachment\\.\\w+";

    public AllureReportGenerator(File... inputDirectories) {
        super(inputDirectories);
    }

    @Override
    public void generate(File outputDirectory) {
        copyAttachments(inputDirectories, outputDirectory);

        ListFiles listFiles = createListFiles(inputDirectories);
        TestSuitesPack testSuitesPack = createTestSuitesPack(listFiles);
        TestCasesPack testCasesPack = createTestCasesPack(testSuitesPack);

//      remove unused data from test suites pack
        for (AllureTestSuite allureTestSuite : testSuitesPack.getTestSuites()) {
            allureTestSuite.getTestCases().clear();
        }

        writeListFiles(outputDirectory, listFiles);
        writeTestSuitesPack(outputDirectory, testSuitesPack);
        writeTestCasesPack(outputDirectory, testCasesPack);
    }

    public static File[] getAttachmentsFiles(File... dirs) {
        return AllureReportUtils.listFiles(dirs, ATTACHMENTS_MASK);
    }

    public static void copyAttachments(File[] dirs, File outputDirectory) {
        for (File attach : getAttachmentsFiles(dirs)) {
            try {
                FileUtils.copyFile(attach, new File(outputDirectory, attach.getName()));
            } catch (IOException ignored) {
            }
        }
    }

    public static ListFiles createListFiles(File... dirs) {
        File[] testSuitesFiles = AllureReportUtils.listFiles(dirs, TEST_SUITES_MASK);

        ListFiles listFiles = new ListFiles();
        for (File file : testSuitesFiles) {
            listFiles.getFiles().add(file.toURI().toString());
        }

        return listFiles;
    }

    public static TestSuitesPack createTestSuitesPack(ListFiles listFiles) {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(new ObjectFactory().createListFiles(listFiles), stringWriter);
        StringReader stringReader = new StringReader(stringWriter.toString());

        String testSuitesPackBody = AllureReportUtils.applyXslTransformation(
                AllureReportGenerator.class.getClassLoader().getResourceAsStream(CREATE_TEST_PACK_XSL),
                stringReader
        );
        return JAXB.unmarshal(
                new StringReader(testSuitesPackBody),
                TestSuitesPack.class
        );
    }

    public static TestCasesPack createTestCasesPack(TestSuitesPack testSuitesPack) {
        TestCasesPack testCasesPack = new TestCasesPack();
        testCasesPack.getTestCases().addAll(Lambda.<AllureTestCase>flatten(extract(
                testSuitesPack.getTestSuites(),
                on(AllureTestSuite.class).getTestCases()
        )));

        return testCasesPack;
    }
}
