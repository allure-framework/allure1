package ru.yandex.qatools.allure.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/22/14
 */
public final class DummyReportGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyReportGenerator.class);

    DummyReportGenerator() {
    }

    /**
     * Generate Allure report data from directories with allure report results.
     *
     * @param args a list of directory paths. First (args.length - 1) arguments -
     *             results directories, last argument - the folder to generated data
     */
    public static void main(String[] args) {
        if (args.length < 2) { // NOSONAR
            LOGGER.error("There must be at least two arguments");
            return;
        }
        int lastIndex = args.length - 1;
        AllureReportGenerator reportGenerator = new AllureReportGenerator(
                getFiles(Arrays.copyOf(args, lastIndex))
        );
        reportGenerator.generate(new File(args[lastIndex]));
    }

    public static File[] getFiles(String[] paths) {
        List<File> files = new ArrayList<>();
        for (String path : paths) {
            files.add(new File(path));
        }
        return files.toArray(new File[files.size()]);
    }
}
