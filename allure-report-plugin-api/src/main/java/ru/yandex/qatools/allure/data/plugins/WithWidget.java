package ru.yandex.qatools.allure.data.plugins;

/**
 * You can add widget to allure report. Widgets are shown at overview
 * tab in the report. There are few supported types of widgets:
 * Key-value list, List with statistics, defects (message with status and count
 * of these messages) and chart.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.04.15
 * @see ProcessPlugin
 * @see ru.yandex.qatools.allure.data.WidgetType
 */
public interface WithWidget {

    /**
     * Name for plugin. Name should be unique and contains only latin characters.
     */
    String getName();

    /**
     * Get plugin widget content. You must implement {@link ProcessPlugin} to collect
     * information from test results.
     */
    Object getWidgetData();
}
