package ru.yandex.qatools.allure.events;

import org.junit.Test;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 05.05.14
 */
public class RemoveAttachmentsEventTest {

    @Test
    public void shouldDoNothing() throws Exception {
        new RemoveAttachmentsEvent("some").process(null);
    }
}
