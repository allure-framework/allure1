package ru.yandex.qatools.allure.data.transform;

import ru.yandex.qatools.allure.data.AllureFeatures;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import javax.xml.bind.JAXB;
import java.io.File;

import static ru.yandex.qatools.allure.data.utils.XslTransformationUtil.applyTransformations;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.13
 */
public class FeaturesTransformer implements TestRunTransformer {

    private static final String TEST_RUN_TO_FEATURES_1_XSL = "xsl/testrun-to-features-1.xsl";

    private static final String TEST_RUN_TO_FEATURES_2_XSL = "xsl/testrun-to-features-2.xsl";

    private static final String FEATURES_JSON = "features.json";

    @Override
    public void transform(String xml, File outputDirectory) {
        String allureFeaturesBody = applyTransformations(
                xml,
                TEST_RUN_TO_FEATURES_1_XSL,
                TEST_RUN_TO_FEATURES_2_XSL
        );

        AllureFeatures allureFeatures = JAXB.unmarshal(
                allureFeaturesBody,
                AllureFeatures.class
        );

        AllureReportUtils.serialize(outputDirectory, FEATURES_JSON, allureFeatures);
    }
}
