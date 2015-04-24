package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.ListWidgetItem;
import ru.yandex.qatools.allure.data.WidgetType;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.04.15
 */
public class MessageStatusWidget extends DefaultWidget<ListWidgetItem> {

    public MessageStatusWidget(String name) {
        super(WidgetType.MESSAGE_STATUS, name);
    }
}
