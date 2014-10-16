package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureTimeline;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringReader;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtils.applyTransformations;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class TimelineDataProvider implements DataProvider {

    private static final String TEST_RUN_TO_TIMELINE_XSL_1 = "xsl/testrun-to-timeline-1.xsl";

    private static final String TEST_RUN_TO_TIMELINE_XSL_2 = "xsl/testrun-to-timeline-2.xsl";

    private static final String TIMELINE_JSON = "timeline.json";

    @Override
    public long provide(String testPack, File[] inputDirectories, File outputDirectory) {
        String allureGraphBody = applyTransformations(
                testPack,
                TEST_RUN_TO_TIMELINE_XSL_1,
                TEST_RUN_TO_TIMELINE_XSL_2
        );

        AllureTimeline allureTimeline = JAXB.unmarshal(
                new StringReader(allureGraphBody),
                AllureTimeline.class
        );

        return serialize(outputDirectory, TIMELINE_JSON, allureTimeline);
    }
}
