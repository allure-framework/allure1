package ru.yandex.qatools.allure;

import ru.qatools.properties.DefaultValue;
import ru.qatools.properties.Property;
import ru.qatools.properties.Resource;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/13/13
 */
@SuppressWarnings("unused")
@Resource.Classpath("allure.properties")
public interface AllureConfig {

    @DefaultValue("a^")
    @Property("allure.report.remove.attachments")
    String getRemoveAttachmentsPattern();

    @DefaultValue("target/allure-results")
    @Property("allure.results.directory")
    Path getResultsDirectory();

    @DefaultValue("UTF-8")
    @Property("allure.attachments.encoding")
    Charset getAttachmentsEncoding();

    @DefaultValue("120")
    @Property("allure.max.title.length")
    int getMaxTitleLength();

}
