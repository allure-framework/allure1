package ru.yandex.qatools.allure.data;

import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.data.transform.*;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public class AllureReportGenerator extends ReportGenerator {

    private static final String ATTACHMENTS_MASK = ".+-attachment\\.\\w+";

    private List<TestRunTransformer> transformers = new ArrayList<>();

    public AllureReportGenerator(File... inputDirectories) {
        super(inputDirectories);
    }

    @Override
    public void generate(File outputDirectory) {
        registerTransformers(
                new XUnitTransformer(),
                new GraphTransformer(),
                new TestCasesTransformer(),
                new BehaviorTransformer()
        );

        copyAttachments(inputDirectories, outputDirectory);

        String testRun = new TestSuiteFiles(inputDirectories).generateTestRun();

        for (TestRunTransformer transformer : transformers) {
            transformer.transform(testRun, outputDirectory);
        }

    }

    public void registerTransformers(TestRunTransformer... ts) {
        Collections.addAll(transformers, ts);
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
}
