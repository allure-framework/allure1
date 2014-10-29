package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureXUnit;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class XUnitDataProvider extends AbstractDataProvider {

    private static final String TEST_RUN_TO_XUNIT_XSL = "xsl/testrun-to-xunit.xsl";

    public static final String XUNIT_JSON = "xunit.json";

    @Override
    public String[] getXslTransformations() {
        return new String[]{TEST_RUN_TO_XUNIT_XSL};
    }

    @Override
    public String getJsonFileName() {
        return XUNIT_JSON;
    }

    @Override
    public Class<?> getType() {
        return AllureXUnit.class;
    }
}
