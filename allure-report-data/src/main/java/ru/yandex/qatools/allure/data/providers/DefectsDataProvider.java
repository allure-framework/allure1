package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureDefects;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class DefectsDataProvider extends AbstractDataProvider {

    private static final String TEST_RUN_TO_DEFECTS_XSL = "xsl/testrun-to-defects.xsl";

    public static final String DEFECTS_JSON = "defects.json";

    @Override
    public String[] getXslTransformations() {
        return new String[]{TEST_RUN_TO_DEFECTS_XSL};
    }

    @Override
    public String getJsonFileName() {
        return DEFECTS_JSON;
    }

    @Override
    public Class<?> getType() {
        return AllureDefects.class;
    }
}
