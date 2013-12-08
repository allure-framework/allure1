package ru.yandex.qatools.allure.data.transform;

import ru.yandex.qatools.allure.data.AllureXUnit;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringReader;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtil.applyTransformation;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class XUnitTransformer implements TestRunTransformer {

    private static final String TEST_RUN_TO_XUNIT_XSL = "xsl/testrun-to-xunit.xsl";

    public static final String XUNIT_JSON = "xunit.json";

    @Override
    public void transform(String testPackXml, File outputDirectory) {
        String allureXUnitBody = applyTransformation(testPackXml, TEST_RUN_TO_XUNIT_XSL);

        AllureXUnit allureXUnit = JAXB.unmarshal(
                new StringReader(allureXUnitBody),
                AllureXUnit.class
        );

        serialize(outputDirectory, XUNIT_JSON, allureXUnit);
    }
}
