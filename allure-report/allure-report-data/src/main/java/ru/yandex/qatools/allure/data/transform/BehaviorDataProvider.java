package ru.yandex.qatools.allure.data.transform;

import ru.yandex.qatools.allure.data.AllureBehavior;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringReader;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtil.applyTransformations;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.13
 */
public class BehaviorDataProvider implements DataProvider {

    private static final String TEST_RUN_TO_FEATURES_1_XSL = "xsl/testrun-to-behavior-1.xsl";

    private static final String TEST_RUN_TO_FEATURES_2_XSL = "xsl/testrun-to-behavior-2.xsl";

    private static final String BEHAVIOR_JSON = "behavior.json";

    @Override
    public void provide(String testPack, File outputDirectory) {
        String allureFeaturesBody = applyTransformations(
                testPack,
                TEST_RUN_TO_FEATURES_1_XSL,
                TEST_RUN_TO_FEATURES_2_XSL
        );

        AllureBehavior allureBehavior = JAXB.unmarshal(
                new StringReader(allureFeaturesBody),
                AllureBehavior.class
        );

        serialize(outputDirectory, BEHAVIOR_JSON, allureBehavior);
    }
}
