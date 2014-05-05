package ru.yandex.qatools.allure.config;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/13/13
 */
@SuppressWarnings("unused")
@Resource.Classpath("allure.report.properties")
public class AllureReportConfig {

    /**
     * Regex to remove attachments in passed test.
     * Will be removed in 1.4.0
     */
    @Property("allure.report.remove.attachments")
    private String removeAttachments = "a^";

    public AllureReportConfig() {
        PropertyLoader.populate(this);
    }

    public String getRemoveAttachments() {
        return removeAttachments;
    }

    public static AllureReportConfig newInstance() {
        return new AllureReportConfig();
    }
}
