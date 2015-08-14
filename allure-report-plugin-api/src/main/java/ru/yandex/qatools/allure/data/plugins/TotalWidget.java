package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.Statistic;
import ru.yandex.qatools.allure.data.Time;
import ru.yandex.qatools.allure.data.WidgetType;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.08.15
 */
public class TotalWidget implements Widget {

    private Statistic statistic;

    private Time time;

    public TotalWidget(Statistic statistic, Time time) {
        this.statistic = statistic;
        this.time = time;
    }

    @Override
    public String getName() {
        return "total";
    }

    @Override
    public WidgetType getType() {
        return WidgetType.TOTAL;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
