package ru.yandex.qatools.allure.junit.inject;

import org.apache.maven.plugins.annotations.*;
import ru.yandex.qatools.allure.inject.AbstractInjectPlugin;
import ru.yandex.qatools.allure.inject.Injector;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
@SuppressWarnings("unused")
@Mojo(name = "allure", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class AllureJUnitPlugin extends AbstractInjectPlugin {

    @Override
    protected Injector getInjector() {
        return new JUnitRulesInjector();
    }
}
