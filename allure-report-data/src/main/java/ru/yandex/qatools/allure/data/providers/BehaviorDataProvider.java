package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureBehavior;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.13
 */
public class BehaviorDataProvider extends AbstractDataProvider {

    private static final String TEST_RUN_TO_FEATURES_1_XSL = "xsl/testrun-to-behavior-1.xsl";

    private static final String TEST_RUN_TO_FEATURES_2_XSL = "xsl/testrun-to-behavior-2.xsl";

    private static final String BEHAVIOR_JSON = "behavior.json";

    @Override
    public String[] getXslTransformations() {
        return new String[]{
                TEST_RUN_TO_FEATURES_1_XSL,
                TEST_RUN_TO_FEATURES_2_XSL
        };
    }

    @Override
    public String getJsonFileName() {
        return BEHAVIOR_JSON;
    }

    @Override
    public Class<?> getType() {
        return AllureBehavior.class;
    }
}
