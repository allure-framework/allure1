package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureTimeline;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class TimelineDataProvider extends AbstractDataProvider {

    private static final String TEST_RUN_TO_TIMELINE_XSL_1 = "xsl/testrun-to-timeline-1.xsl";

    private static final String TEST_RUN_TO_TIMELINE_XSL_2 = "xsl/testrun-to-timeline-2.xsl";

    private static final String TIMELINE_JSON = "timeline.json";

    @Override
    public String[] getXslTransformations() {
        return new String[]{
                TEST_RUN_TO_TIMELINE_XSL_1,
                TEST_RUN_TO_TIMELINE_XSL_2
        };
    }

    @Override
    public String getJsonFileName() {
        return TIMELINE_JSON;
    }

    @Override
    public Class<?> getType() {
        return AllureTimeline.class;
    }
}
