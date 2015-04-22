package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.WidgetType;

import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.04.15
 */
public abstract class DefaultWidget<T> implements Widget{

    private final String name;

    private final WidgetType type;

    private List<T> data;

    public DefaultWidget(WidgetType type, String name) {
        this.name = name;
        this.type = type;
    }

    @Override
    public WidgetType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
