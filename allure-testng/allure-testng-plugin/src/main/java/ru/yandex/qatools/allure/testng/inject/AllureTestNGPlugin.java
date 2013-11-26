package ru.yandex.qatools.allure.testng.inject;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import ru.yandex.qatools.allure.inject.AbstractInjectPlugin;
import ru.yandex.qatools.allure.inject.EmptyInjector;
import ru.yandex.qatools.allure.inject.Injector;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.11.13
 */
@SuppressWarnings("unused")
@Mojo(name = "allure", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class AllureTestNGPlugin extends AbstractInjectPlugin {
    @Override
    protected Injector getInjector() {
        return new EmptyInjector();
    }
}
