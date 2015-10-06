package ru.yandex.qatools.allure.utils

import ru.yandex.qatools.allure.Summary

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
