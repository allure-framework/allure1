package ru.yandex.qatools.allure.data.plugins

import com.google.inject.Inject
import com.google.inject.Singleton
import ru.yandex.qatools.allure.data.io.ResultDirectories

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 29.09.15
 */
@Singleton
class DefaultTestEnvironment implements TestEnvironment {

    @Inject
    DefaultTestEnvironment(@ResultDirectories File...inputDirectories) {

    }

    @Override
    String getTestRunName() {
        return null
    }

    @Override
    String getTestRunUrl() {
        return null
    }

    @Override
    Map<String, String> getEnvironment() {
        return null
    }
}
