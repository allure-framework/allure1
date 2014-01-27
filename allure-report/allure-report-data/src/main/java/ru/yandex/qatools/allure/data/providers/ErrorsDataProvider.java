package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureErrors;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringReader;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtils.applyTransformation;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class ErrorsDataProvider implements DataProvider {

    private static final String TEST_RUN_TO_ERRORS_XSL = "xsl/testrun-to-errors.xsl";

    public static final String ERRORS_JSON = "errors.json";

    @Override
    public void provide(String testPack, File outputDirectory) {
        String allureErrorsBody = applyTransformation(testPack, TEST_RUN_TO_ERRORS_XSL);

        AllureErrors allureErrors = JAXB.unmarshal(
                new StringReader(allureErrorsBody),
                AllureErrors.class
        );

        serialize(outputDirectory, ERRORS_JSON, allureErrors);
    }
}
