package ru.yandex.qatools.allure;

import ru.qatools.properties.DefaultValue;
import ru.qatools.properties.Property;
import ru.qatools.properties.Resource;

import java.nio.file.Path;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
@Resource.Classpath({"command.properties"})
public interface CommandProperties {

    @Property("java.home")
    Path getJavaHome();

    @Property("allure.home")
    Path getAllureHome();

    @DefaultValue("en")
    @Property("allure.locale")
    String getAllureLocale();

    @DefaultValue("allure.properties")
    @Property("allure.config")
    Path getAllureConfig();

    @DefaultValue("-Xms128m")
    @Property("allure.bundle.javaOpts")
    String getBundleJavaOpts();

}
