package ru.yandex.qatools.allure.data.utils

import ru.yandex.qatools.allure.data.Summary

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
@Category(Summary)
class SummaryCategory {
    Summary plus(Summary other) {
        new Summary(steps: steps + other.steps, attachments: attachments + other.attachments);
    }
}
