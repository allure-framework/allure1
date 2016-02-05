package ru.yandex.qatools.allure.data.utils

import org.junit.Test
import ru.yandex.qatools.allure.data.Statistic

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 05.02.16
 */
class PluginUtilsTest {

    @Test
    void shouldCompareStatistics() {
        Statistic first = new Statistic(failed: 1, broken: 2, canceled: 3, passed: 4, pending: 5, total: 15)
        Statistic second = new Statistic(failed: 5, broken: 2, canceled: 4, passed: 1, pending: 3, total: 15)
        Statistic third = new Statistic(failed: 5, broken: 2, canceled: 2, passed: 2, pending: 2, total: 13)

        use(PluginUtils) {
            assert first.cmp(first) == 0
            assert first.cmp(second) < 0
            assert first.cmp(third) < 0

            assert second.cmp(first) > 0
            assert second.cmp(second) == 0
            assert second.cmp(third) > 0

            assert third.cmp(first) > 0
            assert third.cmp(second) < 0
            assert third.cmp(third) == 0
        }

    }
}